<g:applyLayout name="twoColumns">
    <head>
        <title>Detalle de reporte por usuario</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="nav"/>

        <g:if test="${results}">
            <table class="table table-hover">
                <colgroup>
                    <col span="1" style="width: 25%;">
                    <col span="1" style="width: 75%;">
                </colgroup>

                <thead>
                    <tr>
                        <th>Solicitudes</th>
                        <th>Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${results}" var="result">
                        <tr>
                            <td>
                                <g:link controller="request" action="show" id="${result.id}">
                                    <g:formatDate date="${result.date}" format="yyyy-MM-dd"/>
                                </g:link>
                            </td>
                            <td>
                                <ds:requestStatus status="${result.status}"/>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>Sin datos que mostrar</p>
        </g:else>
    </content>
</g:applyLayout>
