package ni.edu.uccleon

class UserService {

    def addSchoolsAndUserClassrooms(def schools, def classrooms, User user) {
        //schools
        //delete all user schools
        def tmpSchools = []
        tmpSchools.addAll user.schools

        tmpSchools.each { school ->
            user.removeFromSchools(school)
        }

        //add new user schools
        schools.each { school ->
            if (!user.schools.contains(school)) {
                user.addToSchools(school)
            }
        }

        //classrooms
        //delete all user classrooms
        def tmpClassrooms = []
        tmpClassrooms.addAll user.classrooms

        tmpClassrooms.each { classroom ->
            user.removeFromClassrooms(classroom)
        }

        //add new user classroom
        classrooms.each { classroom ->
            if (!user.classrooms.contains(classroom)) {
                user.addToClassrooms(classroom)
            }
        }
    }

}