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
                )

                mario.addToSchools(schools[4])
                mario.addToClassrooms(classrooms[3])
                mario.addToClassrooms(classrooms[5])

                if (!mario.save()) {
                    mario.errors.allErrors.each {
                        print it
                    }
                }

                def hotch = new User(
                    email:"hotch@ucc.edu.ni",
                    password:"123",
                    role:"user",
                    fullName:"hotch roger",
                    enabled:true
                )

                hotch.addToSchools(schools[2])
                hotch.addToClassrooms(classrooms[7])
                hotch.addToClassrooms(classrooms[9])

                if (!hotch.save()) {
                    hotch.errors.allErrors.each {
                        print it
                    }
                }

                def peluso = new User(
                    email:"peluso@ucc.edu.ni",
                    password:"123",
                    role:"user",
                    fullName:"peluso roger",
                    enabled:true
                )

                peluso.addToSchools(schools[1])
                peluso.addToClassrooms(classrooms[1])
                peluso.addToClassrooms(classrooms[2])
                peluso.addToClassrooms(classrooms[3])

                if (!peluso.save()) {
                    peluso.errors.allErrors.each {
                        print it
                    }
                }
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