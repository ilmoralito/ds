package ni.edu.uccleon

import org.hibernate.transform.AliasToEntityMapResultTransformer
import org.hibernate.SessionFactory
import static java.util.Calendar.*
import grails.util.Environment

class RequestService {

    SessionFactory sessionFactory
    UserService userService
    AppService appService
    def grailsApplication
    def mailService

    static transactional = false

    Map getRequestDataset(final Long id) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                r.id,
                u.full_name user,
                r.school,
                r.classroom,
                r.datashow,
                r.internet,
                r.audio,
                r.screen,
                r.pointer,
                r.cpu,
                DATE_FORMAT(r.date_of_application, '%Y-%m-%d') dateOfApplication,
                DATE_FORMAT(r.date_created, '%Y-%m-%d') dateCreated,
                DATE_FORMAT(r.last_updated, '%Y-%m-%d') lastUpdated,
                GROUP_CONCAT(h.block ORDER BY h.block) blocks
            FROM
                request r
                    INNER JOIN
                user u ON r.user_id = u.id
                    INNER JOIN
                hour h ON h.request_id = r.id
            WHERE r.id = :id"""
        final sqlQuery = session.createSQLQuery(query)
        final result = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong 'id', id

            uniqueResult()
        }

        result
    }

    Request save(StoreRequestCommand command) {
        User user = userService.find(command.user)
        Request request = new Request()

        request.properties = command.properties.findAll { !(it.key in ['user', 'hours']) }

        command.hours.each { block ->
            request.addToHours(new Hour(block: block))
        }

        user.addToRequests(request)

        if (!request.save(failOnError: true)) {
            throw new Exception('Whoops')
        }

        request
    }

    List<Map> getCurrentDateRequestList() {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                r.id id,
                u.full_name fullName,
                r.datashow datashow,
                r.classroom classroom,
                r.audio audio,
                r.cpu cpu,
                r.internet internet,
                r.pointer pointer,
                r.screen screen,
                r.description description,
                GROUP_CONCAT(h.block ORDER BY block) blocks
            FROM
                request r
                    INNER JOIN
                user u ON r.user_id = u.id
                    INNER JOIN
                hour h ON h.request_id = r.id
            WHERE
                r.date_of_application = curdate()
                    AND r.status = 'pending'
            GROUP BY r.id"""
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            list()
        }

        results
    }

    List<Map> getRequestListInDate(final Date date) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                r.id id,
                u.full_name fullName,
                r.datashow datashow,
                r.classroom classroom,
                r.audio audio,
                r.cpu cpu,
                r.internet internet,
                r.pointer pointer,
                r.screen screen,
                r.description description,
                GROUP_CONCAT(h.block ORDER BY block) blocks
            FROM
                request r
                    INNER JOIN
                user u ON r.user_id = u.id
                    INNER JOIN
                hour h ON h.request_id = r.id
            WHERE
                r.date_of_application = :date
                    AND r.status = 'pending'
            GROUP BY r.id"""
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setDate('date', date)
            list()
        }

        results
    }

    List<Map> getTodayRequestList() {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                r.id id,
                u.full_name fullName,
                r.classroom classroom,
                r.school school,
                r.status status,
                GROUP_CONCAT(h.block ORDER BY block) blocks
            FROM
                request r
                    INNER JOIN
                user u ON r.user_id = u.id
                    INNER JOIN
                hour h ON h.request_id = r.id
            WHERE
                r.date_of_application = curdate()
            GROUP BY 1"""
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            list()
        }

        results
    }

    List<Map> getRequestListBetweenDates(final Date since, final Date till) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                r.id id,
                u.full_name fullName,
                r.classroom classroom,
                r.school school,
                r.status status,
                GROUP_CONCAT(h.block ORDER BY block) blocks
            FROM
                request r
                    INNER JOIN
                user u ON r.user_id = u.id
                    INNER JOIN
                hour h ON h.request_id = r.id
            WHERE
                r.date_of_application >= :since
                    and r.date_of_application <= :till
            GROUP BY 1"""
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setDate 'since', since
            setDate 'till', till

            list()
        }

        results
    }

    List<Map> listTodayActivities() {
        List<Map> results = getCurrentDateRequestList()
        List<Map> data = results.inject([]){ accumulator, currentValue ->
            Map newMap = [:]

            currentValue.each { item ->
                newMap[item.key] = item.key == 'classroom' ? appService.getClassroomCodeOrName(item.value) : item.value
            }

            accumulator << newMap

            accumulator
        }

        data
    }



    List<Map> getRequestListGroupedByBlock(final List<Map> requestList) {
        requestList.groupBy { it.blocks[0] }.collect {
            [
                block: it.key.toInteger() + 1,
                requests: it.value.collect { Map request ->
                    [
                        id: request.id,
                        fullName: request.fullName,
                        classroom: request.classroom,
                        school: request.school,
                        status: request.status,
                        blocks: request.blocks.tokenize(',')
                    ]
                }
            ]
        }.sort { a, b -> a.block <=> b.block }
    }

    def getTotal(Date date, opt) {
        def criteria = Request.createCriteria()
        def total = criteria.count() {
            eq "dateOfApplication", date
            eq opt, true
        }

        def max = (opt == "audio") ? grailsApplication.config.ni.edu.uccleon.speakers : grailsApplication.config.ni.edu.uccleon.screens

        return (total == max) ? true : false
    }

    def getInfoToAddHours(Date date) {
        def requests = Request.requestFromTo(date, date).list()
        def datashows = grailsApplication.config.ni.edu.uccleon.datashows.size()
        def blocks = getDayOfWeekBlocks(date[Calendar.DAY_OF_WEEK])

        [requests: requests, datashows: datashows, day: day, blocks: blocks]
    }

    Integer getDayOfWeekBlocks(Integer dayOfWeek, String school = null) {
        ConfigObject config = grailsApplication.config.ni.edu.uccleon
        Boolean specialization = school in ['Especializacion', 'Educacion continua']

        if (dayOfWeek == 1) {
            if (specialization) {
                config.specialization.sunday.blocks
            } else {
                config.sunday.blocks
            }
        } else if (dayOfWeek == 7) {
            if (specialization) {
                config.specialization.saturday.blocks
            } else {
                config.saturday.blocks
            }
        } else {
            config.blocks
        }
    }

    def mergedClassrooms() {
        def cls = grailsApplication.config.ni.edu.uccleon.cls
        def classroomsMerged = cls["B"] + cls["C"] + cls["D"] + cls["E"] + cls["K"] + cls["undefined"]
        def classrooms = classroomsMerged.collect { classroom ->
            if (classroom.containsKey("name")) {
                classroom
            } else {
                [code:classroom.code, name:classroom.code]
            }
        }

        classrooms.sort { it.name }
    }

    List<Integer> getDatashow(String school, Integer dayOfWeek) {
        ConfigObject config = grailsApplication.config.ni.edu.uccleon
        def datashow = config.data.find { it.coordination == school }.datashow[dayOfWeek - 1]
        List<Integer> list = []

        if (datashow instanceof Integer) {
            list << datashow
        } else {
            datashow
        }
    }

    Integer getMediaQuantityByDateOfApplication(String media, List<Request> requests) {
        Integer result = requests.count { request -> request[media] }

        result
    }

    List<Request> getRequestsBetweenDates(Date from, Date to) {
        List<Request> requests = Request.where {
            dateOfApplication >= from.clearTime() && dateOfApplication <= to.clearTime()
        }.list()

        requests
    }

    List<Request> getRequestStatus(List<Request> requests, String status = 'pending') {
        requests.findAll { request ->
            request.status == status
        }
    }

    Request getAdministrableRequest(Long id) {
        Request.where { id == id && status == 'pending' && school in userService.getCurrentUserSchools() }.get()
    }

    List<Integer> getYearsOfApplications() {
        Request.executeQuery('''
            SELECT DISTINCT
                (YEAR(r.dateOfApplication)) AS year
            FROM
                Request r
            ORDER BY year DESC
        ''')
    }

    List<Integer> getYearListByFaculty(final String school) {
        Request.executeQuery('''
            SELECT DISTINCT
                (YEAR(r.dateOfApplication))
            FROM
                Request r
            WHERE
                r.school = :school
            ORDER BY 1 DESC
        ''', [school: school])
    }

    List getProjectorReport() {
        Request.executeQuery('''
            SELECT
                new map (r.datashow AS datashow, COUNT(*) AS count)
            FROM
                Request r
            GROUP BY 1
            ORDER BY 2 DESC
        ''')
    }

    List getProjectorReportByYear(final Integer year) {
        Request.executeQuery('''
            SELECT
                new map (r.datashow AS datashow, COUNT(*) AS count)
            FROM
                Request r
            WHERE
                YEAR(r.dateOfApplication) = :year
            GROUP BY 1
            ORDER BY 2 DESC
        ''', [year: year])
    }

    void sendSummaryToManagers() {
        Date date = new Date()
        Integer year = date[YEAR]
        Integer month = date[MONTH] + 1
        String monthName = new java.text.DateFormatSymbols(new Locale('es')).months[month]
        List<String> targetEmails = []

        if (Environment.current == Environment.DEVELOPMENT) {
            targetEmails = User.findAllByRole('admin').email
        } else {
            [System.env.EMAIL_DELEGATE_RECTORY, System.env.EMAIL_ACADEMIC_DIRECTOR]
        }

        List<User> recipients =  User.findAllByEmailInList(targetEmails)
        List summary = Request.executeQuery("""
            SELECT
                r.school,
                SUM(CASE
                    WHEN r.status = 'pending' THEN 1
                    ELSE 0
                END) AS pending,
                SUM(CASE
                    WHEN r.status = 'attended' THEN 1
                    ELSE 0
                END) AS attended,
                SUM(CASE
                    WHEN r.status = 'absent' THEN 1
                    ELSE 0
                END) AS absent,
                SUM(CASE
                    WHEN r.status = 'canceled' THEN 1
                    ELSE 0
                END) AS canceled,
                COUNT(r.status) AS total
            FROM
                Request r
                    LEFT JOIN
                r.user u
            WHERE
                YEAR(r.dateOfApplication) = :year
                    AND MONTH(r.dateOfApplication) = :month
            GROUP BY 1
            ORDER BY count(r.status) DESC
        """,[year: year, month: month])

        mailService.sendMail {
            to recipients.email.toArray()
            from 'mario.martinez@ucc.edu.ni'
            subject "Reporte de solicitudes de medios audiovisuales del mes de $monthName del $year"
            body view: '/mails/summary', model: [monthName: monthName, year: year, summary: summary.collect { s ->
                [school: s[0], pending: s[1], attended: s[2], absent: s[3], canceled: s[4], total: s[5] ]
            }]
        }
    }

    List<Map<String, Object>> summaryByCoordination(final String school, final Integer month, final Integer year) {
        final session = sessionFactory.currentSession
        final String query
        if (year) {
            query = """
                SELECT
                    u.id AS id,
                    u.full_name AS fullName,
                    SUM(CASE WHEN r.status = 'pending' THEN 1 ELSE 0 END) AS pending,
                    SUM(CASE WHEN r.status = 'attended' THEN 1 ELSE 0 END) AS attended,
                    SUM(CASE WHEN r.status = 'absent' THEN 1 ELSE 0 END) AS absent,
                    SUM(CASE WHEN r.status = 'canceled' THEN 1 ELSE 0 END) AS canceled,
                    COUNT(r.status) AS total
                FROM
                    user u
                        INNER JOIN
                    request r ON u.id = r.user_id
                WHERE
                    r.school = :school
                        AND MONTH(r.date_of_application) = :month
                        AND YEAR(r.date_of_application) = :year
                GROUP BY id, fullName
                ORDER BY total DESC"""
        } else {
            query = """
                SELECT
                    u.id AS id,
                    u.full_name AS fullName,
                    SUM(CASE WHEN r.status = 'pending' THEN 1 ELSE 0 END) AS pending,
                    SUM(CASE WHEN r.status = 'attended' THEN 1 ELSE 0 END) AS attended,
                    SUM(CASE WHEN r.status = 'absent' THEN 1 ELSE 0 END) AS absent,
                    SUM(CASE WHEN r.status = 'canceled' THEN 1 ELSE 0 END) AS canceled,
                    COUNT(r.status) AS total
                FROM
                    user u
                        INNER JOIN
                    request r ON u.id = r.user_id
                WHERE
                    r.school = :school
                        AND MONTH(r.date_of_application) = :month
                GROUP BY id, fullName
                ORDER BY total DESC"""
        }

        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setString('school', school)
            setInteger('month', month)
            if (year) {
                setInteger('year', year)
            }

            list()
        }

        results
    }

    List<Map<String, Object>> summaryByUser(final Long userId, final String school, final Integer month, final Integer year) {
        final session = sessionFactory.currentSession
        final String query
        if (year) {
            query = '''
                SELECT
                    r.id AS id, r.date_of_application AS date, r.status AS status
                FROM
                    request r
                        INNER JOIN
                    user u ON r.user_id = u.id
                WHERE
                    u.id = :userId AND r.school = :school
                        AND MONTH(r.date_of_application) = :month
                        AND YEAR(r.date_of_application) = :year
                ORDER BY date DESC'''
        } else {
            query = '''
                SELECT
                    r.id AS id, r.date_of_application AS date, r.status AS status
                FROM
                    request r
                        INNER JOIN
                    user u ON r.user_id = u.id
                WHERE
                    u.id = :userId AND r.school = :school
                        AND MONTH(r.date_of_application) = :month
                ORDER BY date DESC'''
        }
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong('userId', userId)
            setString('school', school)
            setInteger('month', month)

            if (year) {
                setInteger('year', year)
            }

            list()
        }

        results
    }

    List<Map<String, Object>> resumen(final Integer year) {
        List<Map> results = []

        if (year) {
            results = Request.executeQuery('''
                SELECT new map (
                    MONTH(date_of_application) AS month,
                    MONTHNAME(date_of_application) as monthName,
                    COUNT(*) as quantity
                )
                FROM Request AS r
                WHERE
                    YEAR(r.dateOfApplication) = :year
                GROUP BY 1, 2
                ORDER BY 1 DESC
            ''',[year: year])
        } else {
            results = Request.executeQuery('''
                SELECT new map (
                    MONTH(date_of_application) AS month,
                    MONTHNAME(date_of_application) AS monthName,
                    COUNT(*) AS quantity
                )
                FROM Request AS r
                GROUP BY 1, 2
                ORDER BY 1 DESC
            ''')
        }

        results
    }

    List<Map<String, Object>> reportSummary(final Integer month, final Integer year) {
        List results = []

        if (year) {
            results = Request.executeQuery("""
                SELECT new map (
                    r.school AS school,
                    SUM(CASE WHEN r.status = 'pending' THEN 1 ELSE 0 END) AS pending,
                    SUM(CASE WHEN r.status = 'attended' THEN 1 ELSE 0 END) AS attended,
                    SUM(CASE WHEN r.status = 'absent' THEN 1 ELSE 0 END) AS absent,
                    SUM(CASE WHEN r.status = 'canceled' THEN 1 ELSE 0 END) AS canceled,
                    COUNT(r.status) as total
                )
                FROM Request AS r
                WHERE
                    MONTH(r.dateOfApplication) = :month
                AND
                    YEAR(r.dateOfApplication) = :year
                GROUP BY 1
                ORDER BY 6 DESC
            """,[month: month, year: year])
        } else {
            results = Request.executeQuery("""
                SELECT new map (
                    r.school AS school,
                    SUM(CASE WHEN r.status = 'pending' THEN 1 ELSE 0 END) AS pending,
                    SUM(CASE WHEN r.status = 'attended' THEN 1 ELSE 0 END) AS attended,
                    SUM(CASE WHEN r.status = 'absent' THEN 1 ELSE 0 END) AS absent,
                    SUM(CASE WHEN r.status = 'canceled' THEN 1 ELSE 0 END) AS canceled,
                    COUNT(r.status) AS total
                )
                FROM Request AS r
                WHERE
                    MONTH(r.dateOfApplication) = :month
                GROUP BY 1
                ORDER BY 6 DESC
            """,[month: month])
        }

        results
    }

    List<Map<String, Object>> reportPerDay(final Integer year) {
        List results = []

        if (year) {
            results = Request.executeQuery("""
                SELECT new map (
                    DAYNAME(r.dateOfApplication) AS day,
                    count(*) AS quantity,
                    CONCAT(ROUND((COUNT(*) / (SELECT COUNT(*) FROM Request AS req WHERE YEAR(req.dateOfApplication) = :year) * 100), 2), '%') AS percentage
                )
                FROM Request as r
                WHERE YEAR(r.dateOfApplication) = :year
                GROUP BY DAYNAME(r.dateOfApplication)
                ORDER BY quantity DESC
            """, [year: year])
        } else {
            results = Request.executeQuery("""
                SELECT new map (
                    DAYNAME(r.dateOfApplication) AS day,
                    COUNT(*) AS quantity,
                    CONCAT(ROUND((COUNT(*) / (SELECT COUNT(*) FROM Request) * 100), 2), '%') AS percentage
                )
                FROM Request AS r
                GROUP BY DAYNAME(r.dateOfApplication)
                ORDER BY quantity DESC
            """)
        }

        results
    }

    List<Map<String, Object>> reportByBlock(final Integer year) {
        final session = sessionFactory.currentSession
        final String queryWithYear = '''
            SELECT
                h.block AS block, COUNT(*) AS count
            FROM
                request r
                    INNER JOIN
                hour h ON r.id = h.request_id
            WHERE
                YEAR(r.date_of_application) = :year
            GROUP BY 1
            ORDER BY 2 DESC;
        '''
        final String query = '''
            SELECT
                h.block AS block, COUNT(*) AS count
            FROM
                request r
                    INNER JOIN
                hour h ON r.id = h.request_id
            GROUP BY 1
            ORDER BY 2 DESC;
        '''
        final sqlQuery = session.createSQLQuery(year ? queryWithYear : query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            if (year) {
                setInteger('year', year)
            }

            list()
        }

        results
    }

    List<Map<String, Object>> classReportPerYear(final Integer year) {
        Request.executeQuery('''
            SELECT
                new map(r.classroom AS classroom, COUNT(*) AS count)
            FROM
                Request r
            WHERE
                YEAR(r.dateOfApplication) = :year
            GROUP BY 1
            ORDER BY 2 DESC
        ''', [year: year])
    }

    List<Map<String, Object>> classReport() {
        Request.executeQuery('''
            SELECT
                new map(r.classroom AS classroom, COUNT(*) AS count)
            FROM
                Request r
            GROUP BY 1
            ORDER BY 2 DESC
        ''')
    }

    List<Map<String, Object>> coordinationReportByYear(final Integer year) {
        Request.executeQuery('''
            SELECT
                new map (r.school AS school, COUNT(*) AS count)
            FROM
                Request r
            WHERE
                YEAR(r.dateOfApplication) = :year
            GROUP BY 1
            ORDER BY 2 DESC
        ''', [year: year])
    }

    List<Map<String, Object>> coordinationReport() {
        Request.executeQuery('''
            SELECT
                new map (r.school AS school, COUNT(*) AS count)
            FROM
                Request r
            GROUP BY 1
            ORDER BY 2 DESC
        ''')
    }

    List<Map<String, Object>> coordinationSummaryInYear(final String school, final Integer year) {
        Request.executeQuery('''
            SELECT new map (
                MONTH(r.dateOfApplication) AS month,
                MONTHNAME(r.dateOfApplication) AS monthName,
                COUNT(*) AS count
            )
            FROM
                Request r
            WHERE
                r.school = :school
                    AND YEAR(r.dateOfApplication) = :year
            GROUP BY 1 , 2
            ORDER BY 1 DESC
        ''', [school: school, year: year])
    }

    List<Map<String, Object>> coordinationSummary(final String school) {
        Request.executeQuery('''
            SELECT new map (
                MONTH(r.dateOfApplication) AS month,
                MONTHNAME(r.dateOfApplication) AS monthName,
                COUNT(*) AS count
            )
            FROM
                Request r
            WHERE
                r.school = :school
            GROUP BY 1 , 2
            ORDER BY 1 DESC
        ''', [school: school])
    }

    List<Map<String, Object>> summaryByApplicant() {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                u.id AS id,
                u.full_name AS fullName,
                SUM(CASE
                    WHEN r.status = 'pending' THEN 1
                    ELSE 0
                END) AS pending,
                SUM(CASE
                    WHEN r.status = 'attended' THEN 1
                    ELSE 0
                END) AS attended,
                SUM(CASE
                    WHEN r.status = 'absent' THEN 1
                    ELSE 0
                END) AS absent,
                SUM(CASE
                    WHEN r.status = 'canceled' THEN 1
                    ELSE 0
                END) AS canceled,
                COUNT(r.status) AS total
            FROM
                request r
                    INNER JOIN
                user u ON r.user_id = u.id
            GROUP BY id, fullName
            ORDER BY total DESC
        """
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            list()
        }

        results
    }

    List<Map<String, Object>> summaryByApplicantInYear(final Integer year) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                u.id AS id,
                u.full_name AS fullName,
                SUM(CASE
                    WHEN r.status = 'pending' THEN 1
                    ELSE 0
                END) AS pending,
                SUM(CASE
                    WHEN r.status = 'attended' THEN 1
                    ELSE 0
                END) AS attended,
                SUM(CASE
                    WHEN r.status = 'absent' THEN 1
                    ELSE 0
                END) AS absent,
                SUM(CASE
                    WHEN r.status = 'canceled' THEN 1
                    ELSE 0
                END) AS canceled,
                COUNT(r.status) AS total
            FROM
                request r
                    INNER JOIN
                user u ON r.user_id = u.id
            WHERE
                YEAR(r.date_of_application) = :year
            GROUP BY id, fullName
            ORDER BY total DESC
        """
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setInteger('year', year)

            list()
        }

        results
    }

    List<Map<String, Object>> summaryOfApplicationsPerApplicant(final Long id) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                r.school AS school,
                SUM(CASE
                    WHEN r.status = 'pending' THEN 1
                    ELSE 0
                END) AS pending,
                SUM(CASE
                    WHEN r.status = 'attended' THEN 1
                    ELSE 0
                END) AS attended,
                SUM(CASE
                    WHEN r.status = 'absent' THEN 1
                    ELSE 0
                END) AS absent,
                SUM(CASE
                    WHEN r.status = 'canceled' THEN 1
                    ELSE 0
                END) AS canceled,
                COUNT(r.status) AS total
            FROM
                request r
                    INNER JOIN
                user u ON r.user_id = u.id
            WHERE
                u.id = :id
            GROUP BY school
            ORDER BY total DESC
        """
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong('id', id)

            list()
        }

        results
    }

    List<Map<String, Object>> summaryOfApplicationsPerApplicantInYear(final Long id, final Integer year) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                r.school AS school,
                SUM(CASE
                    WHEN r.status = 'pending' THEN 1
                    ELSE 0
                END) AS pending,
                SUM(CASE
                    WHEN r.status = 'attended' THEN 1
                    ELSE 0
                END) AS attended,
                SUM(CASE
                    WHEN r.status = 'absent' THEN 1
                    ELSE 0
                END) AS absent,
                SUM(CASE
                    WHEN r.status = 'canceled' THEN 1
                    ELSE 0
                END) AS canceled,
                COUNT(r.status) AS total
            FROM
                request r
                    INNER JOIN
                user u ON r.user_id = u.id
            WHERE
                u.id = :id
                    AND YEAR(r.date_of_application) = :year
            GROUP BY school
            ORDER BY total DESC
        """
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong('id', id)
            setInteger('year', year)

            list()
        }

        results
    }

    List<Map<String, Object>> globalFacultySummary(final String school) {
        Request.executeQuery('''
            SELECT new map (
                MONTH(r.dateOfApplication) AS month,
                MONTHNAME(r.dateOfApplication) AS monthName,
                COUNT(*) AS count
            )
            FROM
                Request r
            WHERE
                r.school = :school
            GROUP BY 1 , 2
            ORDER BY 1 DESC
        ''', [school: school])
    }

    List<Map<String, Object>> annualFacultySummary(final String school, final Integer year) {
        Request.executeQuery('''
            SELECT new map (
                MONTH(r.dateOfApplication) AS month,
                MONTHNAME(r.dateOfApplication) AS monthName,
                COUNT(*) AS count
            )
            FROM
                Request r
            WHERE
                r.school = :school
                    AND YEAR(r.dateOfApplication) = :year
            GROUP BY 1 , 2
            ORDER BY 1 DESC
        ''', [school: school, year: year])
    }

    List<Map<String, Object>> datashowReportSummary(final Integer datashow) {
        final session = sessionFactory.currentSession
        final String query = '''
            SELECT
                us.schools_string AS school, COUNT(r.id) AS count
            FROM
                request r
                    INNER JOIN
                user_schools us ON r.user_id = us.user_id
            where
                r.datashow = :datashow
            GROUP BY 1
            ORDER BY 2 DESC'''
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setInteger('datashow', datashow)

            list()
        }

        results
    }

    List<Map<String, Object>> datashowReportSummaryPerYear(final Integer year, final Integer datashow) {
        final session = sessionFactory.currentSession
        final String query = '''
            SELECT
                us.schools_string AS school, COUNT(r.id) AS count
            FROM
                request r
                    INNER JOIN
                user_schools us ON r.user_id = us.user_id
            where
                r.datashow = :datashow
                    AND YEAR(r.date_of_application) = :year
            GROUP BY 1
            ORDER BY 2 DESC'''
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setInteger('year', year)
            setInteger('datashow', datashow)

            list()
        }

        results
    }
}
