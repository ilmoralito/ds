package ni.edu.uccleon

import static java.util.Calendar.*
import groovy.json.JsonOutput
import groovy.xml.*

class CommonTagLib {

    RequestService requestService
    UserService userService
    AppService appService
    def grailsApplication

    static namespace = "ds"

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

        if (currentUser.role == 'admin') {
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

    def message = { attrs, body ->
        def req = attrs.request
        def blocks = attrs.blocks

        if (session?.user?.role == "admin") {
            out << "${req.user.fullName}, ${req.classroom}, bloques ${blocks}"
        } else {
            out << "${req.dateOfApplication.format('yyyy-MM-dd')}, ${req.classroom}, ${blocks}"
        }
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
        MarkupBuilder builder = new MarkupBuilder(out)
        Map<String, String> schoolParameters = [:]
        List<String> currentUserSchools = session.schools

        if (hasValidRole()) {
            builder.form(action: createLink(controller: 'request', action: 'buildRequest'), autocomplete: 'off', class: 'create-request') {
                if (currentUserSchools.size() > 1) {
                    div(class: 'form-group') {
                        label(for: 'school') {
                            mkp.yield 'Coordinacion'
                        }

                        delegate.select(name: 'school', class: 'form-control input-block-level') {
                            currentUserSchools.each { school ->
                                if (school == params?.school) {
                                    schoolParameters.selected = true
                                } else {
                                    schoolParameters.remove('selected')
                                }

                                schoolParameters.value = school

                                option(schoolParameters) {
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

                    input(type: 'text', id: 'dateOfApplication', name: 'dateOfApplication', class: 'form-control input-block-level', value: params?.dateOfApplication)
                }

                input(type: 'submit', value: 'Crear solicitud', class: 'btn btn-primary btn-block')
            }
        }
    }

    def usersBySchool = { attrs ->
        List<Map> userList = userService.getUserListBySchool(attrs.school)
        User selectedUser = attrs.user ?: userService.getCurrentUser()
        MarkupBuilder markupBuilder = new MarkupBuilder(out)
        Map<String, String> parameters = [:]

        if (userList.size() == 1) {
            markupBuilder.input(type: 'hidden', name: 'user', value: userList[0].id)
        } else {
            markupBuilder.div(class: 'form-group') {
                label(for: 'user') {
                    mkp.yield 'A nombre de'
                }

                delegate.select(id: 'user', name: 'user', class: 'form-control') {
                    userList.each { user ->
                        List<String> classrooms = user.classrooms.tokenize(',')
                        List<Map<String, String>> classroomList = groupClassroomByCode(classrooms)

                        selectedUser.id == user.id ? parameters.selected = true : parameters.remove('selected')
                        parameters.value = user.id
                        parameters['data-classrooms'] = JsonOutput.toJson(classroomList)

                        option(parameters) {
                            mkp.yield user.fullName
                        }
                    }
                }
            }
        }
    }

    def userClassrooms = { attrs ->
        MarkupBuilder markupBuilder = new MarkupBuilder(out)
        List<String> userClassrooms = attrs.user ? userService.getUserClassrooms(attrs.user.id) : getCurrentUserClassroomList()
        List undefinedClassroomList = getUndefinedClassroomList(userClassrooms)
        List classroomList = getGroupedClassroomList(userClassrooms)
        String selected = attrs.selected
        Map parameters = [:]

        markupBuilder.div(class: 'form-group') {
            label(for: 'classroom') {
                mkp.yield 'Aula'
            }

            delegate.select(id: 'classroom', name: 'classroom', class: 'form-group') {
                classroomList.each { classroomObject ->
                    optGroup(label: classroomObject.code) {
                        classroomObject.classrooms.each { classroom ->
                            parameters.value = classroom
                            parameters['data-wifi'] = this.hasClassroomWIFI(classroom)

                            if (classroom == selected) {
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

                if (undefinedClassroomList) {
                    optGroup(label: 'De uso externo') {
                        undefinedClassroomList.each { place ->
                            parameters.value = place

                            if (place == selected) {
                                parameters.selected = true
                            } else {
                                parameters.remove('selected')
                            }

                            option(parameters) {
                                mkp.yield place
                            }
                        }
                    }
                }
            }
        }
    }

    def blockWidget = { attrs ->
        MarkupBuilder markupBuilder = new MarkupBuilder(out)
        List<Integer> datashows = attrs.blockWidget.datashows
        List<Request> requests = attrs.blockWidget.requests
        Integer blocks = attrs.blockWidget.blocks
        Request requestInstance = attrs.requestInstance
        Map<String, String> parameters = [:]

        markupBuilder.section {
            label 'Bloques'

            table(class: 'table table-hover table-bordered') {
                thead {
                    datashows.eachWithIndex { datashow, index ->
                        if (index == 0) {
                            th(width: 40)
                        }

                        th {
                            input(type: 'checkbox', class: 'toggleTrigger', style: 'margin-top: 0;')

                            span datashow

                            if (this.hasHDMI(datashow)) {
                                small(style: 'font-weight: normal; font-size: .6em;') {
                                    mkp.yield 'HDMI'
                                }
                            }

                            if (this.hasObservations(datashow)) {
                                i(
                                    class: 'icon-info-sign',
                                    style: 'float: right; margin-top: 4px;',
                                    'data-title': 'Observacion',
                                    'data-content': getObservation(datashow).join(', '),
                                    'data-placement': 'top'
                                )
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
                                Request request = requests.find { Request object ->
                                    object.datashow == datashow && block in object.hours.block
                                }

                                if (requestInstance && request == requestInstance) {
                                    parameters.checked = true
                                    parameters.remove('disabled')
                                } else {
                                    if (request) {
                                        parameters.checked = true
                                        parameters.disabled = true
                                    } else {
                                        parameters.remove('checked')
                                        parameters.remove('disabled')
                                    }
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
                                    value: 'Enviar',
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

            div(class: 'responsive-table') {
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
                                    Map request = requests.find { it.datashow == datashow && block in it.blocks }

                                    if (request) {
                                        Integer index = request.blocks.findIndexOf { hour -> hour == block }

                                        td(class: "block hasActivity animated ${animate(datashow, block, params.int('datashow'), params.list('blocks')*.toInteger())}", 'data-datashow': datashow, 'data-block': block) {
                                            if (index == 0) {
                                                if (attrs.layout == 'oneColumn') {
                                                    if (request.audio || request.screen || request.internet || request.pointer || request.cpu || request.description) {
                                                        List<String> props = ['audio', 'screen', 'internet', 'pointer', 'cpu', 'description']
                                                        Map<String, Object> properties = request.subMap(props).findAll { it.value }.inject([:]) { accumulator, currentValue ->
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

                                                p request.fullName
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
    }

    def isSupervisor = { attrs, body ->
        if (session?.user?.role == 'supervisor') {
            out << body()
        }
    }

    def isCoordinator = { attrs, body ->
        User currentUser = userService.getCurrentUser()

        if (currentUser.role == 'coordinador') {
            out << body()
        }
    }

    def medias = { attrs ->
        List<String> medias = attrs.inject([]) { accumulator, currentValue ->
            if (currentValue.value) accumulator << currentValue.key

            accumulator
        }

        out << medias.join(', ')
    }

    private Boolean hasValidRole() {
        session.user.role in ['coordinador', 'asistente', 'administrativo', 'supervisor']
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

    private List getClassrooms(final List<String> classrooms) {
        List result = classrooms.collect { classroom ->
            final String letter = classroom[0]

            grailsApplication.config.ni.edu.uccleon.cls[letter].find {
                it.code == classroom
            }
        }

        result
    }

    private List groupClassroomByCode(final List classrooms) {
        List classroomList = getClassrooms(classrooms)
        List results = classroomList.groupBy { it.code[0] }.collect {
            [code: it.key, classrooms: it.value]
        }

        results
    }

    private Boolean hasHDMI(Integer datashow) {
        Map<String, Object> datashowMap = getDatashow(datashow)

        datashowMap.hdmi ?: false
    }

    private Boolean hasObservations(final Integer datashow) {
        Map<String, Object> datashowMap = getDatashow(datashow)

        datashowMap.containsKey('observation')
    }

    private List<String> getObservation(final Integer datashow) {
        Map<String, Object> datashowMap = getDatashow(datashow)

        datashowMap.observation
    }

    private Map<String, Object> getDatashow(final Integer datashow) {
        grailsApplication.config.ni.edu.uccleon.datashows.find { it.code == datashow }
    }

    private String animate(Integer currentDatashow, Integer currentBlock, Integer datashow, List<Integer> blocks) {
        currentDatashow == datashow && currentBlock in blocks ? 'bounce' : ''
    }

    private List<String> undefinedClassroomList() {
        grailsApplication.config.ni.edu.uccleon.cls.undefined.code
    }

    private List<Map> getGroupedClassroomList(final List<String >classrooms) {
        classrooms
            .findAll { classroom -> classroom[0] in classroomCodes() && !(classroom in undefinedClassroomList())}
            .groupBy { it[0] }
            .collect { [ code: it.key, classrooms: it.value.sort() ] }
    }

    private List<Map> getUndefinedClassroomList(final List<String> classrooms) {
        classrooms.findAll { it in undefinedClassroomList() }
    }

    private List<String> getCurrentUserClassroomList() {
        User currentUser = userService.getCurrentUser()

        userService.getUserClassrooms(currentUser.id)
    }

    private List<String> classroomCodes() {
        ['B', 'C', 'D', 'E', 'K']
    }
}
