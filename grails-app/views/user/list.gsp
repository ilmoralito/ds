<g:applyLayout name="threeColumns">
    <head>
        <title>Lista de usuarios</title>
        <r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, players"/>
    </head>

    <content tag="main">
        <script>
            serverURL = "${grailsApplication.config.grails.serverURL}"
        </script>

        <g:if test="${users}">
            <table id="users" class="table table-hover">
                <col width="99%">
                <col width="1%">

                <thead>
                    <tr>
                        <th>
                            <input id="filter" placeholder="Filtrar">
                        </th>
                        <th>
                            <g:link action="filter" class="btn btn-mini">
                                <i class="icon-filter"></i>
                            </g:link>
                        </th>
                    </tr>
                </thead>

                <tbody>
                    <g:each in="${users}" var="user">
                        <tr data-user-id="${user.id}" data-user-fullName="${user.fullName}">
                            <td>
                                <g:link action="show" id="${user.id}" class="target">${user.fullName}</g:link>
                            </td>
                            <td style="text-align: center;">
                                <g:link
                                    action="delete"
                                    id="${user.id}"
                                    onclick="if (!confirm('¿Seguro?')) return false;">
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
        <g:form action="create" method="GET" autocomplete="off" style="margin-bottom: 10px;">
            <div style="margin-bottom: 10px;">
                <label for="role">Selecciona un rol</label>
                <g:each in="${roles}" var="role">
                    <div class="radio">
                        <label>
                            <g:radio name="role" value="${role}" /> ${role.capitalize()}
                        </label>
                    </div>
                </g:each>
            </div>

            <button type="submit" class="btn btn-primary btn-block">Agregar</button>
        </g:form>
    </content>
</g:applyLayout>
