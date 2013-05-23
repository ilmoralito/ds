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

		//user links to schools
		hotch.addToSchools(new School(name:schools[0]))//FIA
		hotch.addToSchools(new School(name:schools[1]))//FCA

        peluso.addToSchools(new School(name:schools[1]))
        peluso.addToSchools(new School(name:schools[2]))
        peluso.addToSchools(new School(name:schools[3]))

        //user links to userClassrooms
        hotch.addToUserClassrooms(new UserClassroom(classroom:classrooms[5]))
        hotch.addToUserClassrooms(new UserClassroom(classroom:classrooms[1]))

		// request
        def r0 = new Request(
            dateOfApplication:new Date() + 10,
            classroom:classrooms[3],
            school:hotch.schools[2].toString(),//FIA
            enabled:true
        )

		def r1 = new Request(
			dateOfApplication:new Date() + 4,
			classroom:classrooms[0],
			school:hotch.schools[0].toString(),//FIA
            enabled:true
		)

        def r2 = new Request(
            dateOfApplication:new Date() + 6,
            classroom:classrooms[2],
            school:peluso.schools[2].toString(),
            enabled:true
        )

		//user links to request
        hotch.addToRequests(r0)
		hotch.addToRequests(r1)
        peluso.addToRequests(r2)
    }
    def destroy = {
    }
}