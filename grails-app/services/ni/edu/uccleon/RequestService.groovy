package ni.edu.uccleon

import org.hibernate.transform.AliasToEntityMapResultTransformer
import org.hibernate.SessionFactory
import static java.util.Calendar.*
import grails.util.Environment

class RequestService {
    def grailsApplication
    def userService
    def mailService
    SessionFactory sessionFactory

    static transactional = false

    List<Map<String, Object>> groupRequestListByBlock(List<Request> requestList) {
        Integer blocks = getDayOfWeekBlocks(new Date()[Calendar.DAY_OF_WEEK])
        List<Map<String, Object>> results = []

        (0..blocks).collect { block ->
            Map<String, Object> node = [block: block, requests: requestList.findAll { r -> r.hours.block.min() == block }]

            results.add node
        }

        results.findAll { it.requests }
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

    def getUsersInCurrentUserCoordinations(String role, User user, String action) {
        if (role in ["coordinador", "asistente"]) {
            List<User> users = User.findAllByRoleNotEqualAndEnabled("admin", true, [sort: "fullName", order: "asc"])
            def results = users.findAll { u ->
                user.schools.any { u.schools.contains(it) }
            }

            //Set session user in position 0 in users list
            if (action == 'create') {
                results -= user
                results.plus 0, user
            } else {
                results
            }
        }
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

    // Request that could be edited or removed
    Request getAdministrableRequest(Long id) {
        Request request = Request.where {
            id == id &&
            status == 'pending' &&
            school in userService.getCurrentUserSchools()
        }.get()

        request
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
}
