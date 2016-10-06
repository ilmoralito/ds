package ni.edu.uccleon

import static java.util.Calendar.*
import grails.gorm.DetachedCriteria

class RequestController {
    def userService
    def requestService
    def classroomService

    def beforeInterceptor = [action: this.&checkRequestStatus, only: ["editRequestFlow" ,"delete"]]

    static defaultAction = "list"
    static allowedMethods = [
        buildRequest: 'GET',
        storeRequest: 'POST',
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
        listOfPendingApplications: 'GET'
    ]

    private static final MONTHS = [
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    ]

    private getRequestStatus() {
        [
            pending: "Pendiente",
            attended: "Atendido",
            absent: "Sin retirar",
            canceled: "Cancelado"
        ]
    }

    def buildRequest(BuildRequestCommand command) {
        if (command.hasErrors()) {
            command.errors.allErrors.each { error ->
                log.error "[field: $error.field, message: $error.defaultMessage]"
            }

            flash.message = "Parametros incorrectos"
            redirect uri: request.getHeader('referer')
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
            internet: params.boolean('internet')
        )

        params.list('hours').each { block ->
            newRequest.addToHours(new Hour(block: block))
        }

        User user = User.get(params.int('user'))

        user.addToRequests(newRequest)

        if (!newRequest.save()) {
            newRequest.errors.allErrors.each { error ->
                log.error "[field: $error.field, message: $error.defaultMessage]"
            }

            flash.message = 'Datos incorrectos'
            redirect uri: request.getHeader('referer')
            return
        }

        flash.message = 'Solicitud creada correctamente'
        redirect action: 'activity', params: [date: params.dateOfApplication]
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
                        classroom: b.classroom,
                        userFullName: b.user.fullName
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
        def results = Request.findAllByUser(session?.user).groupBy { it.dateOfApplication[Calendar.YEAR] } { it.status }.collectEn[Cal]tries { d ->
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

    def report() {
        Date date = new Date()
        List months = MONTHS
        List<Request> requests = Request.list()
        List data = requests.groupBy { it.dateOfApplication[YEAR] } { it.dateOfApplication[MONTH] } { it.school }.collect { o ->
            [
                year: o.key,
                months: o.value.collect { t ->
                    [
                        month: MONTHS[t.key],
                        coordinations: t.value.collect { c ->
                            [
                                coordination: c.key,
                                size: c.value.size()
                            ]
                        }
                    ]
                }
            ]
        }

        [data: data.sort { -it.year }]
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
                pending: o?.value?.pending?.size(),
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

    private checkRequestStatus() {
        def req = Request.get(params?.id)

        if (!req) { response.sendError 404 }

        if (req.status != "pending") { response.sendError 403 }
    }

    def list() {
        def requests
        def users = params.list("users")
        def schools = params.list("schools")
        def departments = params.list("departments")
        def classrooms = params.list("classrooms")
        def types = params.list("types")
        def status = params.list("status")
        def requestFromDate = params.date("requestFromDate", "yyyy-MM-dd")
        def requestToDate = params.date("requestToDate", "yyyy-MM-dd")
        def userInstance = session?.user
        def role = userInstance?.role
        def schoolsAndDepartments = grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments
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
            requestsByBlock:requestsByBlock.findAll { it.requests },
            schoolsAndDepartments:schoolsAndDepartments,
            classrooms:requestService.mergedClassrooms(),
            users:User.findAllByRoleAndEnabled("user", true, [sort:"fullName"])
        ]
    }

    def others() {
        def results = Request.listByUser(session?.user).findAllByStatusNotEqual("pending")
        def requests = results.groupBy { request -> request.status }

        [requests:requests]
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
        Request requestInstance = Request.get(params.id)

        if (!requestInstance) {
            response.sendError 404
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
        flash.message = requestInstance.save() ? 'Cambio de estado aplicado' : 'A ocurrido un error'

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
        case "schools":
          results = (request.get) ? Request.requestsBy("school").list() : Request.requestsBy("school").requestFromTo(from, to).list()
          break
        case "classrooms":
          results = Request.list().groupBy({ it.dateOfApplication[Calendar.YEAR]}, { it.classroom }).collectEntries { d ->
            [d.key, d.value.collectEntries { o ->
              [o.key, o.value.size()]
            }]
          }
          break
        case "users":
          results = (request.get) ? Request.requestsBy("user").listByRole("user").list() : Request.requestsBy("user").listByRole("user").requestFromTo(from, to).list()
          break
        case "datashows":
          results = (request.get) ? Request.requestsBy("datashow").list() : Request.requestsBy("datashow").requestFromTo(from, to).list()
          break
        case "blocks":
          results = (request.get) ? Request.requestsByBlocks().list() : Request.requestsByBlocks().requestFromTo(from, to).list()
          break
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
        case "day":
          def quantityByYear = Request.list().groupBy { it.dateOfApplication[Calendar.YEAR] }.collectEntries { [it.key, it.value.size()] }

          results = Request.list().groupBy { it.dateOfApplication[Calendar.YEAR] } { it.dateOfApplication[Calendar.DAY_OF_WEEK] }.collectEntries {
            [it.key, it.value.collectEntries { d ->
              [d.key, ["size":d.value.size(), "percent":(d.value.size() / quantityByYear[it.key]) * 100]]
            }]
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

        redirect action:"list"
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
}

class BuildRequestCommand {
    def userService

    String school
    String dateOfApplication

    static constraints = {
        school blank: false, validator: { school, obj ->
            school in obj.userService.getCurrentUserSchools()
        }
        dateOfApplication blank: false, validator: { dateOfApplication ->
            Date date = new Date().parse('yyyy-MM-dd', dateOfApplication)

            date >= new Date().clearTime()
        }
    }
}

class BlockWidget {
    Integer blocks
    List<Integer> datashows
    List<Request> requests
}
