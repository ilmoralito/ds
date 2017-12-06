package ni.edu.uccleon

import grails.util.Environment

class UserController {
    def userService

    static defaultAction = 'profile'
    static allowedMethods = [
        list: ['GET', 'POST'],
        create: ['GET', 'POST'],
        show: ['GET', 'POST'],
        updateUserEnabledProperty: 'GET',
        notification: 'POST',
        delete: 'GET',
        updatePassword: 'POST',
        resetPassword: 'GET',
        profile: ['GET', 'POST'],
        classrooms: ['GET', 'POST'],
        updateUserRole: 'GET',
        updateUserSchools: 'GET',
        admin: 'GET'
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
        [users: User.findAllByEnabled(true, [sort: 'fullName'])]
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
            users = userList.findAll { user ->
                user.schools.any { school ->
                    school in schools || school in departments
                }
            }
        }

        render view: 'list', model: [users: users ?: userList]
    }

    def create() {
        [
            roles: grailsApplication.config.ni.edu.uccleon.roles,
            classrooms: grailsApplication.config.ni.edu.uccleon.cls,
            schoolsAndDepartments: grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments
        ]
    }

    def save() {
        List schools = params.list('schools')
        List classrooms = params.list('classrooms')

        User user = new User(
            email: params.email,
            fullName: params.fullName,
            role: params.role,
            schools: schools,
            classrooms: classrooms
        )

        if (!user.save()) {
            flash.message = 'A ocurrido un error'

            render model: [
                user: user,
                roles: grailsApplication.config.ni.edu.uccleon.roles,
                classrooms: grailsApplication.config.ni.edu.uccleon.cls,
                schoolsAndDepartments: grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments
            ], view: 'create'

            return
        }

        if (Environment.current == Environment.PRODUCTION) {
            List<User> authorities = getAuthoritiesInSchools(schools)

            sendNotification(authorities, user)
        }

        flash.message = 'Usuario creado y notificacion enviada'
        redirect action: 'create'
    }

    def show(Integer id) {
        User user = User.get(id)
        List roles = grailsApplication.config.ni.edu.uccleon.roles
        List coordinations = grailsApplication.config.ni.edu.uccleon.data*.coordination

        if (!user) {
            response.sendError 404
        }

        [user: user, roles: roles, coordinations: coordinations]
    }

    def edit(final Long id) {
        [
            user: User.get(id) ?: null,
            roles: grailsApplication.config.ni.edu.uccleon.roles,
            classrooms: grailsApplication.config.ni.edu.uccleon.cls,
            schoolsAndDepartments: grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments
        ]
    }

    def update(final Long id) {
        User user = User.get(id)

        if (user) {
            user.with {
                email = params.email
                fullName = params.fullName
                role = params.role
            }

            userService.addSchoolsAndDepartments(params.list('schools'), user)
            userService.addClassrooms(params.list('classrooms'), user)

            if (!user.save()) {
                flash.message = 'A ocurrido un error'

                render model: [
                    user: user,
                    roles: grailsApplication.config.ni.edu.uccleon.roles,
                    classrooms: grailsApplication.config.ni.edu.uccleon.cls,
                    schoolsAndDepartments: grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments
                ], view: 'edit'

                return
            }

            flash.message = 'Usuario actualizado'
        }

        redirect action: 'edit', id: id
    }

    def updateUserRole(Integer id, String role) {
        User user = User.get id

        if (user) {
            user.properties["role"] = role
            user.save(flush: true)
        }

        render(contentType: "application/json") {
            success = user ? true : false
        }
    }

    def updateUserSchools(Integer id, String coordination, Boolean checked) {
        User user = User.get id

        if (checked) {
            user.addToSchools coordination
        } else {
            user.removeFromSchools coordination
        }

        if (user.save(flush: true)) {
            user.errors.allErrors.each {
                error -> log.error "[$error.field: $error.defaultMessage]"
            }
        }

        render(contentType: "application/json") {
            delegate.coordination = coordination
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

    def notification(Integer id) {
        def user = User.get id

        if (!user) {
            response.sendError 404
        }

        sendMail {
            to user.email
            subject "Sobre solicitudes de datashow"
            html g.render(template: "email", model : [user: user, host: getServerURL()])
        }

        flash.message = "Notificacion enviada"
        redirect action: "show", id: id
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
        User user = session?.user?.refresh()

        if (request.post) {
            user.properties["fullName"] = params

            if (!user.save()) {
                flash.message = "A ocurrido un error error. Intentalo de nuevo"
            }
        }

        [user: user]
    }

    def classrooms() {
        User user = session?.user?.refresh()

        if (request.method == "POST") {
            List<String> classrooms = params.list("classrooms")

            if (classrooms) {
                userService.addClassrooms(classrooms, user)
            }
        }

        [
            user: user,
            allCls: userService.getClassrooms(session?.user?.email)
        ]
    }

    def password() {}

    def updatePassword(updatePasswordCommand cmd) {
        if (!cmd.validate()) {
            cmd.errors.allErrors.each {
                log.error "[$it.field:$it.defaultMessage]"
            }

            redirect action: "password"
            return
        }

        User user = User.get(cmd.id)

        user.properties["password"] = cmd.npassword
        user.save()

        flash.message = "Clave actualizada"
        redirect action: "password", params: [id: cmd.id]
    }

    def resetPassword(Integer id) {
        def user = User.get(id)

        if (!user) {
          response.sendError 404
        }

        //TODO:generate token of 7 values
        user.properties["password"] = "1234567"

        if (!user.save()) {
            flash.message = "something.when.wrong"
            redirect action:"show", params:[id:id]
            return false
        }

        flash.message = "dato.guardado"
        redirect action:"show", params:[id:id]
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
                    schools: user.schools.toList(),
                    classrooms: user.classrooms.toList()
                ])
            }
        }
    }
}

class updatePasswordCommand {
    Integer id
    String password
    String npassword
    String rpassword

    static constraints = {
        password blank:false
        npassword blank:false
        rpassword blank:false, validator:{rpassword, obj ->
            return rpassword == obj.npassword
        }
    }
}
