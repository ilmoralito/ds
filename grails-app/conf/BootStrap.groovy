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
        def classrooms = grailsApplication.config.ni.edu.uccleon.cls

        //users
        def mario = new User (
          email:"mario@ucc.edu.ni",
          password:"123",
          role:"admin",
          fullName:"mario roger",
          enabled:true,
          schools:[schools[4]],
          classrooms:[classrooms["B"][0]["code"], classrooms["B"][1]["code"]] //B101 and B201
        )

        mario.save(failOnError:true)

        def hotch = new User(
          email:"hotch@ucc.edu.ni",
          password:"123",
          role:"user",
          fullName:"hotch roger",
          enabled:true,
          schools:[schools[2], theDepartments[0]],
          classrooms:[classrooms["C"][14]["code"], classrooms["C"][15]["code"]] //C206:lab4, C207:lab3, C208:lab2
        )

        hotch.save(failOnError:true)

        def peluso = new User(
          email:"peluso@ucc.edu.ni",
          password:"123",
          role:"user",
          fullName:"peluso roger",
          enabled:true,
          schools:[schools[1]],
          classrooms:[classrooms["D"][0]["code"], classrooms["D"][1]["code"], classrooms["D"][2]["code"]] // D101, D102, D103
        )

        peluso.save(failOnError:true)

        def nami = new User(
          email:"nami@ucc.edu.ni",
          password:"123",
          role:"user",
          fullName:"nami roger",
          enabled:true,
          schools:[theDepartments[1]],
          classrooms:[classrooms["K"][0]["code"], classrooms["K"][1]["code"]] //K103, K104
        )

        nami.save(failOnError:true)

        //request
        //peluso
        //express
        def today = new Date().clearTime()

        def requestInstance = new Request(dateOfApplication:today, classroom:peluso.classrooms.collect{ it }[0], school:peluso.schools.collect{ it }[0], datashow:1, type:"express")
        def hour = new Hour(block:1)

        requestInstance.addToHours hour
        peluso.addToRequests requestInstance
        requestInstance.save(failOnError:true)

        def pelusoRequestInstance1 = new Request(dateOfApplication:today, classroom:peluso.classrooms.collect{ it }[1], school:peluso.schools.collect{ it }[0], datashow:5, type:"express")
        def pelusoHour1 = new Hour(block:1)

        pelusoRequestInstance1.addToHours pelusoHour1
        peluso.addToRequests pelusoRequestInstance1
        pelusoRequestInstance1.save(failOnError:true)

        def pelusoRequestInstance2 = new Request(dateOfApplication:today, classroom:peluso.classrooms.collect{ it }[2], school:peluso.schools.collect{ it }[0], datashow:3, type:"express")
        def pelusoHour2 = new Hour(block:1)

        pelusoRequestInstance2.addToHours pelusoHour2
        peluso.addToRequests pelusoRequestInstance2
        pelusoRequestInstance2.save(failOnError:true)

        //hotch
        def hotchRequestInstance1 = new Request(dateOfApplication:today, classroom:hotch.classrooms.collect{ it }[0], school:hotch.schools.collect{ it }[0], datashow:1, type:"express")
        def hourInstance = new Hour(block:2)

        hotchRequestInstance1.addToHours hourInstance
        hotch.addToRequests hotchRequestInstance1
        hotchRequestInstance1.save(failOnError:true)

        def hotchRequestInstance2 = new Request(dateOfApplication:today, classroom:hotch.classrooms.collect{ it }[0], school:hotch.schools.collect{ it }[0], datashow:2, type:"express")
        def hourInstance1 = new Hour(block:1)

        hotchRequestInstance2.addToHours hourInstance1
        hotch.addToRequests hotchRequestInstance2
        hotchRequestInstance2.save(failOnError:true)

        def hotchRequestInstance3 = new Request(dateOfApplication:today + 30, classroom:hotch.classrooms.collect{ it }[0], school:hotch.schools.collect{ it }[0], datashow:2, type:"express")
        def hourInstance2 = new Hour(block:1)

        hotchRequestInstance3.addToHours hourInstance2
        hotch.addToRequests hotchRequestInstance3
        hotchRequestInstance3.save(failOnError:true)

        assert Request.count() == 6
      break
      case Environment.PRODUCTION:
        User.findByEmail("mario.martinez@ucc.edu.ni") ?: new User(email:"mario.martinez@ucc.edu.ni", password:"ucc2013", role:"admin", fullName:"Mario Roger Daniel Martinez Morales", enabled:true).save()
      break
    }
  }
  def destroy = {
  }
}