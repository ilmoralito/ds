import ni.edu.uccleon.*
import grails.util.Environment

class BootStrap {
	def grailsApplication

    def init = { servletContext ->
        switch(Environment.current) {
            case Environment.DEVELOPMENT:
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
            break
            case Environment.PRODUCTION:
                def user = User.findByEmail("mario.martinez@ucc.edu.ni")

                if (!user) {
                    new User(
                        email:"mario.martinez@ucc.edu.ni",
                        password:"ucc2013",
                        role:"admin",
                        fullName:"Mario Roger Daniel Martinez Morales",
                        enabled:true
                    ).save()
                }
            break
        }
    }
    def destroy = {
    }
}