package ni.edu.uccleon

import org.springframework.web.context.request.RequestContextHolder as RCH
import grails.validation.ValidationException
import grails.gorm.DetachedCriteria
import static java.util.Calendar.*

class RequestController {
    def grailsApplication
    def classroomService
    RequestService requestService
    def userService
    def appService

    static defaultAction = 'list'

    static allowedMethods = [
        todo: 'POST',
        update: 'POST',
        delete: 'DELETE',
        storeRequest: 'POST',
        list: ['GET', 'POST'],
        changeRequestsStatus: 'POST',
        requestsByUsers: ['GET', 'POST'],
        requestsBySchools: ['GET', 'POST'],
        requestsByClassrooms: ['GET', 'POST'],
        createRequestFromActivity: ['GET', 'POST'],
    ]

    def buildRequest(BuildRequestCommand command) {
        if (command.hasErrors()) {
            flash.errors = command.errors
            redirect uri: request.getHeader('referer')

            return
        }

        [
            school: command.school,
            dateOfApplication: command.dateOfApplication,
            blockWidget: createBlockWidget(command.school, command.dateOfApplication)
        ]
    }

    def storeRequest(StoreRequestCommand command) {
        if (command.hasErrors()) {
            flash.message = 'Parametros incorrectos'
            redirect uri: request.getHeader('referer')

            return
        }

        try {
            Request request = requestService.save(command)

            flash.message = 'Solicitud creada'
            redirect action: 'activity', params: [
                date: params.dateOfApplication,
                datashow: params.datashow,
                blocks: params.hours
            ]
        } catch(ValidationException e) {
            render model: [
                errors: e.errors,
                school: command.school,
                dateOfApplication: command.dateOfApplication,
                blockWidget: createBlockWidget(command.school, command.dateOfApplication)],
            view: 'buildRequest'
        }
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
                requestInstance.dateOfApplication
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
        } else if(currentUserRole in ['user', 'administrativo', 'supervisor']) {
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
        def results = Request.findAllByUser(session?.user).groupBy { it.dateOfApplication[Calendar.YEAR] } { it.status }.collectEntries { d ->
            [d.key, d.value.collectEntries { o ->
                [Utility.STATUS[o.key], o.value.size()]
            }]
        }

        results.each { key, value ->
            Utility.STATUS.each { k, v ->
                if (!(v in value.keySet())) {
                    value[v] = 0
                }
            }

            value["TOTAL"] = value*.value.sum()
        }

        [results: results.sort { -it.key }]
    }

    def userStatisticsDetail(Integer y) {
        def query = Request.where {
            user == session?.user && year(dateOfApplication) == y
        }

        def results = query.list().groupBy { it.dateOfApplication[Calendar.MONTH] } { it.status }.collectEntries { d ->
            [Utility.MONTHLIST[d.key], d.value.collectEntries { o ->
                [Utility.STATUS[o.key], o.value.size()]
            }]
        }

        results.values().each { instance ->
            Utility.STATUS.each { status ->
                if (!(status.value in instance.keySet())) {
                    instance[status.value] = 0
                }
            }
        }

        [results: results]
    }

    def list() {
        List<Request> requestList = Request.todayRequest().list()

        [
            requestCount: requestList.size(),
            results: requestService.groupRequestListByBlock(requestList),
            requestStatus: grailsApplication.config.ni.edu.uccleon.requestStatus
        ]
    }

    def filter() {
        ConfigObject config = grailsApplication.config.ni.edu.uccleon

        [
            departments: config.schoolsAndDepartments.departments.sort(),
            users: User.findAllByEnabled(true, [sort: 'fullName']),
            schools: config.schoolsAndDepartments.schools.sort(),
            classrooms: requestService.mergedClassrooms(),
            requestStatus: config.requestStatus
        ]
    }

    def applyFilter() {
        Date from = params.requestFromDate ? params.date('requestFromDate', 'yyyy-MM-dd') : new Date()
        Date to = params.requestToDate ? params.date('requestToDate', 'yyyy-MM-dd') : new Date()
        List<String> departments = params.list('departments')
        List<String> classrooms = params.list('classrooms')
        List<String> schools = params.list('schools')
        List<String> status = params.list('status')
        List<String> users = params.list('users')

        List<Request> requestList = Request.filter(users, schools, departments, classrooms, status).requestFromTo(from, to).list()

        render view: 'list', model: [
            requestCount: requestList.size(),
            results: requestService.groupRequestListByBlock(requestList),
            requestStatus: grailsApplication.config.ni.edu.uccleon.requestStatus
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

        def results = result.groupBy { it.dateOfApplication[Calendar.YEAR] } { it.school } { it.dateOfApplication[Calendar.MONTH] }.collectEntries { d ->
            [d.key, d.value.collectEntries { o ->
                [o.key, o.value.collectEntries { x ->
                    [Utility.MONTHLIST[x.key], x.value.size()]
                }]
            }]
        }

        [results: results]
    }

    def show(final Long id) {
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
        try {
            Date today = new Date()
            Date dateOfApplication = date ? today.parse('yyyy-MM-dd', date) : today

            List<Request> requestsBetweenDates = requestService.getRequestsBetweenDates(dateOfApplication, dateOfApplication)
            List<Request> requests = requestService.getRequestStatus(requestsBetweenDates)
            Integer datashows = grailsApplication.config.ni.edu.uccleon.datashows.size()

            Closure layout = {
                User currentUser = userService.getCurrentUser()

                !currentUser ? 'oneColumn': currentUser.role != 'admin' ? 'twoColumns' : 'threeColumns'
            }

            [
                dateOfApplication: dateOfApplication,
                datashows: datashows,
                requests: requests,
                layout: layout()
            ]
        } catch(Exception e) {
            flash.message = 'Fecha no parseable. What are you trying to do? ;^)'

            redirect controller: 'request', action: 'activity'
            return
        }
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

    def listTodayActivities() {
        render(contentType: 'application/json') {
            requestService.listTodayActivities()
        }
    }

    private BlockWidget createBlockWidget(String school, Date dateOfApplication) {
        Integer dayOfWeek = Utility.getDayOfWeek(dateOfApplication)

        new BlockWidget(
            blocks: requestService.getDayOfWeekBlocks(dayOfWeek, school),
            datashows: requestService.getDatashow(school, dayOfWeek),
            requests: Request.requestFromTo(dateOfApplication, dateOfApplication).list()
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
}

class BuildRequestCommand {
    String school
    Date dateOfApplication
    def grailsApplication

    static constraints = {
        school nullable: false, blank: false, validator: { school, obj ->
            if (obj.dateOfApplication == null) {
                return false
            }

            def session = RCH.currentRequestAttributes().getSession()
            Integer dayOfWeek = Utility.getDayOfWeek(obj.dateOfApplication)
            List<String> currentUserSchools = session.user.refresh().schools as List
            Map coordination = obj.grailsApplication.config.ni.edu.uccleon.data.find { it.coordination == school }

            if (!(school in currentUserSchools)) {
                return ['notValidSchool']
            }

            if (coordination.datashow[dayOfWeek -1] == null) {
                return ['notValidSchoolInDay']
            }
        }

        dateOfApplication nullable: false, validator: { dateOfApplication ->
            if (dateOfApplication < new Date().clearTime()) {
                return ['outOfRange']
            }
        }
    }
}

class StoreRequestCommand {
    Date dateOfApplication
    String classroom
    String school
    String description
    Integer datashow
    String type = 'express'
    Boolean audio
    Boolean screen
    Boolean internet
    Boolean pointer
    Boolean cpu
    String status = 'pending'
    Long user
    List<Integer> hours

    static constraints = {
        hours nullable: false, min: 1
    }
}

class BlockWidget {
    Integer blocks
    List<Integer> datashows
    List<Request> requests
}
