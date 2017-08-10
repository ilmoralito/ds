<g:applyLayout name="twoColumns">
    <head>
        <title>Lista de solicitudes</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app, requestList"/>
    </head>

    <content tag="main">
        <div class="clearfix">
            <g:link action="filter" class="btn btn-default pull-right">
                <i class="icon-filter"></i> Filtrar
            </g:link>
        </div>

        <g:if test="${results}">
            <table class="table table-hover">
                <thead>
                    <th colspan="3">
                        <input type="checkbox" name="trigger" id="trigger">
                    </th>
                </thead>
                <tbody>
                    <g:each in="${results}" var="result">
                        <tr>
                            <td colspan="3">
                                Bloque ${result.block + 1}
                            </td>
                        </tr>
                        <g:each in="${result.requests}" var="request">
                            <tr>
                                <td width="1">
                                    <g:checkBox name="requests" value="${request.id}" checked="false" class="requests" form="status"/>
                                </td>
                                <td>
                                    <g:link action="show" id="${request.id}">
                                        Por ${request.user} de ${request.school} en ${request.classroom}
                                    </g:link>
                                </td>
                                <td width="1">
                                    <g:link action="updateStatus" params="[id: request.id]">
                                        <ds:requestStatus status="${request.status}"/>
                                    </g:link>
                                </td>
                            </tr>
                        </g:each>
                    </g:each>
                </tbody>
            </table>

            <g:form action="changeRequestsStatus" name="status">
                <g:hiddenField name="newStatus"/>
                <g:hiddenField name="fromDate" value="${params?.requestFromDate}"/>
                <g:hiddenField name="toDate" value="${params?.requestToDate}"/>

                <g:each in="${requestStatus.findAll { it.english != 'pending' }}" var="status">
                    <button type="submit" class="btn trigger" data-status="${status.english}">
                        ${status.spanish}
                    </button>
                </g:each>
            </g:form>
        </g:if>
        <g:else>
            <p>Sin resultados que mostrar</p>
        </g:else>
    </content>
</g:applyLayout>
