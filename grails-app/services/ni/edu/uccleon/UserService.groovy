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

        getUserSchools(userId)
    }

    List<String> getUserSchools(final Serializable id) {
        final session = sessionFactory.currentSession
        final String query = "SELECT schools_string FROM user_schools WHERE user_id = :userId"
        final sqlQuery = session.createSQLQuery(query)
        final List<String> results = sqlQuery.with {
            setLong 'userId', id

            list()
        }

        results
    }

    List<String> getUserClassrooms(final Serializable id) {
        final session = sessionFactory.currentSession
        final String query = "SELECT classrooms_string FROM user_classrooms WHERE user_id = :userId"
        final sqlQuery = session.createSQLQuery(query)
        final List<String> results = sqlQuery.with {
            setLong 'userId', id

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

    User save(SaveUser command) {
        User user = new User()

        user.with {
            fullName = command.fullName
            email = command.email
            role = command.role
            schools = command.schools
            classrooms = command.classrooms

            save(failOnError: true)
        }

        user
    }

    User update(UpdateUser command) {
        User user = User.get(command.id)

        if (user) {
            deleteUserSchools(command.id)
            deleteUserClassrooms(command.id)

            user.with {
                fullName = command.fullName
                email = command.email
                role = command.role
            }

            command.schools.each { String school -> user.addToSchools(school) }

            command.classrooms.each { String school -> user.addToClassrooms(school) }

            user.save(flush: true)
        }

        user
    }

    void addClassrooms(final List<String> classrooms, final User user) {
        deleteUserClassrooms(user.id)

        classrooms.each { String classroom ->
            addUserClassroom(user.id, classroom)
        }
    }

    Number addUserClassroom(final Serializable userId, final String classroom) {
        final session = sessionFactory.currentSession
        final String query = "INSERT INTO user_classrooms (user_id, classrooms_string) VALUES (:userId, :classroom)"
        final sqlQuery = session.createSQLQuery(query)

        final Number result = sqlQuery.with {
            setLong 'userId', userId
            setString 'classroom', classroom

            executeUpdate()
        }

        result
    }

    Number deleteUserClassroom(final Serializable userId, final String classroom) {
        final session = sessionFactory.currentSession
        final String query = 'DELETE FROM user_classrooms WHERE user_id = :userId AND classrooms_string = :classroom'
        final sqlQuery = session.createSQLQuery(query)

        final Number result = sqlQuery.with {
            setLong 'userId', userId
            setString 'classroom', classroom

            executeUpdate()
        }

        result
    }

    Number deleteUserClassrooms(final Serializable userId) {
        final session = sessionFactory.currentSession
        final String query = 'DELETE FROM user_classrooms WHERE user_id = :userId'
        final sqlQuery = session.createSQLQuery(query)

        sqlQuery.setLong 'userId', userId

        sqlQuery.executeUpdate()
    }

    Number deleteUserSchools(final Serializable userId) {
        final session = sessionFactory.currentSession
        final String query = 'DELETE FROM user_schools WHERE user_id = :userId'
        final sqlQuery = session.createSQLQuery(query)

        sqlQuery.setLong 'userId', userId

        sqlQuery.executeUpdate()
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
            [code: 'C101', name: 'Auditorio menor'],
            [code: 'C102', name: 'Desarrollo y proyeccion'],
            [code: 'C201', name: 'Biblioteca']
        ]
        def e = [
            [code: 'E113', name: 'Finanzas'],
            [code: 'E114', name: 'Administracion'],
            [code: 'E204', name: 'Sala de reuniones'],
            [code: 'E219', name: 'Sala de maestros'],
            [code: 'E220', name: 'Escuela de manejo'],
            [code:  'E213', name:  'Proyecto']
        ]
        def allClassrooms = []

        def isUserWithValidEmail = userEmail.tokenize('@')

        if (isUserWithValidEmail[1] != 'ucc.edu.ni') {
            allClassrooms = classrooms.subMap(['C', 'D', 'E', 'K'])

            def validC = allClassrooms['C'].findAll { !(it in c) }
            def validE = allClassrooms['E'].findAll { !(it in e) }

            allClassrooms['C'] = validC
            allClassrooms['E'] = validE
        }

        allClassrooms ?: classrooms
    }

    List<Map> getUsersByStatus(final Boolean enabled) {
        User.executeQuery('''
            SELECT
                new map (id AS id, fullName AS fullName)
            FROM
                User u
            WHERE
                enabled = :enabled ORDER BY fullName''', [enabled: enabled])
    }
}
