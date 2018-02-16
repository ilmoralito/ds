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
                        <th colspan="2">
                            <input id="filter" placeholder="Filtrar">
                        </th>
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

    <content tag="col1">
        <g:form action="create" method="GET" autocomplete="off">
            <label for="role">Rol</label>
            <g:select name="role" from="${roles}" optionValue="${{ it == 'user' ? 'Profesor' : it.capitalize() }}" class="span2"/>

            <button type="submit" class="btn btn-primary btn-block">Agregar</button>
        </g:form>

        <g:link action="filter" class="btn btn-default btn-block">
            <i class="icon-filter"></i> Filtrar
        </g:link>
    </content>
</g:applyLayout>
