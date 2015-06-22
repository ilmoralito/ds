package ni.edu.uccleon

class UserController {

  def userService
  def emailConfirmationService

  static defaultAction = "login"
  static allowedMethods = [
  	list:["GET", "POST"],
  	create:["GET", "POST"],
  	show:["GET", "POST"],
    enableDisableUserAccount:"POST",
    notification:"POST",
  	delete:"GET",
  	login:["GET", "POST"],
  	updatePassword:"POST",
  	resetPassword:"GET",
    profile:["GET", "POST"],
    schoolsAndDepartments:["GET", "POST"],
    classrooms:["GET", "POST"],
    updateUserRole: "POST"
  ]

  def coordsAndRoomsFlow = {
    init {
      action {
        flow.userCoordinations = session?.user?.refresh()?.schools as List

        if (flow.userCoordinations.size() > 1) {
          coordinations()
        } else {
          flow.coordination = flow.userCoordinations[0]
          roster()
        }
      }

      on("coordinations").to "coordinations"
      on("roster").to "roster"
    }

    coordinations {
      on("confirm") {
        flow.coordination = params?.coordination
        if (flow.users) {
          flow.users.collect { it.refresh() }
        }

        def query = User.where {
          enabled == true && role == "user"
        }

        flow.users = query.list()


        if (!flow.coordination) {
          flash.message = "Selecciona una coordinacion"
          return error()
        }
      }.to "roster"
    }

    roster {
      on("back").to "coordinations"
    }

    done {
      redirect controller:"request", action:"listOfPendingApplications"
    }
  }

  def updateUserCoordination(Integer id, String coordination, Boolean state) {
    def user = User.get id

    if (state) {
      user.addToSchools coordination
    } else {
      user.removeFromSchools coordination
    }

    if (!user.save()) {
      user.errors.allErrors.each { error ->
        log.error "[$error.field: $error.defaultMessage]"
      }
    }

    render(contentType: "application/json") {
      error = user.hasErrors()
      email = user.email
      fullName = user.fullName
    }
  }

  def list() {
    def users
    def schoolsAndDepartments = grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments
    def coordinationsAndDepartments = params.list("coordinations") + params.list("departments")
    def usersFiltered = []

    if (request.method == "POST") {
      def criteria = User.createCriteria()
      users = criteria {
        if (params?.fullName) {
          like "fullName", "%$params.fullName%"
        }

        def enabled = params.list("enabled")*.toBoolean()
        if (enabled) {
          "in" "enabled", enabled
        }

        def roles = params.list("roles")
        if (roles) {
          "in" "role", roles
        }
      }
    } else {
      users = User.findAllByEnabled(true)
    }

    if (coordinationsAndDepartments) {
      users.each { user ->
        if (user.schools.any { it in coordinationsAndDepartments} ) {
           usersFiltered << user
         }
      }
    }

    def usersList = usersFiltered ?: users

    [
      users:usersList.sort { it.fullName },
      coordinations:schoolsAndDepartments.schools.sort(),
      departments:schoolsAndDepartments.departments.sort()
    ]
  }

  def create() {
    if (request.post) {
      def schools = params.list "schools"
      def classrooms = params.list "classrooms"

      def user = new User( email: params?.email, fullName: params?.fullName, role: params?.role )

      schools.each { school -> user.addToSchools school }

      classrooms.each { classroom -> user.addToClassrooms classroom }

      if (!user.save()) {
        user.errors.allErrors.each { errors ->
          log.error "[$errors.field: $errors.defaultMessage]"
        }

        return [user:user]
      }

      //notify new user
      sendMail {
        to params.email
        subject "Sobre solicitudes de datashow"
        html g.render(template:"email", model:[user:user])
      }

      flash.message = "Usuario creado y notificacion enviada"
    }
  }

  def show(Integer id) {
    def user = User.get(id)

    if (!user) { response.sendError 404 }

    if (request.get) {
      return [user:user]
    } else if (request.post) {
      user.properties = params

      if (!user.save()) {
        return [user:user]
      }

      flash.message = "data.saved"
    }
  }

  def updateUserRole() {
    def id = params?.id
    def user = User.get(id)

    if (!user) { response.sendError 404 }

    user.properties["role"] = params?.role

    user.save(flush: true)

    redirect action: "show", id: id
  }

  def enableDisableUserAccount(Integer id) {
    def user = User.get(id)

    if (!user) { response.sendError 404 }

    user.properties["enabled"] = !user.enabled

    flash.message = (!user.save()) ? "A ocurrido un error intentalo nuevamente" : "Estado de cuenta actualizado"

    redirect action:"show", id:id
  }

  def notification(Integer id) {
    def user = User.get id

    if (!user) {
      response.sendError 404
    }

    sendMail {
      to user.email
      subject "Datashow"
      html g.render(template:"email", model:[user:user])
    }

    redirect action:"show", id:id
  }

  def delete(Integer id) {
    def user = User.get(id)

    if (!user) {
      response.sendError 404
      return false
    }

    user.delete()

    flash.message = "data.deleted"
    redirect action:"list"
  }

  def profile() {
    def user = User.findByEmail(session?.user?.email)

    if (request.post) {
      user.properties["email", "fullName"] = params
      session?.user?.email = params.email

      if (!user.save()) {
        return [user:user]
      }
    }

    [user:user]
  }

  def schoolsAndDepartments() {
    def user = User.findByEmail(session?.user?.email)

    if (request.method == "POST") {
      def schools = params.list("schools")

      if (schools) {
        userService.addSchoolsAndDepartments(schools, user)
      }
    }

    [user:user]
  }

  def classrooms() {
    def user = User.findByEmail(session?.user?.email)
    def departments = grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments.departments
    def userSchoolsOrDepartments = user.schools as List
    def cls = grailsApplication.config.ni.edu.uccleon.cls
    def c = [[code:"C101", name:"Auditorio menor"], [code:"C102", name:"Desarrollo y proyeccion"], [code:"C201", name:"Biblioteca"]]
    def e = [[code:"E113", name:"Finanzas"], [code:"E114", name:"Administracion"], [code:"E204", name:"Sala de reuniones"], [code:"E219", name:"Sala de maestros"], [code:"E220", name:"Escuela de manejo"]]
    def allCls = []

    def isUserWithValidEmail = user.email.tokenize("@")

    if (isUserWithValidEmail[1] != "ucc.edu.ni") {
      allCls = cls.subMap(["C", "D", "E", "K"])

      def validC = allCls["C"].findAll { !(it in c) }
      def validE = allCls["E"].findAll { !(it in e) }

      allCls["C"] = validC
      allCls["E"] = validE
    }

    if (request.method == "POST") {
      def classrooms = params.list("classrooms")

      if (classrooms) {
        userService.addClassrooms(classrooms, user)
      }
    }

    [user:user, allCls:allCls ?: cls, userClassrooms:user?.classrooms]
  }

  def updatePassword(updatePasswordCommand cmd) {
    if (!cmd.validate()) {
      cmd.errors.allErrors.each { log.error "[$it.field:$it.defaultMessage]" }
      redirect action:"profile"
      return
    }

    def user = User.get(cmd.id)

    user.properties["password"] = cmd.npassword
    user.save()

    flash.message = "Clave actualizada"
    redirect action:"profile", params:[id:cmd.id]
  }

  def login(String email, String password) {
    if (request.post) {
      def user = User.login(email, password).get()

      if (!user) {
        flash.message = "Usuario no registrado. Verifica credenciales"
      } else {
        session.user = user
        redirect controller: "request", action: session?.user?.role == 'admin' ? 'list' : 'listOfPendingApplications'
        return false
      }
    }
  }

  def resetPassword(Integer id) {
  	def user = User.get(id)

  	if (!user) {
  		response.sendError 404
  	}

  	//TODO:generate token of 7 values
  	user.properties["password"] = "1234567"

  	if (!user.save()) {
  		flash.message = "something.when.wrong"
  		redirect action:"show", params:[id:id]
  		return false
  	}

  	flash.message = "dato.guardado"
  	redirect action:"show", params:[id:id]
  }

  def logout() {
    session.user = null
    redirect action:"login"
  }
}

class updatePasswordCommand {
	Integer id
	String password
	String npassword
	String rpassword

	static constraints = {
		password blank:false
		npassword blank:false
		rpassword blank:false, validator:{rpassword, obj ->
			return rpassword == obj.npassword
		}
	}
}
