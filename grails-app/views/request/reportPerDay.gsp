<g:applyLayout name="threeColumns">
    <head>
        <title>Reporte por dia</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="reportNavBar"/>

        <g:if test="${results}">
            <table class="table">
                <colgroup>
                    <col span="1" style="width: 33.3%;">
                    <col span="1" style="width: 33.3%;">
                    <col span="1" style="width: 33.3%;">
                </colgroup>
            
                <thead>
                    <tr>
                        <th>Dia</th>
                        <th>Cantidad</th>
                        <th>Porcentaje</th>
                    </tr>
                </thead>

                <tbody>
                    <g:each in="${results}" var="data">
                        <tr>
                            <td>${data.day}</td>
                            <td>${data.quantity}</td>
                            <td>${data.percentage}</td>
                        </tr>
                    </g:each>
                    <tr>
                        <td>TOTAL</td>
                        <td>${results.quantity.sum()}</td>
                        <td>100%</td>
                    </tr>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>Sin datos que mostrar</p>
        </g:else>
    </content>

    <content tag="col1">
        <g:form action="reportPerDay" autocomplete="off">
            <g:render template="years" model="[years: yearFilter.years]"/>

            <g:submitButton name="send" value="Filtrar" class="btn btn-primary btn-block"/>
        </g:form>
    </content>
</g:applyLayout>
