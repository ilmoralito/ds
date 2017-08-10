<g:applyLayout name="twoColumns">
    <head>
        <title>Lista de usuarios</title>
        <r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, users"/>
    </head>

    <content tag="main">
        <div class="clearfix">
            <div class="pull-right">
                <g:link action="create" class="btn btn-primary">Crear usuario</g:link>
                <g:link action="filter" class="btn btn-default">
                    <i class="icon-filter"></i> Filtrar
                </g:link>
            </div>
        </div>

        <g:if test="${users}">
            <table class="table table-hover">
                <colgroup>
                    <col span="1" style="width: 99%;">
                    <col span="1" style="width: 1%;">
                </colgroup>
                <thead>
                    <tr>
                        <th>
                            <input id="rosterFilterBox" name="rosterFilterBox" type="text" style="margin-bottom: 0;" placeholder="Filtrar">
                        </th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${users}" var="user">
                        <tr>
                            <td>
                                <g:link action="show" id="${user.id}" class="target">${user.fullName}</g:link>
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
</g:applyLayout>
