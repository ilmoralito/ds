package ni.edu.uccleon

import org.springframework.web.context.request.RequestContextHolder as RCH
import grails.validation.ValidationException
import grails.gorm.DetachedCriteria
import static java.util.Calendar.*

class RequestController {

    RequestService requestService
    UserService userService
    AppService appService
    def grailsApplication

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
            requestService.save(command)

            flash.message = 'Solicitud creada'
            redirect action: 'activity', params: [
                date: command.dateOfApplication.format('yyyy-MM-dd'),
                datashow: command.datashow,
                blocks: command.hours
            ]
        } catch(ValidationException e) {
            render model: [
                errors: e.errors,
                school: command.school,
                dateOfApplication: command.dateOfApplication.format('yyyy-MM-dd'),
                blockWidget: createBlockWidget(command.school, command.dateOfApplication)],
            view: 'buildRequest'
        }
    }

    def listOfPendingApplications() {
        User user = session.user
        List<Request> requests = user.role in ['coordinador', 'asistente'] ?
            requestService.getRequestsBySchools(session.schools) :
            requestService.getOwnRequests(user.id)

        List dataset = requests.groupBy { it.date }.collect {
            [
                date: it.key,
                details: it.value.collect { map ->
                    [
                        id: map.id,
                        user: map.user,
                        classroom: appService.getClassroomCodeOrName(map.classroom)
                    ]
                }
            ]
        }.sort { a, b -> b.date <=> a.date }

        [dataset: dataset]
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
        List<Map> requestList = requestService.getTodayRequestList()

        [
            requestCount: requestList.size(),
            results: requestService.getRequestListGroupedByBlock(requestList),
            requestStatus: grailsApplication.config.ni.edu.uccleon.requestStatus
        ]
    }

    def filter() {
        ConfigObject config = grailsApplication.config.ni.edu.uccleon

        [
            departments: config.schoolsAndDepartments.departments.sort(),
            schools: config.schoolsAndDepartments.schools.sort(),
            classrooms: requestService.mergedClassrooms(),
            requestStatus: config.requestStatus,
            users: userService.getUserList(),
        ]
    }

    def applyFilter() {
        Date since = params.requestFromDate ? params.date('requestFromDate', 'yyyy-MM-dd') : new Date()
        Date till = params.requestToDate ? params.date('requestToDate', 'yyyy-MM-dd') : new Date()
        List<Map> requestList = requestService.getRequestListBetweenDates(since, till)

        render view: 'list', model: [
            requestCount: requestList.size(),
            results: requestService.getRequestListGroupedByBlock(requestList),
            requestStatus: grailsApplication.config.ni.edu.uccleon.requestStatus
        ]
    }

    def requestsByCoordination() {
        List<Request> requestList = Request.createCriteria().list {
            "in" 'school', userService.getCurrentUserSchools()
        }

        Map group = requestList.groupBy { it.dateOfApplication[YEAR]} { it.school } { it.dateOfApplication[MONTH] }
        List<Map> results = group.collect {
            [
                year: it.key,
                schools: it.value.collect {
                    [
                        name: it.key,
                        months: it.value.collect {
                            [
                                number: it.key,
                                name: Utility.MONTHLIST[it.key],
                                count: it.value.size()
                            ]
                        }.sort { Map a, Map b -> a.number <=> b.number }
                    ]
                }.sort { Map a, Map b -> a.name <=> b.name }
            ]
        }.sort { Map a, Map b -> b.year <=> a.year }

        [results: results]
    }

    def show(final Long id) {
        Map requestInstance = requestService.getRequestDataset(id)

        if (!requestInstance.user) response.sendError 404

        [requestInstance: requestInstance]
    }

    def edit(final Long id) {
        Request requestInstance = Request.get(id)

        if (!requestInstance) response.sendError 404

        [
            requestInstance: requestInstance,
            blockWidget: createBlockWidget(requestInstance.school, requestInstance.dateOfApplication)
        ]
    }

    def update(UpdateRequestCommand command) {
        Request requestInstance = Request.get(command.id)

        if (!requestInstance) response.sendError 404

        if (command.hasErrors()) {
            render view: 'edit', model: [
                errors: command.errors,
                requestInstance: requestInstance,
                blockWidget: createBlockWidget(requestInstance.school, requestInstance.dateOfApplication)
            ]

            return
        }

        try {
            Request object = requestService.update(command)

            flash.message = 'Solicitud editada'
            redirect action: 'activity', params: [
                date: object.dateOfApplication.format('yyyy-MM-dd'),
                datashow: object.datashow,
                blocks: object.hours
            ]
        } catch(ValidationException e) {
            render view: 'edit', model: [
                errors: e.errors,
                requestInstance: requestInstance,
                blockWidget: createBlockWidget(requestInstance.school, requestInstance.dateOfApplication)
            ]
        }
    }

    def delete(final Long id) {
        Hour.executeUpdate('DELETE Hour WHERE request.id = :id', [id: id])
        Request.executeUpdate('DELETE Request WHERE id = :id', [id: id])

        flash.message = 'Solicitud eliminada'
        redirect action: 'listOfPendingApplications'
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
            Date dateOfApplication = date ? Date.parse('yyyy-MM-dd', date) : new Date()
            List<Map> requestList = date ? requestService.getRequestListInDate(dateOfApplication) : requestService.getCurrentDateRequestList()

            Closure layout = {
                User currentUser = userService.getCurrentUser()

                !currentUser ? 'oneColumn': currentUser.role != 'admin' ? 'twoColumns' : 'threeColumns'
            }

            [
                layout: layout(),
                dateOfApplication: dateOfApplication,
                requestList: tranformToRequestList(requestList),
                datashows: grailsApplication.config.ni.edu.uccleon.datashows.size(),
            ]
        } catch(Exception e) {
            flash.message = 'Fecha no parseable. What are you trying to do? ;^)'

            redirect controller: 'request', action: 'activity'
            return
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

    def listTodayActivities() {
        render(contentType: 'application/json') {
            requestService.listTodayActivities()
        }
    }

    private BlockWidget createBlockWidget(String school, Date dateOfApplication) {
        Integer dayOfWeek = Utility.getDayOfWeek(dateOfApplication)

        new BlockWidget(
            datashows: requestService.getDatashow(school, dayOfWeek),
            blocks: requestService.getDayOfWeekBlocks(dayOfWeek, school),
            requests: Request.requestFromTo(dateOfApplication, dateOfApplication).list(fetch: [hours: 'join'])
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

    private List<Map> tranformToRequestList(final List<Map> requestList) {
        requestList.collect { Map request ->
            [
                id: request.id,
                fullName: request.fullName,
                datashow: request.datashow,
                classroom: request.classroom,
                school: request.school,
                audio: request.audio,
                cpu: request.cpu,
                internet: request.internet,
                pointer: request.pointer,
                screen: request.screen,
                description: request.description,
                blocks: request.blocks.tokenize(',')*.toInteger()
            ]
        }
    }
}

class BuildRequestCommand {

    UserService userService

    String school
    Date dateOfApplication
    def grailsApplication

    static constraints = {
        school nullable: false, blank: false, validator: { school, obj ->
            if (obj.dateOfApplication == null) {
                return false
            }

            Integer dayOfWeek = Utility.getDayOfWeek(obj.dateOfApplication)
            Map coordination = obj.grailsApplication.config.ni.edu.uccleon.data.find { it.coordination == school }

            if (!(school in RCH.requestAttributes.session.schools)) {
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

class UpdateRequestCommand extends StoreRequestCommand {
    Long id
}

class BlockWidget {
    Integer blocks
    List<Integer> datashows
    List<Request> requests
}
