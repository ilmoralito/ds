<g:applyLayout name="twoColumns">
    <head>
        <title>${user?.fullName}</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, userShow"/>

        <g:javascript>
            window.ajaxURI = "${createLink(controller: 'user', action: 'updateUserEnabledProperty')}"
        </g:javascript>
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

            <g:if test="${user.role != 'admin'}">
                <tr>
                    <td>Coordinaciones</td>
                    <td>
                        ${user?.schools?.tokenize(',')?.join(', ')}
                    </td>
                </tr>

                <tr>
                    <td>Aulas</td>
                    <td>
                        ${user?.classrooms?.tokenize(',')?.join(', ')}
                    </td>
                </tr>
            </g:if>

            <tr>
                <td>Estado</td>
                <td>
                    <g:hiddenField name="id" value="${user?.id}"/>
                    <g:checkBox name="enabled" value="${user?.enabled}" checked="${user?.enabled}" data-id="${user.id}"/>
                </td>
            </tr>
        </table>

        <g:link action="edit" id="${user.id}" class="btn">Editar</g:link>
        <g:link action="resetPassword" id="${user.id}" class="btn">Resetear clave</g:link>

        <ds:displayWhen role="${user.role}">
            <button type="submit" form="notificationForm" class="btn">Reenviar notificacion</button>

            <g:form action="notification" name="notificationForm">
                <g:hiddenField name="id" value="${user.id}"/>
            </g:form>
        </ds:displayWhen>
    </content>
</g:applyLayout>
