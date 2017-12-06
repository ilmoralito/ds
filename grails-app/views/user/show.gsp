<g:applyLayout name="threeColumns">
    <head>
        <title>Perfil: ${user?.fullName}</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, userShow"/>
    </head>

    <content tag="main">
        <p>${user?.fullName}</p>

        <p>
            <label>Correo</label>
            <input id="target" value="${user?.email}" />
            <button class="btn btn-small trigger" data-clipboard-target="#target">
                Copiar email
            </button>
        </p>

        <p>Coordinaciones</p>
        <div>
            <small
                id="toggleSchools"
                class="pull-right"
                style="cursor: pointer;">
                Editar
            </small>
        </div>
        <table class="table hide" id="updateSchoolsTable">
            <tbody>
                <g:each in="${coordinations}" var="coordination">
                    <tr>
                        <td width="1">
                            <input
                                type="checkbox"
                                name="coordinations"
                                value="${coordination}"
                                data-id="${user?.id}"
                                data-coordination="${coordination}"
                                ${coordination in user?.schools ? 'checked': ''}>
                        </td>
                        <td>${coordination}</td>
                    </tr>
                </g:each>
            </tbody>
        </table>

        <table class="table" id="userSchoolsTable">
            <tbody>
                <g:each in="${user?.schools}" var="school">
                    <tr>
                        <td>${school}</td>
                    </tr>
                </g:each>
            </tbody>
        </table>

        <label>Aulas</label>
        <table class="table">
            <tbody>
                <g:each in="${user.classrooms}" var="classroom">
                    <tr>
                        <td>${classroom}</td>
                    </tr>
                </g:each>
            </tbody>
        </table>
    </content>

    <content tag="col1">
        <p>Actualizar rol</p>
        <ds:roles user="${user}"/>

        <p>Adminsitrar</p>
        <g:form>
            <g:hiddenField name="id" value="${user?.id}"/>
            <div class="checkbox">
                <label>
                    <g:checkBox name="enabled" value="${user?.enabled}" checked="${user?.enabled}" data-id="${user.id}"/>
                    Estado
                </label>
            </div>
        </g:form>

        <g:form action="notification" style="margin-bottom: 6px;">
            <g:hiddenField name="id" value="${user?.id}"/>
            <button type="submit" class="btn btn-block">Notificar</button>
        </g:form>

        <g:link action="resetPassword" id="${params?.id}" class="btn btn-warning btn-block">Resetear clave</g:link>

        <g:link action="edit" id="${user.id}" class="btn btn-primary btn-block">Editar</g:link>

        <g:javascript>
            window.ajaxURL = "${createLink(controller: 'user', action: 'updateUserRole')}"
            window.ajaxPATH = "${createLink(controller: 'user', action: 'updateUserSchools')}"
            window.ajaxURI = "${createLink(controller: 'user', action: 'updateUserEnabledProperty')}"
        </g:javascript>
    </content>
</g:applyLayout>
