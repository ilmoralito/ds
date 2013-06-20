import ni.edu.uccleon.*

class BootStrap {
	def grailsApplication

    def init = { servletContext ->
    	def schools = grailsApplication.config.ni.edu.uccleon.schools
    	def classrooms = grailsApplication.config.ni.edu.uccleon.classrooms

    	//users
    	def mario = new User(
    		email:"mario@ucc.edu.ni",
    		password:"123",
    		role:"admin",
    		fullName:"mario roger",
    		enabled:true
    	).save()

		def hotch = new User(
    		email:"hotch@ucc.edu.ni",
    		password:"123",
    		role:"user",
    		fullName:"hotch roger",
    		enabled:true
    	).save()

        def peluso = new User(
            email:"peliso@ucc.edu.ni",
            password:"123",
            role:"user",
            fullName:"peluso roger",
            enabled:true
        ).save()

    }
    def destroy = {
    }
}