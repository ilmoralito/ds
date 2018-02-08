<g:applyLayout name="twoColumns">
    <head>
        <title>Lista de solicitudes</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, requestList"/>
    </head>

    <content tag="main">
        <script>
            serverURL = "${grailsApplication.config.grails.serverURL}"
        </script>

        <div class="clearfix">
            <g:link action="filter" class="btn btn-default pull-right">
                <i class="icon-filter"></i> Filtrar
            </g:link>
        </div>

        <g:if test="${results}">
            <table class="table table-hover">
                <col width="1%">
                <col width="99%">
                <col width="1%">

                <caption id="requestCount">${requestCount} resultado${requestCount > 1 ? 's' : ''}</caption>

                <thead>
                    <tr>
                        <th>
                            <input type="checkbox" name="trigger" id="trigger">
                        </th>
                        <th>
                            <input id="filter" placeholder="Filtrar...">
                        </th>
                    </tr>
                </thead>

                <tbody id="tbody">
                    <g:each in="${results}" var="result">
                        <tr>
                            <td colspan="3">
                                Bloque ${result.block}
                            </td>
                        </tr>
                        <g:each in="${result.requests}" var="request">
                            <tr
                                data-request-id="${request.id}"
                                data-request-user="${request.fullName}"
                                data-request-classroom="${request.classroom}"
                                data-request-school="${request.school}"
                                data-request-status="${request.status}"
                                data-request-blocks="${request.blocks}"
                                class="filtrable">
                                <td>
                                    <g:checkBox name="requests" value="${request.id}" checked="false" class="requests" form="status"/>
                                </td>
                                <td>
                                    <g:link action="show" id="${request.id}">
                                        Por ${request.fullName} de ${request.school} en ${request.classroom}
                                    </g:link>
                                </td>
                                <td>
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
