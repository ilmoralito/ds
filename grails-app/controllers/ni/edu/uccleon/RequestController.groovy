package ni.edu.uccleon

import static java.util.Calendar.*
import grails.gorm.DetachedCriteria
import org.hibernate.transform.AliasToEntityMapResultTransformer
import org.springframework.web.context.request.RequestContextHolder as RCH

class RequestController {
    def grailsApplication
    def classroomService
    def requestService
    def userService
    def appService

    static defaultAction = 'list'
    static allowedMethods = [
        buildRequest: 'GET',
        storeRequest: 'POST',
        edit: 'GET',
        update: 'POST',
        list: ['GET', 'POST'],
        show: 'GET',
        delete: 'DELETE',
        updateStatus: 'GET',
        requestsBySchools: ['GET', 'POST'],
        requestsByClassrooms: ['GET', 'POST'],
        requestsByUsers: ['GET', 'POST'],
        changeRequestsStatus: 'POST',
        activity: 'GET',
        todo: 'POST',
        createRequestFromActivity: ['GET', 'POST'],
        report: 'GET',
        reportDetail: 'GET',
        getUserClassroomsAndSchools: 'GET',
        requestsByCoordination: 'GET',
        userStatistics: 'GET',
        userStatisticsDetail: 'GET',
        listOfPendingApplications: 'GET',
        reportBySchool: ['GET', 'POST'],
        reportByClassrooms: ['GET', 'POST'],
        reportByDatashows: ['GET', 'POST'],
        reportByApplicant: ['GET', 'POST'],
        coordinationReportPerApplicant: 'GET',
        reportByBlock: ['GET', 'POST'],
        reportPerDay: ['GET', 'POST'],
        reportPerMonth: ['GET', 'POST']
    ]

    private final MONTHS = [
        'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
    ]

    private final DAYS = [
        [english: 'Sunday', spanish: 'Domingo'],
        [english: 'Monday', spanish: 'Lunes'],
        [english: 'Tuesday', spanish: 'Martes'],
        [english: 'Wednesday', spanish: 'Miercoles'],
        [english: 'Thursday', spanish: 'Jueves'],
        [english: 'Friday', spanish: 'Viernes'],
        [english: 'Saturday', spanish: 'Sabado']
    ]

    private getRequestStatus() {
        [
            pending: 'Pendiente', attended: 'Atendido', absent: 'Sin retirar', canceled: 'Cancelado'
        ]
    }

    def buildRequest(BuildRequestCommand command) {
        if (command.hasErrors()) {
            flash.errors = command
            redirect action: 'listOfPendingApplications'

            return
        }

        [
            school: command.school,
            dateOfApplication: command.dateOfApplication,
            blockWidget: createBlockWidget(command.school, command.dateOfApplication)
        ]
    }

    def storeRequest() {
        Request newRequest = new Request(
            dateOfApplication: params.date('dateOfApplication', 'yyyy-MM-dd'),
            classroom: params.classroom,
            school: params.school,
            description: params.description,
            datashow: params.int('datashow'),
            audio: params.boolean('audio'),
            screen: params.boolean('screen'),
            internet: params.boolean('internet'),
            pointer: params.boolean('pointer'),
            cpu: params.boolean('cpu')
        )

        params.list('hours').each { block ->
            newRequest.addToHours(new Hour(block: block))
        }

        User user = User.get(params.int('user'))

        user.addToRequests(newRequest)

        if (!newRequest.save()) {
            newRequest.errors.allErrors.each { error ->
                log.error "$error.field: $error.defaultMessage"
            }

            flash.message = 'Datos incorrectos'
            redirect uri: request.getHeader('referer')
            return
        }

        flash.message = 'Solicitud creada correctamente'
        redirect action: 'activity', params: [date: params.dateOfApplication]
    }

    def edit(Long id) {
        Request requestInstance = requestService.getAdministrableRequest(id)

        if (!requestInstance || !(userService.getCurrentUser().role in ['coordinador', 'asistente', 'administrativo'])) {
            response.sendError 404
            return false
        }

        [
            requestInstance: requestInstance,
            blockWidget: createBlockWidget(
                requestInstance.school,
                requestInstance.dateOfApplication.format('yyyy-MM-dd')
            )
        ]
    }

    def listOfPendingApplications() {
        User currentUser = userService.getCurrentUser()
        String currentUserRole = currentUser.role
        List<Request> requests
        DetachedCriteria query1 = Request.where { school in userService.getCurrentUserSchools() && status == 'pending' }
        DetachedCriteria query2 = Request.where { user == currentUser && status == 'pending' }

        if (currentUserRole in ['coordinador', 'asistente']) {
            requests = query1.list()
        } else if(currentUserRole in ['user', 'administrativo']) {
            requests = query2.list()
        }

        List dataSet = requests.groupBy { it.dateOfApplication.format("yyyy-MM-dd") }.collect { a ->
            [
                dateOfApplication: a.key,
                details: a.value.collect { b ->
                    [
                        id: b.id,
                        userFullName: b.user.fullName,
                        classroom: appService.getClassroomCodeOrName(b.classroom)
                    ]
                }
            ]
        }.sort { a, b ->
            a.dateOfApplication <=> b.dateOfApplication
        }

        [dataSet: dataSet]
    }

    def userStatistics() {
        def requestStatus = this.getRequestStatus()
        def results = Request.findAllByUser(session?.user).groupBy { it.dateOfApplication[Calendar.YEAR] } { it.status }.collectEntries { d ->
            [d.key, d.value.collectEntries { o ->
                [requestStatus[o.key], o.value.size()]
            }]
        }

        results.each { key, value ->
            requestStatus.each { k, v ->
                if (!(v in value.keySet())) {
                    value[v] = 0
                }
            }

            value["TOTAL"] = value*.value.sum()
        }

        [results: results.sort { -it.key }]
    }

    def userStatisticsDetail(Integer y) {
        List months = MONTHS
        def requestStatus = this.getRequestStatus()
        def query = Request.where {
            user == session?.user && year(dateOfApplication) == y
        }

        def results = query.list().groupBy { it.dateOfApplication[Calendar.MONTH] } { it.status }.collectEntries { d ->
            [months[d.key], d.value.collectEntries { o ->
                [requestStatus[o.key], o.value.size()]
            }]
        }

        results.values().each { instance ->
            requestStatus.each { status ->
                if (!(status.value in instance.keySet())) {
                    instance[status.value] = 0
                }
            }
        }

        [results: results]
    }

    def reportDetail(Integer y, String m, String s) {
        List<Request> requests = Request.where {
            school == s &&
            month(dateOfApplication) == MONTHS.indexOf(m) + 1 &&
            year(dateOfApplication) == y
        }.list()

        List data = requests.groupBy { it.user.fullName } { it.status }.collect { o ->
            [
                user: o.key,
                pending: o?.value?.pending?.size() ?: 0,
                attended: o?.value?.attended?.size() ?: 0,
                absent: o?.value?.absent?.size() ?: 0,
                canceled: o?.value?.canceled?.size() ?: 0,
                total: o.value*.value.flatten().size()
            ]
        }.sort { -it.total }

        [data: data]
    }

    def summary() {
        List<Request> requests = Request.list()
        Map group = requests.groupBy { it.dateOfApplication[YEAR] } { it.dateOfApplication[MONTH] } { it.status }
        List data = group.collect { o ->
            [
                year: o.key,
                months: (0..11).collect { t ->
                    [
                        month: MONTHS[t],
                        pending: o.value.find { it.key == t }.find { it.value },
                        attended: o.value.find { it.key == t && it.value.attended }?.value?.size() ?: 0,
                        absent: o.value.find { it.key == t && it.value.absent }?.value?.size() ?: 0,
                        canceled: o.value.find { it.key == t && it.value.canceled }?.value?.size() ?: 0,
                        total: o.value.find { it.key == t }?.value ?: 0
                    ]
                }
            ]
        }

        [data: data]
    }

    def list() {
        ConfigObject config = grailsApplication.config.ni.edu.uccleon
        Map schoolsAndDepartments = config.schoolsAndDepartments
        List requestStatus = config.requestStatus
        def requests
        def users = params.list('users')
        def schools = params.list('schools')
        def departments = params.list('departments')
        def classrooms = params.list('classrooms')
        def types = params.list('types')
        def status = params.list('status')
        def requestFromDate = params.date('requestFromDate', 'yyyy-MM-dd')
        def requestToDate = params.date('requestToDate', 'yyyy-MM-dd')
        def userInstance = session?.user
        def role = userInstance?.role
        Date today = new Date()
        Date from = requestFromDate ?: today
        Date to = requestToDate ?: today

        if (users || schools || departments || classrooms || types || status || requestFromDate || requestToDate) {
            requests = Request.filter(users, schools, departments, classrooms, types, status, from, to).requestFromTo(from, to).list()
        } else {
            requests = Request.todayRequest().list()
        }

        def blocks = requestService.getDayOfWeekBlocks(today[Calendar.DAY_OF_WEEK])
        def requestsByBlock = []

        //group requests by starting block
        (0..blocks).collect { block ->
            def node = [block: block, requests: requests.findAll { r -> r.hours.block.min() == block }]

            requestsByBlock.add node
        }

        [
            requestStatus: requestStatus,
            schoolsAndDepartments: schoolsAndDepartments,
            classrooms: requestService.mergedClassrooms(),
            requestsByBlock: requestsByBlock.findAll { it.requests },
            users: User.findAllByRoleAndEnabled('user', true, [sort: 'fullName'])
        ]
    }

    def getUserClassroomsAndSchools(String userEmail) {
        def user = User.findByEmail(userEmail)
        def userClassrooms = userService.transformUserClassrooms(user.classrooms as List)
        def userSchools = user.schools.findAll { s ->
            s in session?.user?.schools
        }

        render(contentType: "application/json") {
            classrooms = userClassrooms
            schools = userSchools
        }
    }

    def requestsByCoordination() {
        def user = session.user.refresh()
        def userSchools = user?.schools
        def criteria = Request.createCriteria()
        def result = criteria {
            "in" "school", userSchools
        }

        List<String> months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]

        def results = result.groupBy { it.dateOfApplication[Calendar.YEAR] } { it.school } { it.dateOfApplication[Calendar.MONTH] }.collectEntries { d ->
            [d.key, d.value.collectEntries { o ->
                [o.key, o.value.collectEntries { x ->
                    [months[x.key], x.value.size()]
                }]
            }]
        }

        [results: results]
    }

    def show(Long id) {
        Request requestInstance = Request.get(id)

        if (!requestInstance) {
            response.sendError 404
        }

        [requestInstance: requestInstance]
    }

    def delete() {
        Request requestInstance = requestService.getAdministrableRequest(params.long('id'))

        if (!requestInstance || !(userService.getCurrentUser().role in ['coordinador', 'asistente', 'administrativo'])) {
            response.sendError 404
            return false
        }

        requestInstance.delete()

        flash.message = "Solicitud eliminada"
        redirect action: "listOfPendingApplications"
    }

    def updateStatus(Long id) {
        Request requestInstance = Request.get(id)

        if (!requestInstance) {
            response.sendError 404
        }

        requestInstance.properties['status'] = params.status ?: this.getNextRequestStatus(requestInstance.status)

        // I need to set validate false in order to pass domain class dateOfApplication constraints
        if (!requestInstance.save(validate: false)) {
            requestInstance.errors.allErrors.each { error ->
                log.error "[field: $error.field, message: $error.defaultMessage]"
            }
        }

        flash.message = requestInstance.hasErrors() ? "A ocurrido un error" : "Cambio de estado aplicado"
        redirect uri: request.getHeader('referer')
    }

    def activity(String date) {
        Date today = new Date()
        Date dateOfApplication = date ? today.parse('yyyy-MM-dd', date) : today
        List<Request> requestsBetweenDates = requestService.getRequestsBetweenDates(dateOfApplication, dateOfApplication)
        List<Request> requests = requestService.getRequestStatus(requestsBetweenDates)
        Integer datashows = grailsApplication.config.ni.edu.uccleon.datashows.size()

        Closure layout = {
            User currentUser = userService.getCurrentUser()

            if (!currentUser) {
                return 'oneColumn'
            } else if (currentUser.role != 'admin') {
                return 'twoColumns'
            } else {
                return 'threeColumns'
            }
        }

        [
            requests: requests,
            datashows: datashows,
            dateOfApplication: dateOfApplication,
            layout: layout()
        ]
    }

    def todo(Integer id, Integer datashow, Integer block) {
      def req = Request.get id

      req.datashow = datashow
      req.hours.clear()
      req.addToHours new Hour(block:block)

      if (!req.save(flush:true)) {
        render(contentType:"application/json") {
          status = false
        }
      } else {
        render(contentType:"application/json") {
          fullName = req.user.fullName
          classroom = req.classroom
        }
      }
    }

    def createRequestFromActivity() {
      if (request.post) {
        def r = new Request (
          dateOfApplication:params.date("dateOfApplication", "yyyy-MM-dd"),
          classroom:params?.classroom,
          school:params?.school,
          description:params?.description,
          datashow:params.int("datashow"),
          user:session?.user
        )

        r.addToHours(new Hour(block:params.int("block")))

        if (!r.save(flush:true)) {
          r.errors.allErrors.each { e ->
            log.error "[$e.field: $e.defaultMessage]"
          }

          flash.message = "A ocurrido un error. Verifica que todos los datos esten completados"
        } else {
          redirect action:"activity", params:[dateSelected:params?.dateOfApplication, datashow:params.datashow, block:params.block]
          return
        }
      }

      [userClassrooms:userService.transformUserClassrooms(session?.user?.refresh().classrooms as List)]
    }

    def requestsBy(Date from, Date to, String type) {
      def results
      def totalRequestInYears

      switch(type) {
        case "resumen":
          results = Request.list().groupBy { it.dateOfApplication[Calendar.YEAR] } { it.dateOfApplication[Calendar.MONTH] + 1 }.collectEntries {
            [it.key, it.value.collectEntries { d ->
              [d.key, d.value.size()]
            }]
          }

          totalRequestInYears = results.collectEntries { year ->
            [year.key, year.value.collect { it.value }.sum()]
          }

          break
      }

      [results:results, totalRequestInYears:totalRequestInYears, total:!(type in ['resumen', 'classrooms', 'day']) ? results.count.sum() : 0, type:type]
    }

    def changeRequestsStatus() {
        if (params.requests) {
            DetachedCriteria<Request> query = Request.where {
                id in params.list('requests')*.toLong()
            }

            Integer total = query.updateAll(status: params.newStatus)
            flash.message = "$total solicitudes afectadas"
        } else {
            flash.message = "Seleciona alguna solicitud para continuar"
        }

        redirect action: 'list', params: [requestFromDate: params.fromDate, requestToDate: params.toDate]
    }

    def listBy(String type) {
        def requests

        switch(type) {
            case "user":
                requests = Request.listByUser(session?.user).list(params)
            break
        }

        [requests: requests]
    }

    def reportBySchool() {
        List results = []

        if (request.method == 'POST') {
            def (Date firstDayOfTheYear, Date lastDayOfTheYear) = getFirstAndLastDayOfTheYear(params.int('year'))

            results = Request.executeQuery('''
                SELECT Month(r.dateOfApplication), Count(*)
                FROM Request AS r
                WHERE r.dateOfApplication BETWEEN :firstDayOfTheYear AND :lastDayOfTheYear AND r.school = :school
                GROUP BY Month(r.dateOfApplication)
                ORDER BY MONTH(r.dateOfApplication) DESC'''
            , [firstDayOfTheYear: firstDayOfTheYear, lastDayOfTheYear: lastDayOfTheYear, school: params?.school])
        }

        List<Map> data = results.collect {
            [month: MONTHS[it[0]], quantity: it[1]]
        }

        [schoolsFilter: createSchoolsFilter(), data: data]
    }

    def reportByClassrooms() {
        List results = []
        if (request.method == 'POST') {
            def (Date firstDayOfTheYear, Date lastDayOfTheYear) = getFirstAndLastDayOfTheYear(params.int('year'))

            results = Request.executeQuery('''
                SELECT r.classroom AS classroom, count(*) AS count
                FROM Request AS r
                WHERE r.dateOfApplication BETWEEN :firstDayOfTheYear AND :lastDayOfTheYear
                GROUP BY r.classroom
                ORDER BY count(*) DESC''', [firstDayOfTheYear: firstDayOfTheYear, lastDayOfTheYear: lastDayOfTheYear])
        } else {
            results = Request.executeQuery('''
                SELECT r.classroom AS classroom, count(*) AS count
                FROM Request AS r
                GROUP BY r.classroom
                ORDER BY count(*) DESC
            ''')
        }

        [yearFilter: createYearFilter(), results: results.collect {
            [classroom: it[0], quantity: it[1]]
        }]
    }

    def reportByApplicant() {
        List results = []

        if (request.method == 'POST') {
            def (Date firstDayOfTheYear, Date lastDayOfTheYear) = getFirstAndLastDayOfTheYear(params.int('year'))

            results = Request.createCriteria().list {
                resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                between 'dateOfApplication', firstDayOfTheYear, lastDayOfTheYear

                projections {
                    user {
                        property 'fullName', 'applicant'
                    }
                    count 'id', 'quantity'
                    groupProperty 'user'
                }

                order 'quantity', 'desc'
            }
        } else {
            results = Request.createCriteria().list {
                resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)

                projections {
                    user {
                        property 'fullName', 'applicant'
                    }
                    count 'id', 'quantity'
                    groupProperty 'user'
                }

                order 'quantity', 'desc'
            }
        }

        [yearFilter: createYearFilter(), results: results]
    }

    def coordinationReportPerApplicant(final Integer year) {
        List results = []

        if (year) {
            def (Date firstDayOfTheYear, Date lastDayOfTheYear) = getFirstAndLastDayOfTheYear(params.int('year'))

            results = Request.createCriteria().list {
                between 'dateOfApplication', firstDayOfTheYear, lastDayOfTheYear

                user {
                    eq 'fullName', params?.applicant
                }

                projections {
                    property 'school', 'coordination'
                    count 'id', 'quantity'
                    groupProperty 'school'
                }

                order 'quantity', 'desc'

                resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
            }
        } else {
            results = Request.createCriteria().list {
                user {
                    eq 'fullName', params?.applicant
                }

                projections {
                    property 'school', 'coordination'
                    count 'id', 'quantity'
                    groupProperty 'school'
                }

                order 'quantity', 'desc'

                resultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
            }
        }

        [yearFilter: createYearFilter(), results: results]
    }

    def reportByDatashows() {
        List results = []

        if (request.method == 'GET') {
            results = requestService.getProjectorReport()
        } else {
            results = requestService.getProjectorReportByYear(params.int('year'))
        }

        [yearFilter: createYearFilter(), results: results.collect {
            [datashow: it[0], quantity: it[1]]
        }]
    }

    def reportByBlock() {
        List results = []

        if (request.method == 'POST') {
            def (Date firstDayOfTheYear, Date lastDayOfTheYear) = getFirstAndLastDayOfTheYear(params.int('year'))

            results = Request.requestsByBlocks().requestFromTo(firstDayOfTheYear, lastDayOfTheYear).list()
        } else {
            results = Request.requestsByBlocks().list()
        }

        [yearFilter: createYearFilter(), results: results]
    }

    def reportPerDay() {
        List results = []

        if (request.method == 'POST') {
            def (Date firstDayOfTheYear, Date lastDayOfTheYear) = getFirstAndLastDayOfTheYear(params.int('year'))

            results = Request.executeQuery('''
                SELECT DAYNAME(r.dateOfApplication), count(*) AS quantity
                FROM Request as r
                WHERE r.dateOfApplication BETWEEN :firstDayOfTheYear AND :lastDayOfTheYear
                GROUP BY DAYNAME(r.dateOfApplication)
                ORDER BY quantity DESC''',
                [firstDayOfTheYear: firstDayOfTheYear, lastDayOfTheYear: lastDayOfTheYear])
        } else {
            results = Request.executeQuery('''
                SELECT DAYNAME(r.dateOfApplication), count(*) AS quantity
                FROM Request as r
                GROUP BY DAYNAME(r.dateOfApplication)
                ORDER BY quantity DESC''')
        }

        [yearFilter: createYearFilter(), results: results.collect { set ->
            [day: DAYS.find { day -> day.english == set[0] }.spanish, quantity: set[1]]
        }]
    }

    def reportPerMonth() {
        List results = []

        if (request.method == 'POST') {
            def (Date firstDayOfTheYear, Date lastDayOfTheYear) = getFirstAndLastDayOfTheYear(params.int('year'))

            results = Request.executeQuery('''
                SELECT MONTH(r.dateOfApplication) AS MONTH, count(*) AS COUNT
                FROM Request AS r
                WHERE r.dateOfApplication BETWEEN :firstDayOfTheYear AND :lastDayOfTheYear
                GROUP BY MONTH(r.dateOfApplication)
                ORDER BY MONTH(r.dateOfApplication) DESC
            ''', [firstDayOfTheYear: firstDayOfTheYear, lastDayOfTheYear: lastDayOfTheYear])
        } else {
            results = Request.executeQuery('''
                SELECT MONTH(r.dateOfApplication) AS MONTH, count(*) AS COUNT
                FROM Request AS r
                GROUP BY MONTH(r.dateOfApplication)
                ORDER BY MONTH(r.dateOfApplication) DESC
            ''')
        }

        [yearFilter: createYearFilter(), results: results.collect { result ->
            [month: result[0], monthName: MONTHS[result[0]], quantity: result[1]]
        }]
    }

    private BlockWidget createBlockWidget(String school, String dateOfApplication) {
        Date date = new Date().parse('yyyy-MM-dd', dateOfApplication)
        Integer dayOfWeek = date[Calendar.DAY_OF_WEEK]

        new BlockWidget(
            blocks: requestService.getDayOfWeekBlocks(dayOfWeek, school),
            datashows: requestService.getDatashow(school, dayOfWeek),
            requests: Request.requestFromTo(date, date).list()
        )
    }

    private String getNextRequestStatus(final String status) {
        List<Map<String, String>> requestStatus = grailsApplication.config.ni.edu.uccleon.requestStatus

        Integer index = requestStatus.findIndexOf {
            it.english == status
        }

        if (index < (requestStatus.size() - 1)) {
            index = index + 1
        } else {
            index = 0
        }

        requestStatus[index].english
    }

    private SchoolsFilter createSchoolsFilter() {
        new SchoolsFilter(
            schools: grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments,
            years: requestService.getYearsOfApplications()
        )
    }

    private YearFilter createYearFilter() {
        new YearFilter (
            years: requestService.getYearsOfApplications()
        )
    }

    private def getFirstAndLastDayOfTheYear(final Integer year) {
        Date date = new Date().clearTime()
        Date firstDayOfTheYear = date.clone()
        Date lastDayOfTheYear = date.clone()

        firstDayOfTheYear.set(year: year, month: 0, date: 1)
        lastDayOfTheYear.set(year: year,  month: 11, date: 31)

        [firstDayOfTheYear, lastDayOfTheYear]
    }
}

class CloneRequestCommand {
    List<Date> dates

    static constraints = {
        dates validator: { dates ->
            Date today = new Date().clearTime()
            List<Date> dateList = dates.clone()

            // All dates must be greater than or equal to today
            Boolean stage1 = dates.every { date ->
                date >= today
            }

            // All dates must be unique
            Boolean stage2 = dates.unique() == dateList

            if (!stage1) {
                return 'some.date.its.lower.than.today'
            }

            if (!stage2) {
                return 'some.dates.are.equal'
            }
        }
    }
}

class BuildRequestCommand {
    String school
    String dateOfApplication
    def grailsApplication

    static constraints = {
        school blank: false, validator: { school, obj ->
            def session = RCH.currentRequestAttributes().getSession()
            List<String> currentUserSchools = session.user.refresh().schools as List
            Integer dayOfWeek = obj.dateOfApplication ? new Date().parse('yyyy-MM-dd', obj.dateOfApplication)[Calendar.DAY_OF_WEEK] : new Date()[Calendar.DAY_OF_WEEK]
            Map coordination = obj.grailsApplication.config.ni.edu.uccleon.data.find { it.coordination == school }

            if (!(school in currentUserSchools)) {
                return ['notValidSchool']
            }

            if (coordination.datashow[dayOfWeek -1] == null) {
                return ['notValidSchoolInDay']
            }

            true
        }

        dateOfApplication blank: false, validator: { dateOfApplication, obj ->
            Date today = new Date().clearTime()
            Date date = today.parse('yyyy-MM-dd', dateOfApplication)

            if (date < today) {
                return ['outOfRange']
            }
        }
    }
}

class BlockWidget {
    Integer blocks
    List<Integer> datashows
    List<Request> requests
}

class YearFilter {
    List<String> years
}

class SchoolsFilter {
    Map schools
    List<String> years
}
