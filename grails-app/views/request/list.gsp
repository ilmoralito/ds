<g:applyLayout name="threeColumns">
    <head>
        <title>Solicitudes</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app, requestList"/>
    </head>

    <content tag="main">
        <g:if test="${requestsByBlock}">
            <table class="table table-hover">
                <thead>
                    <th colspan="3">
                        <input type="checkbox" name="trigger" id="trigger">
                    </th>
                </thead>
                <tbody>
                    <g:each in="${requestsByBlock}" var="b">
                        <tr>
                            <td colspan="3">
                                Bloque ${b.block + 1}
                            </td>
                        </tr>
                        <g:each in="${b.requests}" var="r">
                            <tr>
                                <td width="1">
                                    <g:checkBox name="requests" value="${r.id}" checked="false" class="requests" form="status"/>
                                </td>
                                <td>
                                    <g:link action="show" id="${r.id}">
                                        Por ${r.user} de ${r.school} en ${r.classroom}
                                    </g:link>
                                </td>
                                <td width="1">
                                    <g:link action="updateStatus" params="[id: r.id]">
                                        <ds:requestStatus status="${r.status}"/>
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
            <p>Nada que mostrar</p>
        </g:else>
    </content>

    <content tag="col1">
        <g:form action="list" autocomplete="off">
            <label for="requestFromDate">Desde</label>
            <g:textField name="requestFromDate" value="${params?.requestFromDate}" class="span2"/>

            <label for="requestToDate">Hasta</label>
            <g:textField name="requestToDate" value="${params?.requestToDate}" class="span2"/>

            <g:render template="filter"/>

            <label>Estado</label>
            <g:each in="${requestStatus}" var="status">
                <label class="checkbox">
                    <g:checkBox
                        name="status"
                        value="${status.english}"
                        checked="${params?.status?.contains(status.english)}"/>
                    ${status.spanish}
                </label>
            </g:each>

            <g:submitButton name="submit" value="Filtrar" class="btn btn-primary btn-block filter-button"/>
        </g:form>
    </content>
</g:applyLayout>
