<g:applyLayout name="threeColumns">
    <head>
        <title>Reporte de dia</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="nav"/>

        <g:if test="${results}">
            <table class="table table-hover">
                <colgroup>
                    <col span="1" style="width: 45%;">
                    <col span="1" style="width: 27.5%;">
                    <col span="1" style="width: 27.5%;">
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
        <g:render template="yearList" model="[yearList: yearFilter.years]"/>
    </content>
</g:applyLayout>
