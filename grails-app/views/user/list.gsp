<g:applyLayout name="threeColumns">
    <head>
        <title>Usuarios</title>
        <r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, users"/>
    </head>

    <content tag="main">
        <g:if test="${users}">
            <table class="table table-hover">
                <colgroup>
                    <col span="1" style="width: 95%;">
                    <col span="1" style="width: 5%;">
                </colgroup>
                <thead>
                    <tr>
                        <th>
                            <input
                                id="rosterFilterBox"
                                name="rosterFilterBox"
                                type="text"
                                style="margin-bottom: 0;"
                                placeholder="Filtrar">
                        </th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${users}" var="user">
                        <tr>
                            <td>
                                <g:link action="show" id="${user.id}" class="target">
                                    ${user.fullName}
                                </g:link>
                            </td>
                            <td>
                                <g:link action="delete" id="${user.id}" class="action-delete">
                                    <i class="icon-trash"></i>
                                </g:link>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>Nada que mostrar</p>
        </g:else>
    </content>

    <content tag="col1">
        <section>
            <g:link action="create" class="btn btn-block btn-primary">Crear usuario</g:link>
        </section>

        <section>
            <g:form action="list">
                <g:textField name="fullName" value="${params?.fullName}" class="span2" placeholder="Nombre"/>

                <label>Estado</label>
                <label class="checkbox">
                    <g:checkBox name="enabled" value="true" checked="${params.list('enabled').contains('true')}"/>
                    Activos
                </label>

                <label class="checkbox">
                    <g:checkBox name="enabled" value="false" checked="${params.list('enabled').contains('false')}"/>
                    Inactivos
                </label>

                <ds:rol listOfRoles="${params.list('roles')}"/>

                <ds:coordinations area="schools" list="${params.list('schools')}"/>

                <ds:coordinations area="departments" list="${params.list('departments')}"/>

                <button type="submit" class="btn btn-primary btn-block">Filtrar</button>
            </g:form>
        </section>
    </content>
</g:applyLayout>
