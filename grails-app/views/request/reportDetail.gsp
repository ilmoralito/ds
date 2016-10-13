<g:applyLayout name="twoColumns">
    <head>
        <title>Detalle</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="reportNavBar"/>

        <g:if test="${data}">
            <p>${params?.s} ${params?.m} ${params?.y}</p>

            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Solicitado por</th>
                        <th>Pendientes</th>
                        <th>Atendidos</th>
                        <th>No retirados</th>
                        <th>Cancelados</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${data}" var="d">
                        <tr>
                            <td>${d.user}</td>
                            <td>${d.pending}</td>
                            <td>${d.attended}</td>
                            <td>${d.absent}</td>
                            <td>${d.canceled}</td>
                            <td>${d.total}</td>
                        </tr>
                    </g:each>
                    <tr>
                        <td>TOTAL</td>
                        <td>${data.pending.sum()}</td>
                        <td>${data?.attended?.sum()}</td>
                        <td>${data?.absent?.sum()}</td>
                        <td>${data?.canceled?.sum()}</td>
                        <td>${data?.total?.sum()}</td>
                    </tr>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>...</p>
        </g:else>
    </content>
</g:applyLayout>
