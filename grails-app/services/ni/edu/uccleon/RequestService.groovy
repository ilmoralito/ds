package ni.edu.uccleon

import static java.util.Calendar.*
import grails.util.Environment

class RequestService {
    def grailsApplication
    def userService
    def mailService

    static transactional = false

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

    List<String> getYearsOfApplications() {
        Request.executeQuery('''
            SELECT DISTINCT YEAR(r.dateOfApplication) AS YEAR
            FROM Request AS r
            ORDER BY YEAR(r.dateOfApplication) DESC'''
        )
    }

    List getProjectorReport() {
        Request.executeQuery('''
            SELECT r.datashow AS datashow, count(*) AS count
            FROM Request AS r
            GROUP BY r.datashow
            ORDER BY count(*) DESC
        ''')
    }

    List getProjectorReportByYear(final Integer year) {
        Date firstDayOfTheYear = new Date().clearTime()
        Date lastDayOfTheYear = firstDayOfTheYear.clone()

        firstDayOfTheYear.set(year: year, month: 0, date: 1)
        lastDayOfTheYear.set(year: year,  month: 11, date: 31)

        Request.executeQuery('''
            SELECT r.datashow AS datashow, count(*) AS count
            FROM Request AS r
            WHERE r.dateOfApplication BETWEEN :firstDayOfTheYear AND :lastDayOfTheYear
            GROUP BY r.datashow
            ORDER BY count(*) DESC''',
            [firstDayOfTheYear: firstDayOfTheYear, lastDayOfTheYear: lastDayOfTheYear])
    }

    void sendSummaryToManagers() {
        Date date = new Date()
        Integer year = date[YEAR]
        Integer month = date[MONTH] + 1
        String monthName = new java.text.DateFormatSymbols(new Locale('es')).months[month]
        List<String> targetEmails = []

        if (Environment.current == Environment.DEVELOPMENT) {
            targetEmails = ['mario.martinez@ucc.edu.ni', 'sergio@lopez.ucc.edu.ni']
        } else {
            targetEmails = ['guissella.gonzalez@ucc.edu.ni', 'marta.torres@ucc.edu.ni']
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
}
