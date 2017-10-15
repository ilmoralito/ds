package ni.edu.uccleon

import groovy.xml.*
import groovy.json.JsonOutput
import static java.util.Calendar.*

class CommonTagLib {
    def userService
    def requestService
    def grailsApplication
    def appService

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
        final String classroomCode = attrs.classroomCode

        out << appService.getClassroomCodeOrName(classroomCode)
    }

    def isAdmin = {attrs, body ->
        User currentUser = userService.getCurrentUser()

        if (currentUser.role == "admin") {
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
        Map<String, String> params = [type: 'checkbox', name: area]
        List<String> list = attrs.list

        mb.div {
            label(style: 'margin: 10px 0;') {
                mkp area == 'schools' ? 'Academia' : 'Administrativa'
            }

            coordinations.each { coordination ->
                params.value = coordination

                if (coordination in list) {
                    params.checked = true
                } else {
                    params.remove('checked')
                }

                div(class: 'checkbox') {
                    input(params)
                    label { mkp.yield coordination }
                }
            }
        }
    }

    def rol = { attrs ->
        MarkupBuilder mb = new MarkupBuilder(out)
        List<String> roles = grailsApplication.config.ni.edu.uccleon.roles
        Map<String, String> params = [type: 'checkbox', name: 'roles']
        List<String> listOfRoles = attrs?.listOfRoles

        mb.div {
            label(style: 'margin: 10px 0;') { mkp 'Roles' }

            roles.each { rol ->
                params.value = rol

                if (rol in listOfRoles) {
                    params.checked = true
                } else {
                    params.remove('checked')
                }

                label(class: 'checkbox') {
                    input(params)
                    label { mkp.yield rol.capitalize() }
                }
            }
        }
    }

    def createRequest = {
        Map<String, String> parameters = [:]
        MarkupBuilder mb = new MarkupBuilder(out)
        User currentUser = session?.user?.refresh()
        List<String> currentUserSchools = currentUser.schools as List

        if (currentUser.role in ['coordinador', 'asistente', 'administrativo', 'supervisor']) {
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
        Map<String, String> parameters = [:]
        MarkupBuilder mb = new MarkupBuilder(out)
        User currentUser = userService.getCurrentUser()
        List<User> userList = userService.getUsersBySchool(attrs.school)

        if (userList.size() == 1) {
            mb.input(type: 'hidden', name: 'user', value: userList[0].id)
        } else {
            mb.div(class: 'form-group') {
                label(for: 'user') {
                    mkp.yield 'Solicitado por'
                }

                delegate.select(id: 'user', name: 'user', class: 'form-control') {
                    userList.each { user ->
                        if (currentUser == user) {
                            parameters.selected = true
                        } else {
                            parameters.remove('selected')
                        }

                        parameters.value = user.id
                        parameters['data-classrooms'] = JsonOutput.toJson(this.getClassrooms(user.classrooms.toList().sort()))

                        option(parameters) {
                            mkp.yield user.fullName
                        }
                    }
                }
            }
        }
    }

    def userClassrooms = { attrs ->
        Map<String, String> parameters = [:]
        MarkupBuilder mb = new MarkupBuilder(out)
        User currentUser = userService.getCurrentUser()
        String selectedClassroom = attrs.selectedClassroom
        List<String> currentUserClassrooms = currentUser.classrooms.toList().sort()

        mb.div(class: 'form-group') {
            label(for: 'classroom') {
                mkp.yield 'Aula'
            }

            delegate.select(id: 'classroom', name: 'classroom', class: 'form-group') {
                currentUserClassrooms.each { classroom ->
                    parameters.value = classroom
                    parameters['data-wifi'] = this.hasClassroomWIFI(classroom)

                    if (classroom == selectedClassroom) {
                        parameters.selected = true
                    } else {
                        parameters.remove('selected')
                    }

                    option(parameters) {
                        mkp.yield appService.getClassroomCodeOrName(classroom)
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
                            th(width: 40)
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
                            td(style: 'text-align: center;') {
                                if (index == 3 && blocks > 3) {
                                    p(style: 'font-size: 0.6em; margin: 0; padding: 0;') {
                                        mkp 'Medio dia'
                                    }
                                } else {
                                    mkp.yield block + 1
                                }
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

    def activitiesTable = { attrs ->
        MarkupBuilder mb = new MarkupBuilder(out)
        Date dateOfApplication = attrs.dateOfApplication
        List<Request> requests = attrs.requests
        Integer datashows = attrs.datashows
        User currentUser = userService.getCurrentUser()
        List requestStatus = grailsApplication.config.ni.edu.uccleon.requestStatus.findAll { it.english != 'pending' }

        mb.section {
            p {
                span(id: 'activity-count') {
                    mkp.yield requests.size()
                }

                mkp.yield " actividades el ${g.formatDate(format: 'yyyy-MM-dd', date: dateOfApplication)}"
            }

            table(id: 'activity-table', class: 'table table-bordered fixed') {
                thead {
                    (1..datashows).eachWithIndex { datashow, index ->
                        if (index == 0) {
                            th(width: 15) {}
                        }

                        th(style: 'font-weight: normal; text-align: center;') {
                            mkp.yield datashow
                        }
                    }
                }

                tbody {
                    (0..6).eachWithIndex { block, idx ->
                        tr {
                            td(style: 'vertical-align: middle; text-align: center;') {
                                if (idx == 3) {
                                    i(class: 'fa fa-sun-o', 'aria-hidden': true)
                                } else {
                                    mkp.yield block + 1
                                }
                            }

                            (1..datashows).each { datashow ->
                                Request request = requests.find {
                                    it.datashow == datashow && block in it.hours.block
                                }

                                if (request) {
                                    List<Hour> hours = request.hours.sort { it.block }
                                    Integer index = hours.findIndexOf { hour -> hour.block == block }

                                    td(class: "block hasActivity animated ${animate(datashow, block, params.int('datashow'), params.list('blocks')*.toInteger())}", 'data-datashow': datashow, 'data-block': block) {
                                        if (index == 0) {
                                            if (attrs.layout == 'oneColumn') {
                                                if (request.audio || request.screen || request.internet || request.pointer || request.cpu || request.description) {
                                                    List<String> props = ['audio', 'screen', 'internet', 'pointer', 'cpu', 'description']
                                                    Map<String, Object> properties = request.properties.subMap(props).findAll { it.value }.inject([:]) { accumulator, currentValue ->
                                                        accumulator["data-${currentValue.key}"] = currentValue.value

                                                        accumulator
                                                    }

                                                    a(href: '#', class: 'show-modal', *:properties) {
                                                        mkp.yield '+'
                                                    }
                                                }
                                            }

                                            if (currentUser?.role == 'admin') {
                                                div(class: 'dropdown') {
                                                    a(class: 'dropdown-toggle', 'data-toggle': 'dropdown', href: '#') {
                                                        mkp.yield '+'
                                                    }
                                                    ul(class: 'dropdown-menu', role: 'menu', 'aria-labelledby': 'dLabel') {
                                                        requestStatus.each { status ->
                                                            li {
                                                                a(href: g.createLink(action: 'updateStatus', params: [id: request.id, status: status.english])) {
                                                                    mkp.yield status.spanish
                                                                }
                                                            }
                                                        }

                                                        li(class: 'divider')
                                                        li {
                                                            a(href: g.createLink(action: 'show', params: [id: request.id])) {
                                                                mkp.yield 'Detalle'
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            p request.user.fullName
                                            p appService.getClassroomCodeOrName(request.classroom)
                                        }
                                    }
                                } else {
                                    td(class: 'block', 'data-datashow': datashow, 'data-block': block) {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    def isSupervisor = { attrs, body ->
        if (session?.user?.role == 'supervisor') {
            out << body()
        }
    }

    def isCoordinator = { attrs, body ->
        if (session?.user?.role == 'coordinador') {
            out << body()
        }
    }

    private Boolean hasClassroomWIFI(String classroom) {
        Map classrooms = grailsApplication.config.ni.edu.uccleon.cls
        String letter = classroom[0]

        if (classroom in classrooms.undefined.code) {
            return false
        }

        if (letter in ['B', 'C', 'D', 'E', 'K']) {
            Map room = classrooms[letter].find {
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

    private String animate(Integer currentDatashow, Integer currentBlock, Integer datashow, List<Integer> blocks) {
        currentDatashow == datashow && currentBlock in blocks ? 'bounce' : ''
    }
}
