<g:applyLayout name="threeColumns">
    <head>
        <title>Resumen de facultad  </title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <p>${params.school}</p>

        <g:if test="${results}">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Mes</th>
                        <th>Numero de solicitudes</th>
                    </tr>
                </thead>

                <tbody>
                    <g:each in="${results}" var="result">
                        <tr>
                            <td style="vertical-align: middle;">
                                <g:link
                                    action="summaryOfTeacherApplicationsInMonth"
                                    params="[school: params.school, month: result.month, year: params?.year]">
                                    ${result.monthName}
                                </g:link>
                            </td>
                            <td>
                                <div
                                    style="background: #222; width: ${!params?.year ? result.count / 3 : result.count}px; padding: 5px; color: #FFF;">
                                    ${result.count}
                                </div>
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

    <content tag="col1">
        <g:if test="${schoolYearFilter.yearList}">
            <ul class="nav nav-pills nav-stacked">
                <li class="${!params?.year ? 'active' : ''}">
                    <g:link action="facultySummary" params="[school: params.school]">Global</g:link>
                </li>

                <g:each in="${schoolYearFilter.yearList}" var="year">
                    <li class="${params.int('year') == year ? 'active' : ''}">
                        <g:link action="facultySummary" params="[school: params.school, year: year]">${year}</g:link>
                    </li>
                </g:each>
            </ul>
        </g:if>
    </content>
</g:applyLayout>
