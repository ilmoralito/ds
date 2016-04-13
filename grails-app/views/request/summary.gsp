<g:applyLayout name="twoColumns">
    <head>
        <title>Resumen</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="reportNavBar"/>

        <g:if test="${data}">
            <g:each in="${data}" var="y">
                <h4>${y.year}</h4>

                <table class="table">
                    <thead>
                        <tr>
                            <th>Mes</th>
                            <th>Pendientes</th>
                            <th>Atendidos</th>
                            <th>No retirados</th>
                            <th>Cancelados</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <g:each in="${y.months}" var="m">
                            <tr>
                                <td>${m.month}</td>
                                <td>${m.pending}</td>
                                <td>${m.attended}</td>
                                <td>${m.absent}</td>
                                <td>${m.canceled}</td>
                                <td>${m.total}</td>
                            </tr>
                        </g:each>
                        <tr>
                            <td>TOTAL</td>
                            <td>#</td>
                            <td>#</td>
                            <td>#</td>
                            <td>#</td>
                            <td>#</td>
                        </tr>
                    </tbody>
                </table>
            </g:each>
        </g:if>
        <g:else>
            <p>...</p>
        </g:else>
    </content>
</g:applyLayout>
