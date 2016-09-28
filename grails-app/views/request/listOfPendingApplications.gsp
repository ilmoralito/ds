<g:applyLayout name="twoColumns">
    <head>
        <title>Lista de solicitudes pendientes</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="toolbar"/>

        <g:if test="${results}">
            <g:each in="${results}" var="r">
                <p>${r.key}</p>

                <table class="table table-hover">
                    <tbody>
                        <g:each in="${r.value}" var="ri">
                            <tr>
                                <td>
                                    <g:link action="editRequest" id="${ri.id}">
                                        Para ${ri.school} en ${ri.classroom}
                                    </g:link>
                                </td>
                                <td width="1">
                                    <g:link action="delete" params="[id: ri.id]"><i class="icon-trash"></i></g:link>
                                </td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
            </g:each>
        </g:if>
        <g:else>
            <p>Nada que mostrar</p>
        </g:else>
    </content>
</g:applyLayout>
