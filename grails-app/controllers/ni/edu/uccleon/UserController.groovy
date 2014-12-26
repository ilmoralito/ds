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
        profile:["GET", "POST"]
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

            def schools = params.list("schools")
            def classrooms = params.list("classrooms")

            userService.addSchoolsAndUserClassrooms(schools, classrooms, user)
        }

        [user:user]
    }

    def password() { }

    def updatePassword(updatePasswordCommand cmd) {
        if (!cmd.validate()) {
            chain action:(params.path) ?: "password", model:[cmd:cmd], params:[id:cmd.id]
            return
        }

        def user = User.get(cmd.id)

        user.properties["password"] = cmd.npassword
        user.save()

        flash.message = "dato.guardado"
        redirect action:(params.path) ?: "password", params:[id:cmd.id]
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

    def welcome() { }
    def oops() { }
    def invalid() { }
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
