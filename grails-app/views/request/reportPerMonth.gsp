<g:applyLayout name="threeColumns">
    <head>
        <title>Reporte por mes</title>
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
                    <g:each in="${results}" var="data">
                        <tr>
                            <td>
                                <g:link action="coordinationReportPerMonth" params="${params?.year ? [month: data.month, year: params.year] : [month: data.month]}">
                                    ${data.monthName}
                                </g:link>
                            </td>
                            <td>${data.quantity}</td>
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
        <g:form action="reportPerMonth" autocomplete="off">
            <g:render template="years" model="[years: yearFilter.years]"/>

            <g:submitButton name="send" value="Filtrar" class="btn btn-primary btn-block"/>
        </g:form>
    </content>
</g:applyLayout>
