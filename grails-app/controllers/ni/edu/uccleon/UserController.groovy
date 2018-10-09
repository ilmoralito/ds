package ni.edu.uccleon

import grails.validation.ValidationException
import ni.edu.uccleon.Helper

class UserController {

    UserService userService
    def mailService
    Helper helper

    static defaultAction = 'profile'

    static allowedMethods = [
        save: 'POST',
        notification: 'POST',
        updatePassword: 'POST',
        profile: 'GET',
        updateProfile: 'POST',
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
        [users: userService.getUsersByStatus(), roles: grailsApplication.config.ni.edu.uccleon.roles.sort()]
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

            // user instance with role user equals profesor
            if (user.role == 'user') {
                sendNotification(user)
            }

            flash.message = 'Usuario creado'
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

        if (user.role == 'user') {
            sendNotification(user)
        }

        flash.message = 'Notificacion enviada'
        redirect action: 'show', id: id
    }

    def profile() {
        [user: session.user, schools: session.schools ?: userService.getCurrentUserSchools()]
    }

    def updateProfile(final Long id, final String fullName) {
        userService.updateUserProfile(id, fullName)

        session?.user?.fullName = fullName

        render(contentType: 'application/json') {
            status = true
        }
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

    def record(final Long id) {
        [dataset: userService.getUserRecordDataset(id), yearWidget: createYearWidget(id)]
    }

    def recordsByYear(final Long id, final Integer year) {
        render view: 'record', model: [dataset: userService.getUserRecordDataset(id, year), yearWidget: createYearWidget(id)]
    }

    def recordsDetail(final Long id, final String school) {
        List<Map> dataset = userService.getUserRecordsDetail(id, school)
        List<Map> result = createRecordsDetail(dataset)

        [dataset: result, yearWidget: createYearWidget(id)]
    }

    def recordsDetailByYear(final Long id, final String school, final Integer year) {
        List<Map> dataset = userService.getUserRecordsDetail(id, school, year)
        List<Map> result = createRecordsDetail(dataset)

        render view: 'recordsDetail', model: [dataset: result, yearWidget: createYearWidget(id)]
    }

    def recordsDetailSummary(final Long id, final String school, final Integer month) {
        List<Map> dataset = userService.getUserRecordsDetailSummary(id, school, month)
        List<Map> result = getTransformedDetailSummary(dataset)

        [dataset: result]
    }

    def recordsDetailSummaryByYear(final Long id, final String school, final Integer month, final Integer year) {
        List<Map> dataset = userService.getUserRecordsDetailSummary(id, school, month, year)
        List<Map> result = getTransformedDetailSummary(dataset)

        render view: 'recordsDetailSummary', model: [dataset: result]
    }

    def removeClassroom(final Long userId, final String classroom) {
        Number result = userService.deleteUserClassroom(userId, classroom)

        render(status: result ? 200 : 500, contentType: 'application/json')
    }

    def password() {}

    def updatePassword(UpdatePasswordCommand command) {
        if (command.hasErrors()) {
            render view: 'password', model: [errors: command.errors]

            return
        }

        userService.updatePassword(command)

        flash.message = 'Clave actualizada'
        redirect action: 'password'
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

    def getUsersByStatus(final String status) {
        final Boolean enabled = status == 'active' ? true : false
        List<Map> users = userService.getUsersByStatus(enabled)

        render(contentType: 'application/json') {
            users = users
        }
    }

    def getUsersByRole(final String role) {
        List<Map> users = userService.getUsersByRole(role)

        render(contentType: 'application/json') {
            users = users
        }
    }

    private String getServerURL() {
        grailsApplication.config.grails.serverURL
    }

    private List<User> getAuthoritiesInSchools(final List<String> schoolList) {
        List<User> academyAuthorities = getAcademicAuthorities()
        List<User> authorities = academyAuthorities.inject([]) { accumulator, currentValue ->
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

    private void sendNotification(final User user) {
        final List<User> authorities = getAuthoritiesInSchools(user.schools.toList())

        authorities.each { User authority ->
            mailService.sendMail {
                to authority.email
                subject 'UCC León. Notificacion de datashow'
                html g.render (template: 'email', model: [
                    authority: authority.fullName,
                    fullName: user.fullName,
                    schools: user.schools.sort(),
                    classrooms: user.classrooms.sort()
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

    private List<Map> createRecordsDetail(List<Map> dataset) {
        List<String> months = helper.months().reverse()
        List<Map> result = (12..1).collect { final Integer month ->
            if (month in dataset.month) {
                dataset.find { Map data -> data.month == month }
            } else {
                [total: 0, canceled: 0, attended: 0, pending: 0, absent: 0, month: month, monthname: months[--month]]
            }
        }

        result
    }

    private YearWidget createYearWidget(id) {
        new YearWidget(years: userService.getUserYearsRecord(id))
    }

    private List<Map> getTransformedDetailSummary(List<Map> dataset) {
        Map group = dataset.groupBy { it.dayofmonth }
        List<Map> result = group.collect {[
            dayOfMonth: it.key,
            dataset: it.value.collect {
                [
                    id: it.id,
                    classroom: it.classroom,
                    requirements: getRequirements(it.requirements),
                    description: it.description,
                    status: it.status,
                    blocks: getBlocks(it.blocks),
                ]
            }
        ]}

        result
    }

    private String getRequirements(final String requirements) {
        List<String> tokens = requirements?.tokenize(' ')

        tokens?.join(', ')
    }

    private String getBlocks(final String blocks) {
        List<String> tokens = blocks?.tokenize(',')

        tokens?.collect { ++it.toInteger() }?.join(', ')
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

class YearWidget {
    List<String> years
}
