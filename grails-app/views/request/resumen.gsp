<g:applyLayout name="threeColumns">
    <head>
        <title>Resumen</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="reportNavBar"/>

        <g:if test="${results}">
            <table class="table">
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
                            <td style="vertical-align: middle;">
                                <g:link action="reportSummary" params="${params?.year ? [month: result.month, year: params.year] : [month: result.month]}">
                                    ${result.monthName}
                                </g:link>
                            </td>
                            <td>
                                <div
                                    style="background: #222; width: ${!params?.year ? result.quantity / 3 : result.quantity}px; padding: 5px; color: #FFF;">
                                    ${result.quantity}
                                </div>
                            </td>
                        </tr>
                    </g:each>

                    <tr>
                        <td>TOTAL</td>
                        <td>${results.quantity.sum()}</td>
                    </tr>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>Sin datos que mostrar</p>
        </g:else>
    </content>

    <content tag="col1">
        <g:form action="resumen" autocomplete="off">
            <g:render template="years" model="[years: yearFilter.years]"/>

            <g:submitButton name="send" value="Filtrar" class="btn btn-primary btn-block"/>
        </g:form>
    </content>
</g:applyLayout>
