package ni.edu.uccleon

class UserService {

    def addSchoolsAndUserClassrooms(Map params, User user) {
    	//schools
        //delete all schools from user
        def query = School.where {
            user == user
        }

		query.deleteAll()

        //get all params with schools in its name
        def schools = params.findAll {key, value ->
            key.indexOf("schools") != -1
        }

        // iterate schools and get value equal on
        schools.each {key, value ->
            if (value == "on") {
            	def school = key.tokenize(".")[1]
             	user.addToSchools(new School(name:school))
            }
        }

        //userClassrooms
        //delete all userClassrooms from user
        def q = UserClassroom.where {
            user == user
        }

        q.deleteAll()

        //get all params with userClassrooms in its name
        def userClassrooms = params.findAll {key, value ->
            key.indexOf("userClassrooms") != -1
        }

        // iterate userClassrooms and get value equal on
        userClassrooms.each {key, value ->
            if (value == "on") {
                def userClassroom = key.tokenize(".")[1]
                user.addToUserClassrooms(new UserClassroom(classroom:userClassroom))
            }
        }
    }
}