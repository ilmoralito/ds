<g:applyLayout name="twoColumns">
    <head>
        <title>Detalle de reporte</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="reportNavBar"/>

        <g:if test="${results}">
            <table class="table">
                <colgroup>
                    <col span="1" style="width: 25%;">
                    <col span="1" style="width: 25%;">
                    <col span="1" style="width: 25%;">
                    <col span="1" style="width: 25%;">
                </colgroup>

                <thead>
                    <tr>
                        <th>Coordinacion</th>
                        <th>Pendientes</th>
                        <th>Atendidos</th>
                        <th>Sin retirar</th>
                        <th>Cancelados</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${results}" var="result">
                        <tr>
                            <td>${result.school}</td>
                            <td>${result.pending}</td>
                            <td>${result.attended}</td>
                            <td>${result.absent}</td>
                            <td>${result.canceled}</td>
                        </tr>
                    </g:each>

                    <tr>
                        <td>TOTAL</td>
                        <td>${results.pending.sum()}</td>
                        <td>${results.attended.sum()}</td>
                        <td>${results.absent.sum()}</td>
                        <td>${results.canceled.sum()}</td>
                    </tr>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>Sin datos que mostrar</p>
        </g:else>
    </content>
</g:applyLayout>
