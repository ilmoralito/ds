<g:applyLayout name="threeColumns">
    <head>
        <title>Perfil: ${user?.fullName}</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, userShow"/>
    </head>

    <content tag="main">
        <h4>Perfil</h4>
        <p>
            <label for="">Nombre complete</label>
            ${user?.fullName}
        </p>

        <p>
            <label for="">Correo</label>
            <input id="target" value="${user?.email}" />
            <button class="btn btn-small trigger" data-clipboard-target="#target">
                Copiar email
            </button>
        </p>

        <label>Coordinaciones</label>
        <small id="toggleSchools" class="pull-right" style="cursor: pointer;">
            Editar
        </small>

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

        <g:if test="${user?.classrooms}">
            <label for="">Aulas</label>
            <ul>
                <g:each in="${user.classrooms}" var="classroom">
                    <li>${classroom}</li>
                </g:each>
            </ul>
        </g:if>
    </content>

    <content tag="col1">
        <h4>Actualizar rol</h4>
        <ds:roles user="${user}"/>

        <h4>Adminsitrar</h4>
        <g:form action="enableDisableUserAccount">
            <g:hiddenField name="id" value="${user?.id}"/>
            <button type="submit" class="btn btn-default btn-block">Estado actual: <ds:isEnabled status="${user.enabled}"/></button>
        </g:form>

        <g:form action="notification">
            <g:hiddenField name="id" value="${user?.id}"/>
            <button type="submit" class="btn btn-info btn-block">Notificar</button>
        </g:form>

        <g:link action="resetPassword" id="${params?.id}" class="btn btn-warning btn-block">Resetear clave</g:link>

        <g:javascript>
            window.ajaxURL = "${createLink(controller: 'user', action: 'updateUserRole')}"
            window.ajaxPATH = "${createLink(controller: 'user', action: 'updateUserSchools')}"
        </g:javascript>
    </content>
</g:applyLayout>
