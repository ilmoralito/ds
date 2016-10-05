<g:applyLayout name="fourColumns">
    <head>
        <title>Crear usuario</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, app"/>
    </head>

    <content tag="main">
        <g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.cls}"/>
        <g:set var="roles" value="${grailsApplication.config.ni.edu.uccleon.roles}"/>

        <g:form action="create" autocomplete="off" name="form">
            <p>Perfil</p>

            <label for="email">Correo</label>
            <g:textField name="email" value="${user?.email}" class="span3"/>

            <label for="fullName">Nombre y apellido</label>
            <g:textField name="fullName" value="${user?.fullName}" class="span3"/>

            <p>Role</p>
            <g:each in="${roles}" var="role">
                <label class="radio">
                    <g:radio
                        name="role"
                        value="${role}"
                        checked="${role == 'user'}"/>
                        ${role.capitalize()}
                </label>
            </g:each>

            <g:submitButton
                name="send"
                value="Guardar y enviar notificacion"
                class="btn btn-primary"/>

            <g:hasErrors bean="${user}">
                <p><g:renderErrors bean="${user}"/></p>
            </g:hasErrors>
        </g:form>
    </content>

    <content tag="col1">
        <g:render template="schoolsAndDepartments"/>
    </content>

    <content tag="col2">
        <p>Aulas</p>
        <g:each in="${classrooms}" var="classroom">
            <p>${classroom.key}</p>
            <g:each in="${classrooms[classroom.key]}" var="c">
                <label class="checkbox">
                    <g:checkBox
                        form="form"
                        name="classrooms"
                        value="${c.code}"
                        checked="${userClassrooms?.contains(c.code)}"/>
                        ${c.name ?: c.code}
                </label>
            </g:each>
        </g:each>
    </content>
</g:applyLayout>
