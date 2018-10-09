<g:applyLayout name="${dataset ? 'threeColumns' : 'twoColumns'}">
    <head>
        <title>Historial</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="nav"/>

        <g:if test="${dataset}">
            <table class="table table-hover table-bordered">
                <col width="45%">
                <col width="65%">

                <thead>
                    <tr>
                        <th>Coordinacion</th>
                        <th>Cantidad</th>
                    </tr>
                </thead>

                <tbody>
                    <g:each in="${dataset}" var="data">
                        <tr>
                            <td>
                                <g:if test="${actionName == 'record'}">
                                    <g:link
                                        action="recordsDetail"
                                        params="[id: params.id, school: data.school]">${data.school}</g:link>
                                </g:if>
                                <g:else>
                                    <g:link
                                        action="recordsDetailByYear"
                                        params="[id: params.id, school: data.school, year: params.year]">${data.school}</g:link>
                                </g:else>
                            </td>
                            <td>${data.count}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>Sin registros que mostrar</p>
        </g:else>
    </content>

    <content tag="col1">
        <years:widget years="${yearWidget.years}" id="${params.id}" currentYear="${params.year}"/>
    </content>
</g:applyLayout>
