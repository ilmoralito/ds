package ni.edu.uccleon

import grails.util.Holders

class RequestController {
  def userService
  def requestService
  def beforeInterceptor = [action: this.&checkRequestStatus, only: ["editRequestFlow" ,"delete"]]

  static defaultAction = "list"
  static allowedMethods = [
    list:["GET", "POST"],
    create:["GET", "POST"],
    edit:"GET",
    show:"GET",
    updte:"POST",
    delete:["GET"],
    updateStatus:"GET",
    requestsBySchools:["GET", "POST"],
    requestsByClassrooms:["GET", "POST"],
    requestsByUsers:["GET", "POST"],
    disponability:"POST",
    updStatus:"POST",
    activity:["GET", "POST"],
    todo:"POST",
    createRequestFromActivity:["GET", "POST"],
    report:"GET"
  ]

  def report() {
    def months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
    def results = Request.list().groupBy { it.dateOfApplication[Calendar.YEAR] } { it.dateOfApplication[Calendar.MONTH] } { it.school }.collectEntries { a ->
      [a.key, a.value.collectEntries { b->
        [months[b.key], b.value.collectEntries { c ->
          [c.key, c.value.size()]
        }]
      }]
    }

    [results:results]
  }

  private checkRequestStatus() {
    def req = Request.get(params?.id)

    if (!req) { response.sendError 404 }

    if (req.status != "pending") { response.sendError 403 }
  }

  def list() {
    def requests
    def schoolsAndDepartments = grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments
    def users = params.list("users")
    def schools = params.list("schools")
    def classrooms = params.list("classrooms")
    def types = params.list("types")
    def status = params.list("status")
    def requestFromDate = params.date("requestFromDate", "yyyy-MM-dd")
    def requestToDate = params.date("requestToDate", "yyyy-MM-dd")
    def userInstance = session?.user
    def role = userInstance?.role

    if (users || schools || classrooms || types || status || requestFromDate || requestToDate) {
      Date from = requestFromDate ?: new Date()
      Date to = requestToDate ?: new Date()
      def criteria = Request.createCriteria()

      requests = criteria {
        ge "dateOfApplication", from.clearTime()
        le "dateOfApplication", to.clearTime()

        if (users) {
          user {
            "in" "email", users
          }
        }

        if (schools) {
          "in" "school", schools
        }

        if (classrooms) {
          "in" "classroom", classrooms
        }

        if (types) {
          "in" "type", types
        }

        if (status) {
          "in" "status", status
        }
      }
    } else {
      requests = (role == "admin") ? Request.todayRequest().list() : Request.listByUser(userInstance).findAllByStatus("pending")
    }

    [
      requests:requests,
      schoolsAndDepartments:schoolsAndDepartments.schools + schoolsAndDepartments.departments,
      classrooms:requestService.mergedClassrooms(),
      users:User.findAllByRole("user")
    ]
  }

    def others() {
      def results = Request.listByUser(session?.user).findAllByStatusNotEqual("pending")
      def requests = results.groupBy { request -> request.status }

      [requests:requests]
    }

    def createRequestFlow = {
      init {
        action {
          flow.type = params?.type ?: "common"
          flow.userClassrooms = userService.transformUserClassrooms()
        }

        on("success").to "buildRequest"
      }

      buildRequest {
        on("create") { BuildRequestCommand cmd ->
          if (!cmd.validate()) {
            cmd.errors.allErrors.each { error ->
              log.error "[$error.field: $error.defaultMessage]"
            }
            flow.requestErrors = cmd

            return error()
          }

          Request req = new Request(
            dateOfApplication:cmd.dateOfApplication,
            classroom:cmd.classroom,
            school:cmd.school,
            description:cmd.description,
            type:cmd.type,
            audio:cmd.audio,
            screen:cmd.screen,
            internet:cmd.internet
          )

          [
            req:req,
            requests:Request.requestFromTo(cmd.dateOfApplication, cmd.dateOfApplication).list()
          ]
        }.to "hours"
      }

      hours {
        on("confirm") { PersistHourCommand cmd ->
          if (!cmd.validate()) {
              cmd.errors.allErrors.each { println it.defaultMessage }
              return error()
          }

          //add to current request datashow selected
          def user = User.get(session?.user?.id)
          user.addToRequests flow.req

          //add datashow number selected to current request
          flow.req.datashow = cmd.datashow

          //add hours to request
          cmd.blocks.each { block ->
              flow.req.addToHours(new Hour(block:block))
          }

          flash.message = "Solicitud guardada"
        }.to "done"

        on("delete").to "done"
      }

      done {
        redirect controller:"request", action:"list"
      }
    }

    def editRequestFlow = {
      init {
        action {
          Integer id = params.int("id")
          def req = Request.findByIdAndUser(id, session?.user)

          if (!req || req.status != "pending") { response.sendError 404 }

          [req:req, userClassrooms:userService.transformUserClassrooms()]
        }

        on("success").to "edit"
      }

      edit {
        on("confirm") { BuildRequestCommand cmd ->
          if (!cmd.validate()) {
            cmd.errors.allErrors.each { println it }
            return error()
          }

          flow.req.dateOfApplication = cmd.dateOfApplication
          flow.req.classroom = cmd.classroom
          flow.req.school = cmd.school
          flow.req.description = cmd.description
          flow.req.audio = cmd.audio
          flow.req.screen = cmd.screen
          flow.req.internet = cmd.internet

          [req:flow.req, requests:Request.requestFromTo(cmd.dateOfApplication, cmd.dateOfApplication).list()]
        }.to("hours")
      }

      hours {
        on("confirm") { PersistHourCommand cmd ->
          if (!cmd.validate()) {
            cmd.errors.allErrors.each { println it }
            return error()
          }

          //delete all previous hours in request
          def query = Hour.where {
              request == flow.req
          }

          query.deleteAll()

          //update datashow
          if (cmd.datashow != flow.req.datashow) {
            flow.req.datashow = cmd.datashow
          }

          //add new blocks
          cmd.blocks.each { block ->
            flow.req.addToHours(new Hour(block:block))
          }

          flow.req.save()
        }.to "done"

        on("cancel").to "done"
      }

      done {
        redirect controller:"request", action:"list"
      }
    }

    def multipleRequestsFlow = {
      init {
        action {
          flow.currentUser = session?.user
          flow.userClassrooms = userService.transformUserClassrooms()
          flow.userSchools = flow.currentUser?.schools as List
          flow.dates = []
          flow.requestInstances = []
          flow.type = params?.type ?: "interval"
        }

        on("success").to "interval"
      }

      interval {
        on("addDate") { AddCommand cmd ->
          if (cmd.hasErrors() || flow.dates.contains(cmd.date)) {
            cmd.errors.allErrors.each { err ->
              log.error "[$err.field: $err.defaultMessage]"
            }

            return error()
          }

          flow.dates << cmd.date
        }.to "interval"

        on("addInterval") { AddIntervalCommand cmd ->
          if (cmd.hasErrors()) {
            cmd.errors.allErrors.each { err ->
              log.error "[$err.field: $err.defaultMessage]"
            }
            return error()
          }

          flow.dates = []
          flow.dates.addAll cmd.fromDate..cmd.toDate
        }.to "interval"

        on("deleteDate") {
          def index = params.int("index")

          flow.dates.remove index
        }.to "interval"

        on("confirm") {
          flow.position = 0

          def result = requestService.getInfoToAddHours(flow.dates[flow.position])

          [requests:result.requests, datashows:result.datashows, day:result.day, blocks:result.blocks]
        }.to "create"
      }

      create {
        on("add") { BuildRequestCommand cmd ->
          if (cmd.hasErrors()) {
            cmd.errors.allErrors.each { error ->
              log.error "[$error.field: $error.defaultMessage]"
            }

            return error()
          }

          def requestInstance = new Request(
            dateOfApplication:cmd.dateOfApplication,
            classroom:cmd.classroom,
            school:cmd.school,
            datashow:cmd.datashow,
            description:cmd.description
          )

          cmd.hours.each { block ->
            requestInstance.addToHours(new Hour(block:block))
          }

          flow.currentUser.addToRequests requestInstance

          flow.requestInstances << requestInstance
        }.to "create"

        on("next") {
          if (params?.position) {
            flow.position = params?.int("position")
          } else if (flow.position < (flow.dates.size() - 1)) {
            flow.position += 1
          } else {
            flow.position = 0
          }

          def result = requestService.getInfoToAddHours(flow.dates[flow.position])

          flow.blocks = 0 //restart blocks in flow scope

          [requests:result.requests, datashows:result.datashows, day:result.day, blocks:result.blocks]
        }.to "create"

        on("cancel") {
          flow.requestInstances = []
        }.to "interval"

        on("done") {
          flow.requestInstances.each { req ->
            if (!req.save(flush:true)) {
              req.errors.allErrors.each { error ->
                log.error "[$error.field: $error.defaultMessage]"
              }
            }
          }
        }.to "end"

        on("summary"){
          def results = flow.requestInstances.groupBy { it.dateOfApplication }

          [results:results]
        }.to "summary"
      }

      summary {
        on("back").to "create"

        on("deleteRequestInstance") {
          def rDate = new Date().parse("yyyy-MM-dd", params?.rDate)
          def index = params.int("index")
          def instance = flow.results[rDate][index]

          //remove from requestInstances grouped
          flow.results[rDate] -= flow.results[rDate][index]

          //remove from all requestInstances
          flow.requestInstances -= instance

          [results:flow.results]
        }.to "summary"
      }

      end() {
        redirect action:"list"
      }
    }

    def show(Integer id) {
      def req = Request.get(id)

      if (!req) {
        response.sendError 404
      }

      [req:req]
    }

    def delete(Integer id) {
    	def req = Request.findByIdAndUser(id, session?.user)

    	if (!req) {
    		response.sendError 404
    	}

    	req.delete()

    	flash.message = "data.request.deleted"
    	redirect action:"list"
    }

    def updateStatus(Long id) {
      def req = Request.get(id)

      if (!req) { response.sendError 404 }

      if (params?.status) {
        req.status = params.status
      } else {
        if (req.status == "pending") {
          req.status = "attended"
        } else if (req.status == "attended") {
          req.status = "absent"
        } else if (req.status == "absent") {
          req.status = "canceled"
        } else {
          req.status = "pending"
        }
      }

      flash.message = req.save(validate: false) ? "Confirmado..." : req.errors.allErrors.each { println it }
      redirect action:params?.path ?: "list", params:params
    }

    def activity(String dateSelected) {
      def users = params.list("users")
      def schools = params.list("schools")
      def classrooms = params.list("classrooms")
      def types = params.list("types")
      def dateOfApplication = params.date("dateSelected", "yyyy-MM-dd") ?: new Date()
      def day = dateOfApplication[Calendar.DAY_OF_WEEK]
      def schoolsAndDepartments = grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments
      def requests

      def blocks = {
        if (day == 7) {
          grailsApplication.config.ni.edu.uccleon.saturday.blocks
        } else if (day == 1) {
          grailsApplication.config.ni.edu.uccleon.sunday.blocks
        } else {
          grailsApplication.config.ni.edu.uccleon.blocks
        }
      }

      def layout = {
        if (!session?.user) {
          "oneColumn"
        } else if (session?.user?.role == "admin") {
          "threeColumns"
        } else {
          "twoColumns"
        }
      }

      if (users || schools || classrooms || types) {
        def criteria = Request.createCriteria()

        requests = criteria {
          ge "dateOfApplication", dateOfApplication
          le "dateOfApplication", dateOfApplication

          eq "status", "pending"

          if (users) {
            user {
              "in" "email", users
            }
          }

          if (schools) {
            "in" "school", schools
          }

          if (classrooms) {
            "in" "classroom", classrooms
          }

          if (types) {
            "in" "type", types
          }
        }
      } else {
        requests = Request.requestFromTo(dateOfApplication, dateOfApplication).findAllByStatus("pending")
      }

      [
        requests:requests,
        blocks:blocks.call(),
        day:day,
        dateSelected:dateOfApplication,
        datashows:grailsApplication.config.ni.edu.uccleon.datashows,
        layout:layout.call(),
        schoolsAndDepartments:schoolsAndDepartments.schools + schoolsAndDepartments.departments,
        classrooms:requestService.mergedClassrooms(),
        users:User.findAllByRole("user")
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

      [userClassrooms:userService.transformUserClassrooms()]
    }

    //REPORTS
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

    def updStatus() {
      if (params.requests) {
        def status = {
          def s = params?._action_updStatus

          if (s == "Atendido") {
            return "attended"
          } else if (s == "Ausente") {
            return "absent"
          } else {
            return "canceled"
          }
        }

        def requests = params.list("requests")

        requests.each { request ->
          def r = Request.get(request)

          if (r) {
            r.properties["status"] = status.call()

            if (!r.save()) {
              r.errors.allErrors.each {
                  print it
              }
            }

            flash.message = "Estado actualizado"
          }
        }
      } else {
        flash.message = "Seleciona al menos una solicitud para poder continuar"
      }

      redirect action:"list"
    }

    //LIST BY
    def listBy(String type) {
      def requests

      switch(type) {
        case "user":
            requests = Request.listByUser(session?.user).list(params)
            break
      }

      [requests:requests]
    }
}

class BuildRequestCommand implements Serializable {
  Date dateOfApplication
  String classroom
  String school
  String description
  Integer datashow
  String type = "express"
  Boolean audio = false
  Boolean screen = false
  Boolean internet = false
  List hours

  static constraints = {
    importFrom Request
  }
}

class PersistHourCommand {
  Integer datashow
  List blocks

  static constraints = {
    datashow nullable:false, min:1
    blocks nullable:false
  }
}

class AddCommand {
  Date date

  static constraints = {
    date nullable:false, validator:{ date ->
      def today = new Date().clearTime()

      date >= today ? true : "AddDateCommand.date.validator"
    }
  }
}

@grails.validation.Validateable
class AddIntervalCommand implements Serializable {
  Date fromDate
  Date toDate

  static constraints = {
    fromDate nullable:false, validator:{ date ->
      def today = new Date().clearTime()

      date >= today ? true : "AddDateByIntervalCommand.fromDate.validator"
    }
    toDate nullable:false, validator:{ toDate, obj ->
      toDate > obj.fromDate ? true : "AddDateByIntervalCommand.toDate.validator"
    }
  }
}
