<g:applyLayout name="threeColumns">
    <head>
        <title>Historial detalle</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="nav"/>

        <table class="table table-hover table-bordered">
            <col width="47%">
            <col width="12%">
            <col width="12%">
            <col width="12%">
            <col width="12%">
            <col width="5%">

            <thead>
                <tr>
                    <th>Mes</th>
                    <th>Pendientes</th>
                    <th>Atendidos</th>
                    <th>Sin retirar</th>
                    <th>Cancelados</th>
                    <th>Total</th>
                </tr>
            </thead>

            <tbody>
                <g:each in="${dataset}" var="data">
                    <tr>
                        <td>
                            <g:if test="${actionName == 'recordsDetail' && data.total}">
                                <g:link
                                    action="recordsDetailSummary"
                                    params="[id: params.id, school: params.school, month: data.month]">
                                    ${data.monthname}
                                </g:link>
                            </g:if>
                            <g:elseif test="${actionName == 'recordsDetailByYear' && data.total}">
                                <g:link
                                    action="recordsDetailSummaryByYear"
                                    params="[id: params.id, school: params.school, month: data.month, year: params.year]">
                                    ${data.monthname}
                                </g:link>
                            </g:elseif>
                            <g:else>
                                ${data.monthname}
                            </g:else>
                        </td>
                        <td>${data.pending}</td>
                        <td>${data.attended}</td>
                        <td>${data.absent}</td>
                        <td>${data.canceled}</td>
                        <td>${data.total}</td>
                    </tr>
                </g:each>
                <tr>
                    <td>
                        <b>Total</b>
                    </td>
                    <td>${dataset.pending.sum()}</td>
                    <td>${dataset.attended.sum()}</td>
                    <td>${dataset.absent.sum()}</td>
                    <td>${dataset.canceled.sum()}</td>
                    <td>${dataset.total.sum()}</td>
                </tr>
            </tbody>
        </table>
    </content>

    <content tag="col1">
        <years:widget years="${yearWidget.years}" id="${params.id}" currentYear="${params.year}"/>
    </content>
</g:applyLayout>
