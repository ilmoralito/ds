import ni.edu.uccleon.*
import grails.util.Environment

class BootStrap {
	def grailsApplication

  def init = { servletContext ->
    switch(Environment.current) {
      case Environment.DEVELOPMENT:
        def theSchools = grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments.schools
        def theDepartments = grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments.departments
        def schools = theSchools + theDepartments
        def classrooms = grailsApplication.config.ni.edu.uccleon.classrooms

        //users
        def mario = new User(email:"mario@ucc.edu.ni", password:"123", role:"admin", fullName:"mario roger",enabled:true)

        mario.addToSchools(schools[4])
        mario.addToClassrooms(classrooms[3])
        mario.addToClassrooms(classrooms[5])

        if (!mario.save()) {
          mario.errors.allErrors.each { println it }
        }

        def hotch = new User( email:"hotch@ucc.edu.ni", password:"123", role:"user", fullName:"hotch roger", enabled:true)

        hotch.addToSchools(schools[2])
        hotch.addToClassrooms(classrooms[7])
        hotch.addToClassrooms(classrooms[9])

        if (!hotch.save()) {
          hotch.errors.allErrors.each { println it }
        }

        def peluso = new User( email:"peluso@ucc.edu.ni", password:"123", role:"user", fullName:"peluso roger", enabled:true)

        peluso.addToSchools(schools[1])
        peluso.addToClassrooms(classrooms[1])
        peluso.addToClassrooms(classrooms[2])
        peluso.addToClassrooms(classrooms[3])

        if (!peluso.save()) {
          peluso.errors.allErrors.each { println it }
        }

        //request
        //peluso
        //express
        def today = new Date().clearTime()

        def requestInstance = new Request(dateOfApplication:today, classroom:peluso.classrooms.collect{ it }[0], school:peluso.schools.collect{ it }[0], datashow:1, type:"express")
        def hour = new Hour(block:1)

        requestInstance.addToHours hour
        peluso.addToRequests requestInstance
        requestInstance.save()

        //common
        def pelusoRequestInstance = new Request(dateOfApplication:today + 5, classroom:peluso.classrooms.collect{ it }[0], school:peluso.schools.collect{ it }[0], datashow:1, type:"common")
        def pelusoHour = new Hour(block:1)

        pelusoRequestInstance.addToHours pelusoHour
        peluso.addToRequests pelusoRequestInstance
        pelusoRequestInstance.save()

        //hotch
        def hotchRequestInstance = new Request(dateOfApplication:today, classroom:hotch.classrooms.collect{ it }[0], school:hotch.schools.collect{ it }[0], datashow:1, type:"express")
        def hourInstance = new Hour(block:2)

        hotchRequestInstance.addToHours hourInstance
        hotch.addToRequests hotchRequestInstance
        hotchRequestInstance.save()

        assert 3 == Request.count()
      break
      case Environment.PRODUCTION:
        User.findByEmail("mario.martinez@ucc.edu.ni") ?: new User(email:"mario.martinez@ucc.edu.ni", password:"ucc2013", role:"admin", fullName:"Mario Roger Daniel Martinez Morales", enabled:true).save()
      break
    }
  }
  def destroy = {
  }
}