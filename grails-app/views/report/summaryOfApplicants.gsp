<g:applyLayout name="threeColumns">
    <head>
        <title>Sumario de solicitantes</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="nav"/>

        <g:if test="${results}">
            <table class="table table-hover">
                <colgroup>
                    <col span="1" style="width: 35%;">
                    <col span="1" style="width: 15%;">
                    <col span="1" style="width: 15%;">
                    <col span="1" style="width: 15%;">
                    <col span="1" style="width: 15%;">
                    <col span="1" style="width: 5%;">
                </colgroup>

                <thead>
                    <tr>
                        <th>Solicitante</th>
                        <th>Pendientes</th>
                        <th>Atendidos</th>
                        <th>Sin retirar</th>
                        <th>Cancelados</th>
                        <th>TOTAL</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${results}" var="result">
                        <tr>
                            <td>
                                <g:link
                                    action="summaryOfApplicationsPerApplicant"
                                    params="[id: result.id, year: params.year, fullName: result.fullName]">
                                    ${result.fullName}
                                </g:link>
                            </td>
                            <td>${result.pending}</td>
                            <td>${result.attended}</td>
                            <td>${result.absent}</td>
                            <td>${result.canceled}</td>
                            <td>${result.total}</td>
                        </tr>
                    </g:each>

                    <tr>
                        <td>TOTAL</td>
                        <td>${results.pending.sum()}</td>
                        <td>${results.attended.sum()}</td>
                        <td>${results.absent.sum()}</td>
                        <td>${results.canceled.sum()}</td>
                        <td>${results.total.sum()}</td>
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
