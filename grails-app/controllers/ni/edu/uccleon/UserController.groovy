package ni.edu.uccleon

import grails.validation.ValidationException
import grails.util.Environment

class UserController {

    UserService userService

    static defaultAction = 'profile'

    static allowedMethods = [
        save: 'POST',
        notification: 'POST',
        updatePassword: 'POST',
        profile: ['GET', 'POST'],
    ]

    def admin() {
        redirect action: "coordsAndRooms"
    }

    def coordsAndRoomsFlow = {
        init {
            action {
                flow.userCoordinations = session?.user?.refresh()?.schools as List
                flow.users =  User.where {
                    enabled == true && role in ['user', 'coordinador']
                }.list(sort: 'fullName', order: 'asc')

                if (flow.userCoordinations.size() > 1) {
                    coordinations()
                } else {
                    flow.coordination = flow.userCoordinations[0]
                    roster()
                }
            }

            on("coordinations").to "coordinations"
            on("roster").to "roster"
        }

        coordinations {
            on("confirm") {
                flow.coordination = params?.coordination
                if (flow.users) {
                    flow.users.collect { it.refresh() }
                }

                if (!flow.coordination) {
                    flash.message = "Selecciona una coordinacion"
                    return error()
                }
            }.to "roster"
        }

        roster {
            on("back").to "coordinations"
            on("classrooms") {
                def user = User.get params?.id

                if (!user) {
                    response.sendError 404
                }

                if (flow.users) {
                    flow.users.collect { it.refresh() }
                }

                [
                    user: user,
                    classrooms: userService.getClassrooms(user.email),
                    userClassrooms: user.classrooms
                ]
            }.to "classrooms"
        }

        classrooms {
            on("back").to "roster"
        }
    }

    def addingOrRemovingUserCoordinationsOrClassrooms(Integer id, String data, String flag, Boolean state) {
        User user = User.get id

        if (state) {
            if (flag == "classrooms") {
                user.addToClassrooms data
            } else {
                user.addToSchools data
            }
        } else {
            if (flag == "classrooms") {
                user.removeFromClassrooms data
            } else {
                user.removeFromSchools data
            }
        }

        if (!user.save(flush: true)) {
            user.errors.allErrors.each { error ->
                log.error "[$error.field: $error.defaultMessage]"
            }
        }

        render(contentType: "application/json") {
            error = user.hasErrors()
            email = user.email
            fullName = user.fullName
        }
    }

    def list() {
        final String query = """
            SELECT
                new map (u.id AS id, u.fullName AS fullName)
            FROM
                User u
            WHERE
                u.enabled = true
            ORDER BY u.fullName"""

        [users: User.executeQuery(query), roles: grailsApplication.config.ni.edu.uccleon.roles.sort()]
    }

    def filter() {}

    def applyFilter() {
        List<Boolean> enabled = params.list('enabled')*.toBoolean()
        List<String> departments = params.list('departments')
        List<String> schools = params.list('schools')
        List<String> roles = params.list('roles')
        List<User> users = []

        List<User> userList = User.createCriteria().list {
            if (enabled) {
                'in' 'enabled', enabled
            }

            if (roles) {
                'in' 'role', roles
            }

            order 'fullName', 'asc'
        }

        if (schools || departments) {
            users = userList.findAll { User user ->
                List<String> schoolList = userService.getUserSchools(user.id)

                schoolList.any { school ->
                    school in schools || school in departments
                }
            }
        }

        render view: 'list', model: [users: users ?: userList]
    }

    def create(Role command) {
        if (command.hasErrors()) {
            redirect action: 'list', method: 'GET'

            flash.message = 'Rol no valido'
            return
        }

        [userModel: makeUserModel(command.role)]
    }

    def save(SaveUser command) {
        if (command.hasErrors()) {
            render model: [errors: command.errors, userModel: makeUserModel(command.role)], view: 'create'

            return
        }

        try {
            User user = userService.save(command)

            if (Environment.current == Environment.PRODUCTION) {
                List<User> authorities = getAuthoritiesInSchools(command.schools)

                sendNotification(authorities, user)
            }

            flash.message = Environment.current == Environment.PRODUCTION ? 'Usuario creado y notificado' : 'Usuario creado'
            redirect action: 'create', method: 'GET', params: ['role': command.role]
        } catch(ValidationException e) {
            render model: [errors: e.errors, userModel: makeUserModel(command.role)], view: 'create'
        }
    }

    def show(Integer id) {
        Map user = userService.getUserDataset(id)

        if (!user.fullName) response.sendError 404

        [user: user]
    }

    def edit(final Long id) {
        Map user = userService.getUserDataset(id)

        if (!user.fullName) response.sendError 404

        [user: user, userModel: makeUserModel(user.role)]
    }

    def update(UpdateUser command) {
        if (command.hasErrors()) {
            render model: [
                user: userService.getUserDataset(command.id),
                userModel: makeUserModel(command.role),
                errors: command.errors
            ], view: 'edit'

            return
        }

        try {
            User user = userService.update(command)
            flash.message = 'Datos actualizados'

            redirect action: 'show', id: command.id
        } catch(ValidationException e) {
            render model: [user: userService.getUserDataset(command.id), userModel: makeUserModel(user.role)], view: 'edit'
        }
    }

    def updateUserEnabledProperty(Integer id) {
        User user = User.get(id)

        user.properties["enabled"] = !user.enabled

        String message = user.save() ? 'success' : 'error'

        render(contentType: "application/json") {
            status = message
        }
    }

    def notification(final Long id) {
        User user = User.get(id)

        if (!user) response.sendError 404

        sendMail {
            to user.email
            subject 'Sobre solicitudes de datashow'
            html g.render(template: 'email', model : [user: user, host: getServerURL()])
        }

        flash.message = 'Notificacion enviada'
        redirect action: 'show', id: id
    }

    def delete(Integer id) {
        def user = User.get(id)

        if (!user) {
            response.sendError 404
        }

        user.delete()

        flash.message = "Usuario eliminado"
        redirect action:"list"
    }

    def profile() {
        User user = session.user

        if (request.post) {
            User.executeUpdate('''
                UPDATE
                    User
                SET
                    fullName = :fullName
                WHERE
                    id = :id''',
                [fullName: params.fullName, id: user.id])

            flash.message = 'Perfil actualizado. Reiniciar sesion para verificar cambio'
        }

        [user: user, schools: session.schools ?: userService.getCurrentUserSchools()]
    }

    def classrooms() {
        User user = session?.user

        [
            user: user,
            userClassrooms: userService.getUserClassrooms(user.id),
            classroomList: userService.getClassrooms(user.email).collect {[code: it.key, classrooms: it.value]}
        ]
    }

    def addClassroom(final Long userId, final String classroom) {
        Number result = userService.addUserClassroom(userId, classroom)

        render(status: result ? 200 : 500, contentType: 'application/json')
    }

    def removeClassroom(final Long userId, final String classroom) {
        Number result = userService.deleteUserClassroom(userId, classroom)

        render(status: result ? 200 : 500, contentType: 'application/json')
    }

    def password() {}

    def updatePassword(UpdatePasswordCommand cmd) {
        if (cmd.hasErrors()) {
            render view: 'password', model: [errors: cmd.errors]

            return
        }

        User.executeUpdate('''
            UPDATE
                User
            SET
                password = :password
            WHERE
                id = :id''', [password: cmd.npassword.encodeAsSHA1(), id: cmd.id])

        flash.message = 'Clave actualizada'
        redirect action: 'password', params: [id: cmd.id]
    }

    def resetPassword(Integer id) {
        User user = User.get(id)

        if (!user) response.sendError 404

        user.with {
            password = '1234567'
            save(flush: true)
        }

        flash.message = 'Clave resumida'
        redirect action:'show', id: id
    }

    private String getServerURL() {
        grailsApplication.config.grails.serverURL
    }

    private List<User> getAuthoritiesInSchools(List<String> schoolList) {
        List<User> authorities = getAcademicAuthorities().inject([]) { accumulator, currentValue ->
            if (currentValue.schools.toList().any { school -> school in schoolList }) {
                accumulator << currentValue
            }

            accumulator
        }

        authorities
    }

    private List<User> getAcademicAuthorities() {
        User.hasAcademyAuthority.list()
    }

    private void sendNotification(final List<User> authorities, final User user) {
        authorities.each { authority ->
            sendMail {
                to authority.email
                subject 'Notificacion de datashow'
                html g.render (template: 'email', model: [
                    authority: authority.fullName,
                    fullName: user.fullName,
                    schools: userService.getUserSchools(user.id),
                    classrooms: userService.getUserClassrooms(user.id)
                ])
            }
        }
    }

    private UserModel makeUserModel(final String role) {
        ConfigObject config = grailsApplication.config.ni.edu.uccleon

        List<String> schools = {
            if (role == 'admin') {
                return ['Soporte tecnico']
            } else if (role in ['user', 'coordinador', 'asistente']) {
                return config.schoolsAndDepartments.schools
            } else if (role in ['administrativo']) {
                return config.schoolsAndDepartments.departments
            } else {
                return ['Administracion', 'Delegacion de la sede', 'Direccion academica']
            }
        }()

        Map classrooms = {
            if (role == 'user') {
                return config.cls.subMap(['B', 'C', 'D', 'K'])
            }

            return config.cls
        }()

        new UserModel (
            role: role,
            schools: schools,
            classrooms: classrooms
        )
    }
}

class Role {
    String role
    def grailsApplication

    static constraints = {
        role validator: { role, obj -> role in obj.grailsApplication.config.ni.edu.uccleon.roles }
    }
}

class UserModel {
    String role
    Map classrooms
    List<String> schools
}

class UpdatePasswordCommand {
    Long id
    String password
    String npassword
    String rpassword

    static constraints = {
        password blank:false
        npassword blank:false
        rpassword blank:false, validator:{rpassword, obj -> rpassword == obj.npassword }
    }
}

class SaveUser {
    String fullName
    String email
    String role
    List<String> schools
    List<String> classrooms

    static constraints = {
        importFrom User
    }
}

class UpdateUser extends SaveUser {
    Long id
}
