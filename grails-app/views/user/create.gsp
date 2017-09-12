<g:applyLayout name="oneColumnSmall">
    <head>
        <title>Crear usuario</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, app"/>
    </head>

    <content tag="main">
        <g:form action="save" autocomplete="off" name="form">
            <section>
                <label for="fullName">Nombres y apellidos</label>
                <g:textField name="fullName" value="${user?.fullName}" class="span8"/>

                <label for="email">Correo</label>
                <g:textField name="email" value="${user?.email}" class="span8"/>
            </section>

            <section>
                <label>Rol</label>
                <g:each in="${roles}" var="role">
                    <label class="radio">
                        <g:radio
                            name="role"
                            value="${role}"
                            checked="${role == 'user'}"/>
                            ${role.capitalize()}
                    </label>
                </g:each>
            </section>

            <section>
                <section>
                    <label>Coordinaciones y departamentos</label>

                    <details ${schoolsAndDepartments.schools.any { school -> school in user?.schools } ? 'open' : ''}>
                        <summary>Coordinaciones</summary>
                        <g:each in="${schoolsAndDepartments.schools.sort()}" var="school">
                            <label class="checkbox">
                                <g:checkBox
                                    name="schools"
                                    value="${school}"
                                    checked="${user?.schools?.contains(school)}"/>
                                    ${school}
                            </label>
                        </g:each>
                    </details>
                </section>

                <section>
                    <details ${schoolsAndDepartments.departments.any { department -> department in user?.schools } ? 'open' : ''}>
                        <summary>Departamentos</summary>
                        <g:each in="${schoolsAndDepartments.departments.sort()}" var="department">
                            <label class="checkbox">
                                <g:checkBox
                                    name="schools"
                                    value="${department}"
                                    checked="${user?.schools?.contains(department)}"/>
                                    ${department}
                            </label>
                        </g:each>
                    </details>
                </section>
            </section>

            <section>
                <label>Aulas</label>
                <g:each in="${classrooms}" var="classroom">
                    <details ${classrooms[classroom.key].code.any { code -> code in user?.classrooms } ? 'open' : ''}>
                        <summary>${classroom.key}</summary>
                        <g:each in="${classrooms[classroom.key]}" var="c">
                            <label class="checkbox">
                                <g:checkBox
                                    form="form"
                                    name="classrooms"
                                    value="${c.code}"
                                    checked="${user?.classrooms?.contains(c.code)}"/>
                                    ${c.name ?: c.code}
                            </label>
                        </g:each>
                    </details>
                </g:each>
            </section>

            <g:submitButton name="send" value="Confirmar y enviar notificacion" class="btn btn-primary"/>
            <g:link action="user" action="list" class="btn btn-default">Regresar</g:link>

            <g:hasErrors bean="${user}">
                <p>
                    <g:renderErrors bean="${user}"/>
                </p>
            </g:hasErrors>
        </g:form>
    </content>
</g:applyLayout>
