package ni.edu.uccleon

class UserService {

    def addSchoolsAndUserClassrooms(def schools, def classrooms, User user) {
        //schools
        //delete all schools for current user
        def query = School.where {
            user == user
        }

        query.deleteAll()

        schools.each { school ->
            user.addToSchools(new School(name:school))
        }

        //classrooms
        //delete all userClassrooms instances from current user
        def q = UserClassroom.where {
            user == user
        }

        q.deleteAll()

        classrooms.each { classroom ->
            user.addToUserClassrooms(new UserClassroom(classroom:classroom))
        }
    }

}