package ni.edu.uccleon

import grails.util.Holders

class RequestController {

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
    activity:["GET", "POST"]
  ]

  private checkRequestStatus() {
    def req = Request.get(params?.id)

    if (!req) { response.sendError 404 }

    if (req.status != "pending") { response.sendError 403 }
  }

  def list() {
    def requests
    def user = session?.user
    def role = user?.role

    if (params?.requestFromDate && params?.requestToDate) {
      Date from = params.date("requestFromDate") ?: new Date()
      Date to = params.date("requestToDate") ?: new Date()

      requests = Request.requestFromTo(from , to).list()
    } else {
      requests = (role == "admin") ? Request.todayRequest().list() : Request.listByUser(user).findAllByStatus("pending")
    }

    [requests:requests]
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
        }

        on("success").to "buildRequest"
      }

      buildRequest {
        on("create") { BuildRequestCommand cmd ->
          if (!cmd.validate()) {
              cmd.errors.allErrors.each { println it.defaultMessage }
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

          [req:req, requests:Request.requestFromTo(cmd.dateOfApplication, cmd.dateOfApplication).list()]
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

          [req:req]
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

    def updateStatus(Integer id) {
      def req = Request.get(id)

      if (!req) { response.sendError 404 }

      //TODO:find a better solution for this scenario
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

      flash.message = req.save() ? "Confirmado..." : "Ocurrio un error. Intentalo otravez. Si el error perciste porfavor notificalo a ST"
      redirect action:params?.path ?: "list", params:params
    }

    def activity(String dateSelected) {
      Date date = params.date("dateSelected", "yyyy-MM-dd") ?: new Date()
      def requests = Request.requestFromTo(date, date).findAllByStatus("pending")
      def day = date[Calendar.DAY_OF_WEEK]
      def blocks = {
        if (day == 7) {
          grailsApplication.config.ni.edu.uccleon.saturday.blocks
        } else if (day == 1) {
          grailsApplication.config.ni.edu.uccleon.sunday.blocks
        } else {
          grailsApplication.config.ni.edu.uccleon.blocks
        }
      }

      [requests:requests, blocks:blocks.call(), day:day]
    }

    //REPORTS
    def requestsBy(Date from, Date to, String type) {
      List results

      switch(type) {
        case "schools":
          results = (request.get) ? Request.requestsBy("school").list() : Request.requestsBy("school").requestFromTo(from, to).list()
          break
        case "classrooms":
          results = (request.get) ? Request.requestsBy("classroom").list() : Request.requestsBy("classroom").requestFromTo(from, to).list()
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
      }

      [results:results, total:getTotal(results), type:type]
    }

    def updStatus() {
      if (params.requests) {
        def status = params?._action_updStatus
        def requests = params.list("requests")

        requests.each { request ->
          def r = Request.get(request)

          if (r) {
            r.properties["status"] = status?.toLowerCase()

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

    private getTotal(List results) {
      def total = 0

      results.each {result ->
        total += result.count
      }

      total
    }

}

class updateStatusCommand {
    Integer id
    String status

    static constraints = {
      status inList:["pending", "attended", "absent"]
    }

    Request updateRequestStatus() {
      def req = Request.get(id)

      req.status = status

      //req.save()
    }
}

class BuildRequestCommand {
  Date dateOfApplication
  String classroom
  String school
  String description
  String type
  Boolean audio = false
  Boolean screen = false
  Boolean internet = false

  static constraints = {
    dateOfApplication nullable:false, validator: {val, obj ->
      def today = new Date()
      def minCommonRequestDate = today + 2
      
      if (obj.type == "express") {
        val >= today.clearTime()
      } else {
        val >= minCommonRequestDate
      }
    }

    classroom blank:false, inList:Holders.config.ni.edu.uccleon.classrooms as List
    school blank:false, inList:Holders.config.ni.edu.uccleon.schoolsAndDepartments.schools + Holders.config.ni.edu.uccleon.schoolsAndDepartments.departments
    description nullable:true
    type blank:false, inList:["common", "express"]
    audio nullable:false
    screen nullable:false
    internet nullable:false
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
