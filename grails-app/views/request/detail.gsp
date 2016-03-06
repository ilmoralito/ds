<g:applyLayout name="twoColumns">
    <head>
        <title>Detalle</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="reportNavBar"/>

        <div class="row">
            <div class="span10">
                <g:link
                    action="report"
                    params="[type:'report']"
                    fragment="${params?.y}${params?.m}">
                    Regresar
                </g:link>
            </div>
        </div>
        <br>

        <g:if test="${results}">
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
                    <g:each in="${results.sort{ -it.value.total }}" var="r">
                        <tr>
                            <td>${r.key}</td>
                            <td>${r.value.pending}</td>
                            <td>${r.value.attended}</td>
                            <td>${r.value.absent}</td>
                            <td>${r.value.canceled}</td>
                            <td>${r.value.total}</td>
                        </tr>
                    </g:each>
                    <tr>
                        <td>TOTALES</td>
                        <td>${results*.value.pending.sum()}</td>
                        <td>${results*.value.attended.sum()}</td>
                        <td>${results*.value.absent.sum()}</td>
                        <td>${results*.value.canceled.sum()}</td>
                        <td>${results*.value.total.sum()}</td>
                    </tr>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>...</p>
        </g:else>
    </content>
</g:applyLayout>
