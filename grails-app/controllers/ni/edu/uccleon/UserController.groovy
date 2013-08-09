package ni.edu.uccleon

class UserController {

    def userService
    def emailConfirmationService

	static defaultAction = "login"
    static allowedMethods = [
    	list:["GET", "POST"],
    	create:["GET", "POST"],
    	show:["GET", "POST"],
    	delete:"GET",
    	login:["GET", "POST"],
    	updatePassword:"POST",
    	resetPassword:"GET",
        profile:["GET", "POST"]
    ]

    def list() {
    	def users
        def max = Math.min(params.int('max') ?: 3, 100)
        def offset = params.int('offset') ?: 0

    	if (request.post) {
    		users = User.listByRole("user").search("%${params?.query}%").list()
    	} else {
            users = User.listByRole("user").list()
        }

        [users:users]
    }

    def create() {
        if (request.get) {
            //[user:new User(params)]
        } else {

            if (params?.schools) {
                def schools = params.schools

                def user = new User(
                    email:params?.email,
                    password:"123",
                    fullName:params.fullName ?: params?.email,
                    enabled:true
                )

                if (!user.save()) {
                    return [user:user]
                }

                if ( schools instanceof String ) {
                    user.addToSchools(new School(name:schools))
                } else {
                    schools.each { school ->
                        user.addToSchools(new School(name:school))
                    }
                }

                flash.message = "data.saved"
            } else {
                flash.message = "you.need.to.add.at.least.one.school"
            }
        }
    }

    def show(Integer id) {
        def user = User.get(id)

        if (!user) {
            response.sendError 404
            return false
        }

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

            //update session.user email property
            session?.user?.email = params.email

            if (!user.save()) {
                return [user:user]
            }

            userService.addSchoolsAndUserClassrooms(params.schools, params.classrooms, user)
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
                redirect controller:"request"
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