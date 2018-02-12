<g:applyLayout name="twoColumns">
    <head>
        <title>Lista de usuarios</title>
        <r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, players"/>
    </head>

    <content tag="main">
        <script>
            serverURL = "${grailsApplication.config.grails.serverURL}"
        </script>

        <div class="clearfix">
            <div class="pull-right">
                <g:link action="filter" class="btn btn-default">
                    <i class="icon-filter"></i> Filtrar
                </g:link>
                <g:link action="create" class="btn btn-primary">Crear usuario</g:link>
            </div>
        </div>

        <g:if test="${users}">
            <table id="users" class="table table-hover">
                <colgroup>
                    <col span="1" style="width: 99%;">
                    <col span="1" style="width: 1%;">
                </colgroup>
                <thead>
                    <tr>
                        <th>
                            <input id="filter" placeholder="Filtrar">
                        </th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${users}" var="user">
                        <tr data-user-id="${user.id}" data-user-fullName="${user.fullName}">
                            <td>
                                <g:link action="show" id="${user.id}" class="target">${user.fullName}</g:link>
                            </td>
                            <td>
                                <g:link
                                    action="delete"
                                    id="${user.id}"
                                    onclick="if (!confirm('Seguro?')) return false;">
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
</g:applyLayout>
