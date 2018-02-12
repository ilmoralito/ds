package ni.edu.uccleon

import org.springframework.web.context.request.RequestContextHolder
import org.hibernate.transform.AliasToEntityMapResultTransformer
import org.hibernate.SessionFactory

class UserService {

    SessionFactory sessionFactory
    def grailsApplication

    Map getUserDataset(final Long userId) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                u.id id,
                u.full_name fullName,
                u.email email,
                u.user_status enabled,
                u.user_role role,
                GROUP_CONCAT(DISTINCT us.schools_string ORDER BY us.schools_string) schools,
                GROUP_CONCAT(DISTINCT uc.classrooms_string ORDER BY uc.classrooms_string) classrooms
            FROM
                user u
                    INNER JOIN
                user_schools us ON us.user_id = u.id
                    INNER JOIN
                user_classrooms uc on uc.user_id = u.id
            WHERE
                u.id = :userId"""
        final sqlQuery = session.createSQLQuery(query)
        final result = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong 'userId', userId

            uniqueResult()
        }

        result
    }

    def getUserList() {
        String query = """
            SELECT
                new map (u.fullName AS fullName)
            FROM
                User u
            WHERE
                u.enabled = true
            ORDER BY u.fullName"""

        User.executeQuery(query)
    }

    User find(Serializable id) {
        User.get(id)
    }

    User getCurrentUser() {
        RequestContextHolder.currentRequestAttributes().getSession().user
    }

    List<String> getCurrentUserSchools() {
        final Long userId = getCurrentUser().id
        final session = sessionFactory.currentSession
        final String query = "SELECT schools_string FROM user_schools WHERE user_id = :userId"
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            setLong 'userId', userId

            list()
        }

        results
    }

    List<Map> getUserListBySchool(final String school) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                u.id id,
                u.full_name fullName,
                GROUP_CONCAT(uc.classrooms_string ORDER BY uc.classrooms_string) classrooms
            FROM
                user u
                    INNER JOIN
                user_classrooms uc ON uc.user_id = u.id
                    INNER JOIN
                user_schools us ON us.user_id = u.id
            WHERE
                us.schools_string = :school
            GROUP BY 1
            ORDER BY 2"""
        final sqlQuery = session.createSQLQuery(query)
        final results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setString 'school', school

            list()
        }

        results
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
