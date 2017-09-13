<g:applyLayout name="threeColumns">
    <head>
        <title>Resumen</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, application"/>
    </head>

    <content tag="main">
        <g:render template="nav"/>

        <g:if test="${results}">
            <table class="table table-hover">
                <colgroup>
                    <col span="1" style="width: 45%;">
                    <col span="1" style="width: 55%;">
                </colgroup>

                <thead>
                    <tr>
                        <th>Mes</th>
                        <th>Cantidad</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${results}" var="result">
                        <tr>
                            <td>
                                <g:link
                                    action="reportSummary"
                                    params="${params?.year ? [month: result.month, year: params.year] : [month: result.month]}">
                                    ${result.monthName}
                                </g:link>
                            </td>
                            <td class="data" data-month="${result.month}" data-quantity="${result.quantity}">
                                ${result.quantity}
                            </td>
                        </tr>
                    </g:each>

                    <tr>
                        <td>TOTAL</td>
                        <td>${results.quantity.sum()}</td>
                    </tr>
                </tbody>
            </table>

            <canvas id="chart" width="400" height="400"></canvas>
        </g:if>
        <g:else>
            <p>Sin datos que mostrar</p>
        </g:else>
    </content>

    <content tag="col1">
        <g:render template="yearList" model="[yearList: yearFilter.years]"/>
    </content>
</g:applyLayout>
