package ni.edu.uccleon

import org.springframework.web.context.request.RequestContextHolder

class UserService {
    def grailsApplication

    User find(Serializable id) {
        User.get(id)
    }

    User getCurrentUser() {
        def session = RequestContextHolder.currentRequestAttributes().getSession()

        session?.user?.refresh()
    }

    List getCurrentUserSchools() {
        def session = RequestContextHolder.currentRequestAttributes().getSession()

        session?.user?.schools as List
    }

    List<User> getUsersBySchool(String school) {
        List<User> notAdminUsers = User.where { enabled == true && role != 'admin' }.list()
        List<User> users = notAdminUsers.findAll { user ->
            school in user.schools
        }.sort { it.fullName }

        users
    }

    def addSchoolsAndDepartments(schools, User user) {
        def tmpSchools = []
        tmpSchools.addAll user.schools

        tmpSchools.each { school ->
            user.removeFromSchools(school)
        }

        schools.each { school ->
            if (!user.schools.contains(school)) {
                user.addToSchools(school)
            }
        }
    }

    def addClassrooms(classrooms, User user) {
        def tmpClassrooms = []
        tmpClassrooms.addAll user.classrooms

        tmpClassrooms.each { classroom ->
            user.removeFromClassrooms(classroom)
        }

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

    def transformUserClassrooms(List userClassrooms) {
        def classrooms = grailsApplication.config.ni.edu.uccleon.cls
        def results = userClassrooms.collect { c ->
            if (classrooms["undefined"].find { it.code == c }) {
                [code: c, name: c]
            } else {
                def target = classrooms[c[0]].find { it.code == c }
                if (target.containsKey("name")) {
                    target
                } else {
                    [code:target.code, name:target.code]
                }
            }
        }

        results.sort { it.name }
    }

    def getClassrooms(String userEmail) {
        def classrooms = grailsApplication.config.ni.edu.uccleon.cls
        def c = [
            [code:"C101", name:"Auditorio menor"],
            [code:"C102", name:"Desarrollo y proyeccion"],
            [code:"C201", name:"Biblioteca"]
        ]
        def e = [
            [code:"E113", name:"Finanzas"],
            [code:"E114", name:"Administracion"],
            [code:"E204", name:"Sala de reuniones"],
            [code:"E219", name:"Sala de maestros"],
            [code:"E220", name:"Escuela de manejo"],
            [code: "E213", name: "Proyecto"]
        ]
        def allClassrooms = []

        def isUserWithValidEmail = userEmail.tokenize("@")

        if (isUserWithValidEmail[1] != "ucc.edu.ni") {
            allClassrooms = classrooms.subMap(["C", "D", "E", "K"])

            def validC = allClassrooms["C"].findAll { !(it in c) }
            def validE = allClassrooms["E"].findAll { !(it in e) }

            allClassrooms["C"] = validC
            allClassrooms["E"] = validE
        }

        allClassrooms ?: classrooms
    }
}
