package ni.edu.uccleon

import org.springframework.web.context.request.RequestContextHolder

class UserService {
  def grailsApplication

  def addSchoolsAndDepartments(schools, User user) {
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
  }

  def addClassrooms(classrooms, User user) {
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

  def addSchoolsAndUserClassrooms(def schools, def classrooms, User user) {
    addSchoolsAndDepartments(schools, user)
    addClassrooms(classrooms, user)
  }

  def transformUserClassrooms() {
    def session = RequestContextHolder.currentRequestAttributes().getSession()
    def user = session.user.refresh()
    def userClassrooms = user.classrooms
    def classrooms = grailsApplication.config.ni.edu.uccleon.cls
    def results = userClassrooms.collect { c ->
      def zone = c[0]
      def target = classrooms[zone].find { it.code == c }

      if (target?.containsKey("name")) {
        target
      } else {
        [code:target.code, name:target.code]
      }
    }

    results.sort()
  }
}
