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

    Number updateUserProfile(final Long id, final String fullName) {
        User.executeUpdate('UPDATE User user SET user.fullName = :fullName WHERE user.id = :id', [fullName: fullName, id: id])
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

    Number updatePassword(UpdatePasswordCommand command) {
        final Long id = command.id
        final String password = command.npassword.encodeAsSHA1()

        User.executeUpdate('UPDATE User SET password = :password WHERE id = :id', [password: password, id: id])
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

    List<Map> getUsersByStatus(final Boolean enabled = true) {
        User.executeQuery('''
            SELECT
                new map (id AS id, fullName AS fullName)
            FROM
                User u
            WHERE
                enabled = :enabled ORDER BY fullName''', [enabled: enabled])
    }

    List<Map> getUsersByRole(final String role) {
        User.executeQuery('''
            SELECT
                new map (id AS id, fullName AS fullName)
            FROM
                User u
            WHERE
                role = :role ORDER BY fullName''', [role: role])
    }

    List<String> getUserYearsRecord(final Long id) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                DISTINCT YEAR(r.date_of_application) AS year
            FROM
                request r INNER JOIN user u on u.id = r.user_id
            WHERE
                u.id = :id
            ORDER BY year DESC"""
        final sqlQuery = session.createSQLQuery(query)
        final List<String> results = sqlQuery.with {
            setLong 'id', id

            list()
        }

        results
    }

    List<Map> getUserRecordDataset(final Long id) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                school,
                COUNT(r.id) count
            FROM
                request r
                    INNER JOIN
                user u ON u.id = r.user_id
            WHERE
                u.id = :id
            GROUP BY
                school
            ORDER BY count DESC"""
        final sqlQuery = session.createSQLQuery(query)
        final List<String> results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong 'id', id

            list()
        }

        results
    }

    List<Map> getUserRecordDataset(final Long id, final Integer year) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                school,
                COUNT(r.id) count
            FROM
                request r
                    INNER JOIN
                user u ON u.id = r.user_id
            WHERE
                u.id = :id
                    AND YEAR(r.date_of_application) = :year
            GROUP BY
                school
            ORDER BY count DESC"""
        final sqlQuery = session.createSQLQuery(query)
        final List<String> results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong 'id', id
            setInteger 'year', year

            list()
        }

        results
    }

    List<Map> getUserRecordsDetail(final Long id, final String school) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                MONTHNAME(r.date_of_application) monthname,
                MONTH(r.date_of_application) month,
                SUM(CASE WHEN status = 'pending' THEN 1 ELSE 0 END) pending,
                SUM(CASE WHEN status = 'attended' THEN 1 ELSE 0 END) attended,
                SUM(CASE WHEN status = 'absent' THEN 1 ELSE 0 END) absent,
                SUM(CASE WHEN status = 'canceled' THEN 1 ELSE 0 END) canceled,
                COUNT(CASE WHEN status IN ('pending', 'attended', 'absent', 'canceled') THEN 1 END) total
            FROM
                request r
                    INNER JOIN
                user u on u.id = r.user_id
            WHERE
                u.id = :id AND school = :school
            GROUP BY monthname, month
            ORDER BY 2 DESC"""
        final sqlQuery = session.createSQLQuery(query)
        final List<String> results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong 'id', id
            setString 'school', school

            list()
        }

        results
    }

    List<Map> getUserRecordsDetail(final Long id, final String school, final Integer year) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                MONTHNAME(r.date_of_application) monthname,
                MONTH(r.date_of_application) month,
                SUM(CASE WHEN status = 'pending' THEN 1 ELSE 0 END) pending,
                SUM(CASE WHEN status = 'attended' THEN 1 ELSE 0 END) attended,
                SUM(CASE WHEN status = 'absent' THEN 1 ELSE 0 END) absent,
                SUM(CASE WHEN status = 'canceled' THEN 1 ELSE 0 END) canceled,
                COUNT(CASE WHEN status IN ('pending', 'attended', 'absent', 'canceled') THEN 1 END) total
            FROM
                request r
                    INNER JOIN
                user u on u.id = r.user_id
            WHERE
                u.id = :id
                    AND r.school = :school
                        AND YEAR(r.date_of_application) = :year
            GROUP BY monthname, month
            ORDER BY 2 DESC"""
        final sqlQuery = session.createSQLQuery(query)
        final List<String> results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong 'id', id
            setInteger 'year', year
            setString 'school', school

            list()
        }

        results
    }

    List<Map> getUserRecordsDetailSummary(final Long id, final String school, final Integer month) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                r.id,
                r.classroom,
                CONCAT((CASE
                            WHEN r.audio = 1 THEN 'Parlantes '
                            ELSE ''
                        END),
                        (CASE
                            WHEN r.cpu = 1 THEN 'Computadora '
                            ELSE ''
                        END),
                        (CASE
                            WHEN r.internet = 1 THEN 'WIFI '
                            ELSE ''
                        END),
                        (CASE
                            WHEN r.pointer = 1 THEN 'Puntero'
                            ELSE ''
                        END),
                        (CASE
                            WHEN r.screen = 1 THEN 'Pantalla'
                            ELSE ''
                        END)) requirements,
                r.description,
                (CASE
                    WHEN r.status = 'pending' THEN 'Pendiente'
                    ELSE (CASE
                        WHEN r.status = 'attended' THEN 'Atendido'
                        ELSE (CASE
                            WHEN r.status = 'absent' THEN 'Sin retirar'
                            ELSE (CASE
                                WHEN r.status = 'canceled' THEN 'Cancelado'
                                ELSE ''
                            END)
                        END)
                    END)
                END) status,
                DAYOFMONTH(r.date_of_application) dayofmonth,
                GROUP_CONCAT(DISTINCT h.block
                    ORDER BY h.block) blocks
            FROM
                request r
                    INNER JOIN
                hour h ON r.id = h.request_id
                    INNER JOIN
                user u ON u.id = r.user_id
            WHERE
                u.id = :id
                    AND r.school = :school
                        AND month(r.date_of_application) = :month
            GROUP BY 1
            ORDER BY dayofmonth DESC"""
        final sqlQuery = session.createSQLQuery(query)
        final List<String> results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong 'id', id
            setString 'school', school
            setInteger 'month', month

            list()
        }

        results
    }

    List<Map> getUserRecordsDetailSummary(final Long id, final String school, final Integer month, final Integer year) {
        final session = sessionFactory.currentSession
        final String query = """
            SELECT
                r.id,
                r.classroom,
                CONCAT((CASE
                            WHEN r.audio = 1 THEN 'Parlantes '
                            ELSE ''
                        END),
                        (CASE
                            WHEN r.cpu = 1 THEN 'Computadora '
                            ELSE ''
                        END),
                        (CASE
                            WHEN r.internet = 1 THEN 'WIFI '
                            ELSE ''
                        END),
                        (CASE
                            WHEN r.pointer = 1 THEN 'Puntero'
                            ELSE ''
                        END),
                        (CASE
                            WHEN r.screen = 1 THEN 'Pantalla'
                            ELSE ''
                        END)) requirements,
                r.description,
                (CASE
                    WHEN r.status = 'pending' THEN 'Pendiente'
                    ELSE (CASE
                        WHEN r.status = 'attended' THEN 'Atendido'
                        ELSE (CASE
                            WHEN r.status = 'absent' THEN 'Sin retirar'
                            ELSE (CASE
                                WHEN r.status = 'canceled' THEN 'Cancelado'
                                ELSE ''
                            END)
                        END)
                    END)
                END) status,
                DAYOFMONTH(r.date_of_application) dayofmonth,
                GROUP_CONCAT(DISTINCT h.block
                    ORDER BY h.block) blocks
            FROM
                request r
                    INNER JOIN
                hour h ON r.id = h.request_id
                    INNER JOIN
                user u ON u.id = r.user_id
            WHERE
                u.id = :id
                    AND r.school = :school
                        AND month(r.date_of_application) = :month
                            AND YEAR(r.date_of_application) = :year
            GROUP BY 1
            ORDER BY dayofmonth DESC"""
        final sqlQuery = session.createSQLQuery(query)
        final List<String> results = sqlQuery.with {
            resultTransformer = AliasToEntityMapResultTransformer.INSTANCE

            setLong 'id', id
            setString 'school', school
            setInteger 'month', month
            setInteger 'year', year

            list()
        }

        results
    }
}
