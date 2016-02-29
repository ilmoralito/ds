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
          fullName:"Hotch Martinez",
          enabled:true,
          schools:[theSchools[7], theSchools[2]],
          classrooms:[classrooms["C"][14]["code"], classrooms["C"][15]["code"]] //C206:lab4, C207:lab3, C208:lab2
        )

        hotch.save(failOnError:true)

        def bonita = new User(
          email:"bonita@ucc.edu.ni",
          password:"123",
          role:"user",
          fullName:"Bonita Martinez",
          enabled:true,
          schools:[theSchools[6]],
          classrooms:[classrooms["C"][14]["code"], classrooms["C"][15]["code"]] //C206:lab4, C207:lab3, C208:lab2
        )

        bonita.save(failOnError: true)

        def peluso = new User(
          email:"peluso@yahoo.com",
          password:"123",
          role:"user",
          fullName:"Peluso Martinez",
          enabled:true,
          schools:[schools[1], theSchools[7], schools[7]],
          classrooms:[classrooms["D"][0]["code"], classrooms["D"][1]["code"], classrooms["D"][2]["code"]] // D101, D102, D103
        )

        peluso.save(failOnError:true)

        def nami = new User(
          email:"nami@ucc.edu.ni",
          password:"123",
          role:"asistente",
          fullName:"Nami Martinez",
          enabled:true,
          schools: [theDepartments[0]],
          classrooms:[classrooms["K"][0]["code"], classrooms["K"][1]["code"]] //K103, K104
        )

        nami.save(failOnError:true)

        def ready = new User(
          email: "ready@ucc.edu.ni",
          password: "123",
          role: "administrativo",
          fullName: "Ready Martinez",
          schools: [theDepartments[0]],
          classrooms: [classrooms["B"][0]["code"], classrooms["B"][1]["code"], classrooms["B"][2]["code"]]
        )

        ready.save(failOnError: true)

        def coco = new User(
          email: "coco@ucc.edu.ni",
          password: "123",
          role: "administrativo",
          fullName: "Coco Martinez",
          schools: [theDepartments[5]],
          classrooms: [classrooms["B"][0]["code"], classrooms["B"][1]["code"], classrooms["B"][2]["code"]]
        )

        coco.save(failOnError: true)

        def bonita2 = new User(
          email: "bonita2@ucc.edu.ni",
          password: "123",
          role: "coordinador",
          fullName: "Bonita2 Martinez",
          schools: [theSchools[7]],
          classrooms: [classrooms["B"][0]["code"], classrooms["B"][1]["code"], classrooms["B"][2]["code"]]
        ).save failOnError: true

        //request
        //peluso
        def today = new Date().clearTime()

        def requestInstance = new Request(dateOfApplication:today, classroom:peluso.classrooms.collect{ it }[0], school:peluso.schools.collect{ it }[0], datashow:1, type:"express")
        def hour = new Hour(1)

        requestInstance.addToHours hour
        peluso.addToRequests requestInstance
        requestInstance.save(failOnError:true)

        def requestInstance0 = new Request(
          dateOfApplication:today + 3,
          classroom:peluso.classrooms.collect{ it }[0],
          school:peluso.schools.collect{ it }[0],
          datashow:1,
          type:"common"
        )

        requestInstance0.addToHours new Hour(0)
        peluso.addToRequests requestInstance0
        requestInstance0.save(failOnError:true)

        def r = new Request(
          dateOfApplication:today + 3,
          classroom:peluso.classrooms.collect{ it }[0],
          school:peluso.schools.collect{ it }[0],
          datashow:1,
          type:"express"
        )

        r.addToHours new Hour(1)
        peluso.addToRequests r
        r.save(failOnError:true)

        def pelusoRequestInstance1 = new Request(dateOfApplication:today, classroom:peluso.classrooms.collect{ it }[1], school:peluso.schools.collect{ it }[0], datashow:5, type:"express")
        def pelusoHour1 = new Hour(1)

        pelusoRequestInstance1.addToHours pelusoHour1
        peluso.addToRequests pelusoRequestInstance1
        pelusoRequestInstance1.save(failOnError:true)

        def pelusoRequestInstance2 = new Request(dateOfApplication:today, classroom:peluso.classrooms.collect{ it }[2], school:peluso.schools.collect{ it }[0], datashow:3, type:"express")
        def pelusoHour2 = new Hour(1)

        pelusoRequestInstance2.addToHours pelusoHour2
        peluso.addToRequests pelusoRequestInstance2
        pelusoRequestInstance2.save(failOnError:true)

        //hotch
        def hotchRequestInstance1 = new Request (
          dateOfApplication: today,
          classroom: hotch.classrooms.collect{ it }[0],
          school: hotch.schools.collect{ it }[0],
          datashow: 10,
          audio: true,
          description: "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorem a aut ex beatae, aspernatur sapiente minus labore voluptatem nobis molestias inventore enim iure saepe minima tempore totam aperiam hic impedit."
        )

        hotchRequestInstance1
          .addToHours(new Hour(0))
          .addToHours(new Hour(1))
          .addToHours(new Hour(2))

        hotch.addToRequests hotchRequestInstance1
        hotchRequestInstance1.save failOnError: true

        def hotchRequestInstance2 = new Request(dateOfApplication:today, classroom:hotch.classrooms.collect{ it }[0], school:hotch.schools.collect{ it }[0], datashow:2, type:"express")
        def hourInstance1 = new Hour(1)

        hotchRequestInstance2.addToHours hourInstance1
        hotch.addToRequests hotchRequestInstance2
        hotchRequestInstance2.save(failOnError:true)

        def hotchRequestInstance3 = new Request(dateOfApplication:today + 30, classroom:hotch.classrooms.collect{ it }[0], school:hotch.schools.collect{ it }[0], datashow:2, type:"express")
        def hourInstance2 = new Hour(1)

        hotchRequestInstance3.addToHours hourInstance2
        hotch.addToRequests hotchRequestInstance3
        hotchRequestInstance3.save(failOnError:true)

        assert Request.count() == 8
      break
      case Environment.PRODUCTION:
        if (!User.findByEmail(System.env.GMAIL_USERNAME)) {
          new User (
            email: System.env.GMAIL_USERNAME,
            password: System.env.DEFAULT_ADMIN_PASSWORD,
            role: "admin",
            fullName: "Mario Roger Martinez Morales",
            enabled: true,
            schools: ["Soporte tecnico"],
            classrooms: ["B201"]
          ).save(failOnError: true)
        }
      break
    }
  }
  def destroy = {
  }
}