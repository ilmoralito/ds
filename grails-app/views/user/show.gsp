<g:applyLayout name="threeColumns">
    <head>
        <title>${user?.fullName}</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, userShow"/>
    </head>

    <content tag="main">
        <table class="table table-hover">
            <col width="25%">
            <col width="75%">

            <tr>
                <td style="border-top: 0;">Nombre de usuario</td>
                <td style="border-top: 0;">${user.fullName}</td>
            </tr>

            <tr>
                <td style="vertical-align: middle;">Correo</td>
                <td>
                    <input id="target" value="${user.email}" style="" />
                    <button class="btn btn-small trigger" data-clipboard-target="#target">
                        Copiar email
                    </button>
                </td>
            </tr>

            <tr>
                <td>Rol</td>
                <td>${user.role}</td>
            </tr>

            <tr>
                <td>Coordinaciones</td>
                <td>
                    <g:join in="${user.schools.tokenize(',')}"/>
                </td>
            </tr>

            <tr>
                <td>Aulas</td>
                <td>
                    <g:join in="${user.classrooms.tokenize(',')}"/>
                </td>
            </tr>

            <tr>
                <td>Estado</td>
                <td>
                    <g:hiddenField name="id" value="${user?.id}"/>
                    <div class="checkbox">
                        <label>
                            <g:checkBox name="enabled" value="${user?.enabled}" checked="${user?.enabled}" data-id="${user.id}"/>
                            Estado
                        </label>
                    </div>
                </td>
            </tr>
        </table>
    </content>

    <content tag="col1">
        <g:form action="notification" style="margin-bottom: 6px;">
            <g:hiddenField name="id" value="${user.id}"/>
            <button type="submit" class="btn btn-block">Enviar notificacion</button>
        </g:form>

        <g:link action="resetPassword" id="${user.id}" class="btn btn-warning btn-block">Resetear clave</g:link>

        <g:link action="edit" id="${user.id}" class="btn btn-primary btn-block">Editar</g:link>

        <g:javascript>
            window.ajaxURI = "${createLink(controller: 'user', action: 'updateUserEnabledProperty')}"
        </g:javascript>
    </content>
</g:applyLayout>
