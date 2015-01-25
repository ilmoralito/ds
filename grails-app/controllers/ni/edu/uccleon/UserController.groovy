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
    classrooms:["GET", "POST"]
  ]

  def list() {
    def users

    params.max = Math.min(params.int('max') ?: 10, 100)

  	if (request.method == "POST") {
      def query = "%${params?.query}%"

      users = User.listByRole("user").search(query).list(params)
  	} else {
      users = User.listByRole("user").list(params)
    }

    [users:users, usersCount:User.count()]
  }

  def create() {
    if (request.post) {
      def schools = params.list "schools"
      def classrooms = params.list "classrooms"

      def user = new User(
        email:params?.email,
        password:"123",
        fullName:params?.fullName,
        enabled:true
      )

      schools.each { school -> user.addToSchools school }

      classrooms.each { classroom -> user.addToClassrooms classroom }

      if (!user.save()) {
        return [user:user]
      }

      //notify new user
      sendMail {
        to params.email
        subject "Datashow"
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
    def c = [["C101":"Auditorio menor"], ["C102":"Desarrollo y proyeccion"], ["C201":"Biblioteca"]]
    def e = [["E113":"Finanzas"], ["E114":"Administracion"], ["E204":"Sala de reuniones"], ["E219":"Sala de maestros"], ["E220":"Escuela de manejo"]]
    def allCls = []

    def isAdministrative = userSchoolsOrDepartments.findAll { it in departments }

    if (!isAdministrative) {
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

    [user:user, allCls:allCls ?: cls, userSchoolsOrDepartments:userSchoolsOrDepartments]
  }

  def password() {}

  def updatePassword(updatePasswordCommand cmd) {
    if (!cmd.validate()) {
      cmd.errors.allErrors.each { log.error "[$it.field:$it.defaultMessage]" }
      redirect action:"password"
      return
    }

    def user = User.get(cmd.id)

    user.properties["password"] = cmd.npassword
    user.save()

    flash.message = "Clave actualizada"
    redirect action:"password", params:[id:cmd.id]
  }

  def login(String email, String password) {
    if (request.post) {
      def user = User.login(email, password).get()

      if (!user) {
        flash.message = "user.not.found"
      } else {
        session.user = user
        redirect controller:"request", action:"list"
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
