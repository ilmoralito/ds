package ni.edu.uccleon

import groovy.xml.*
import groovy.json.JsonOutput

class CommonTagLib {
    def userService
    def requestService
    def grailsApplication

    static namespace = "ds"

    def roles = { attrs ->
        List roles = grailsApplication.config.ni.edu.uccleon.roles
        User user = attrs.user
        String userRole = user.role
        MarkupBuilder builder = new MarkupBuilder(out)
        Map radioParams = [type: "radio", name: "role", "data-id": user.id]

        builder.div {
            roles.each { rol ->
                label(class: "radio") {
                    radioParams.value = rol
                    radioParams["data-role"] = rol
                    if (userRole == rol) {
                        radioParams.checked = true
                    } else {
                        radioParams.remove "checked"
                    }

                    input(radioParams)
                    builder.yield rol?.capitalize()
                }
            }
        }
    }

    def flashMessage = {attrs, body ->
        if (flash.message) {
            out << body()
        }
    }

    def isEnabled = { attrs ->
        if (attrs.status) {
            out << "Habilitado"
        } else {
            out << "Deshabilitado"
        }
    }

    def classroom = { attrs ->
        List classrooms = requestService.mergedClassrooms() as List

        out << classrooms.find { it.code == attrs.room }.name
    }

    def isAdmin = {attrs, body ->
        if (session?.user?.role == "admin") {
            out << body()
        }
    }

    def isNotAdmin = { attrs, body ->
        if (session?.user?.role != "admin") {
            out << body()
        }
    }

    def isAcademic = { attrs, body ->
        if (session?.user?.role in ["coordinador", "asistente"]) {
            out << body()
        }
    }

    def requestStatus = {attrs, body ->
        def status = attrs.status

        if (status == "pending") {
            out << "Pendiente"
        } else if (status == "attended") {
            out << "Atendido"
        } else if (status == "absent") {
            out << "Ausente"
        } else if (status == "canceled") {
            out << "Cancelado"
        }
    }

    def renderTitle = { attrs ->
        switch(attrs.title) {
            case "schools":
                out << "Facultades"
            break
            case "classrooms":
                out << "Aulas"
            break
            case "users":
                out << "Usuarios"
            break
            case "datashows":
                out << "Datashows"
            break
            case "blocks":
                out << "Bloques"
            break
        }
    }

    def blockToHour = { attrs, body ->
        def block = attrs.int("block")
        def doapp = attrs.int("doapp") //day of Application

        //sunday
        if (doapp == 1) {
            if (block == 1) {
                out << "08:00 - 09:10"
            }

            if (block == 2) {
                out << "09:10 - 10:40"
            }

            if (block == 3) {
                out << "10:40 - 12:00"
            }
        }

        //saturday
        if (doapp == 7) {
            if (block == 1) {
                out << "08:00 - 10:00"
            }

            if (block == 2) {
                out << "10:00 - 12:00"
            }

            if (block == 3) {
                out << "01:40 - 02:15"
            }

            if (block == 4) {
                out << "02:15 - 03:30"
            }
        }

        //weekday
        if (doapp >= 2 && doapp < 7) {
            if (block == 1) {
                out << "08:00 - 09:10"
            }

            if (block == 2) {
                out << "09:10 - 10:20"
            }

            if (block == 3) {
                out << "10:40 - 11:45"
            }

            if (block == 4) {
                out << "12:00 - 01:15"
            }

            if (block == 5) {
                out << "01:15 - 02:25"
            }

            if (block == 6) {
                out << "02:25 - 03:35"
            }

            if (block == 7) {
                out << "03:40 - 05:00"
            }
        }
    }

    def message = { attrs, body ->
        def req = attrs.request
        def blocks = attrs.blocks

        if (session?.user?.role == "admin") {
            out << "${req.user.fullName}, ${req.classroom}, bloques ${blocks}"
        } else {
            out << "${req.dateOfApplication.format('yyyy-MM-dd')}, ${req.classroom}, ${blocks}"
        }
    }

    def countByStatus = { attrs ->
        def result = Request.listByUser(session?.user).countByStatusNotEqual(attrs.status)

        if (result) {
            out << result
        }
    }

    def dayOfWeek = { attrs ->
        def days = ["Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"]

        out << days[attrs.int("index") - 1]
    }

    def coordinations = { attrs ->
        MarkupBuilder mb = new MarkupBuilder(out)
        String area = attrs.area
        List<String> coordinations = grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments[area]
        Map<String, String> params = [type: "checkbox", name: area]
        List<String> list = attrs.list

        mb.div {
            label area == "schools" ? "Academia" : "Administrativa"

            coordinations.each { coordination ->
                params.value = coordination

                if (coordination in list) {
                    params.checked = true
                } else {
                    params.remove("checked")
                }

                div(class: "checkbox") {
                    input(params)
                    label { mkp.yield coordination }
                }
            }
        }
    }

    def rol = { attrs ->
        MarkupBuilder mb = new MarkupBuilder(out)
        List<String> roles = grailsApplication.config.ni.edu.uccleon.roles
        Map<String, String> params = [type: "checkbox", name: "roles"]
        List<String> listOfRoles = attrs?.listOfRoles

        mb.div {
            p "Roles"

            roles.each { rol ->
                params.value = rol

                if (rol in listOfRoles) {
                    params.checked = true
                } else {
                    params.remove("checked")
                }

                div(class: "checkbox") {
                    input(params)
                    label { mkp.yield rol }
                }
            }
        }
    }

    def createRequest = {
        MarkupBuilder mb = new MarkupBuilder(out)
        User currentUser = userService.getCurrentUser()
        List<String> currentUserSchools = currentUser.schools.toList()
        Map<String, String> parameters = [:]

        if (currentUser.role in ['coordinador', 'asistente', 'administrativo']) {
            mb.form(action: g.createLink(controller: 'request', action: 'buildRequest'), autocomplete: 'off', class: 'create-request') {
                if (currentUserSchools.size() > 1) {
                    div(class: 'form-group') {
                        label(for: 'school') {
                            mkp.yield 'Coordinacion'
                        }

                        delegate.select(name: 'school', class: 'form-control input-block-level') {
                            currentUserSchools.each { school ->
                                if (school == params?.school) {
                                    parameters.selected = true
                                } else {
                                    parameters.remove('selected')
                                }

                                parameters.value = school

                                option(parameters) {
                                    mkp.yield school
                                }
                            }
                        }
                    }
                } else {
                    input(type: 'hidden', name: 'school', value: currentUserSchools[0])
                }

                div(class: 'form-group') {
                    label(for: 'dateOfApplication') {
                        mkp.yield 'Fecha de solicitud'
                    }

                    input(type: 'date', id: 'dateOfApplication', name: 'dateOfApplication', class: 'form-control input-block-level', value: params?.dateOfApplication)
                }

                input(type: 'submit', value: 'Crear solicitud', class: 'btn btn-primary btn-block')
            }
        }
    }

    def usersBySchool = { attrs ->
        MarkupBuilder mb = new MarkupBuilder(out)
        User currentUser = userService.getCurrentUser()
        List<User> users = userService.getUsersBySchool(attrs.school)
        Map<String, String> parameters = [:]

        if (currentUser.role == 'administrativo') {
            mb.input(type: 'hidden', name: '', value: '')
        } else {
            mb.div(class: 'form-group') {
                label(for: 'user') { mkp.yield 'Solicitado por' }

                delegate.select(id: 'user', name: 'user', class: 'form-control') {
                    users.each { user ->
                        if (currentUser == user) {
                            parameters.selected = true
                        } else {
                            parameters.remove('selected')
                        }

                        parameters.value = user.id
                        parameters['data-classrooms'] = JsonOutput.toJson(this.getClassrooms(user.classrooms.toList()))

                        option(parameters) {
                            mkp.yield user.fullName
                        }
                    }
                }
            }
        }
    }

    def userClassrooms = { attrs ->
        MarkupBuilder mb = new MarkupBuilder(out)
        User currentUser = userService.getCurrentUser()
        List<String> currentUserClassrooms = currentUser.classrooms.toList()

        mb.div(class: 'form-group') {
            label(for: 'classroom') {
                mkp.yield 'Aula'
            }

            delegate.select(id: 'classroom', name: 'classroom', class: 'form-group') {
                currentUserClassrooms.each { classroom ->
                    option(value: classroom, 'data-wifi': this.hasClassroomWIFI(classroom)) {
                        mkp.yield classroom
                    }
                }
            }
        }
    }

    def blockWidget = { attrs ->
        MarkupBuilder mb = new MarkupBuilder(out)
        List<Integer> datashows = attrs.blockWidget.datashows
        List<Request> requests = attrs.blockWidget.requests
        Integer blocks = attrs.blockWidget.blocks
        Map<String, String> parameters = [:]

        mb.section {
            label 'Bloques'

            table(class: 'table table-hover table-bordered') {
                thead {
                    datashows.eachWithIndex { datashow, index ->
                        if (index == 0) {
                            th(width: 1)
                        }

                        th {
                            mkp.yield datashow

                            if (this.hasHDMI(datashow)) {
                                small(style: 'font-weight: normal; font-size: .6em;') {
                                    mkp.yield 'HDMI'
                                }
                            }
                        }
                    }
                }

                tbody {
                    (0..blocks).eachWithIndex { block, index ->
                        tr {
                            td {
                                mkp.yield block + 1
                            }
                            datashows.each { datashow ->
                                if (requests.find { it.datashow ==  datashow && block in it.hours.block }) {
                                    parameters.checked = true
                                    parameters.disabled = true
                                } else {
                                    parameters.remove('checked')
                                    parameters.remove('disabled')
                                }

                                parameters.type = 'checkbox'
                                parameters.name = 'hours'
                                parameters.value = index

                                td {
                                    input(parameters)
                                }
                            }
                        }
                    }
                }

                tfoot {
                    tr {
                        datashows.eachWithIndex { datashow, index ->
                            if (index == 0) {
                                td {}
                            }

                            td {
                                input(
                                    type: 'submit',
                                    value: 'Confirmar',
                                    class: 'btn btn-small btn-primary trigger',
                                    'data-datashow': datashow)
                            }
                        }
                    }
                }
            }
        }
    }

    private Boolean hasClassroomWIFI(String classroom) {
        String letter = classroom[0]

        if (letter in ['B', 'C', 'D', 'E', 'K']) {
            Map room = grailsApplication.config.ni.edu.uccleon.cls[letter].find {
                it.code == classroom
            }

            return room.wifi ?: false
        }

        false
    }

    private List getClassrooms(List<String> classrooms) {
        def result = classrooms.collect { classroom ->
            String letter = classroom[0]

            grailsApplication.config.ni.edu.uccleon.cls[letter].find {
                it.code == classroom
            }
        }

        result
    }

    private Boolean hasHDMI(Integer datashow) {
        List cannons = grailsApplication.config.ni.edu.uccleon.datashows

        Map cannon = cannons.find { c ->
            c.code == datashow
        }

        cannon.hdmi ?: false
    }
}
