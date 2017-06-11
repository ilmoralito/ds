<g:applyLayout name="threeColumns">
    <head>
        <title>Reporte por aulas</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="reportNavBar"/>

        <g:if test="${results}">
            <table class="table">
                <thead>
                    <tr>
                        <th>Aula</th>
                        <th>Cantidad en aula</th>
                    </tr>
                </thead>

                <tbody>
                    <g:each in="${results}" var="data">
                        <tr>
                            <td>${data.classroom}</td>
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
        <g:form action="reportByClassrooms" autocomplete="off">
            <g:render template="years" model="[years: yearFilter.years]"/>

            <g:submitButton name="send" value="Filtrar" class="btn btn-primary btn-block"/>
        </g:form>
    </content>
</g:applyLayout>
