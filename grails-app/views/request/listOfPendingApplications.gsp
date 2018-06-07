<%@ page import="groovy.json.*" %>

<g:applyLayout name="${requestState.dataset ? 'threeColumns' : 'twoColumns'}">
    <head>
        <title>Lista de solicitudes pendientes</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, filter"/>

        <script>
            window.serverURL = "${grailsApplication.config.grails.serverURL}"
        </script>
    </head>

    <content tag="main">
        <g:render template="toolbar"/>

        <g:if test="${dataset}">
            <input id="filter" placeholder="Filtrar...">

            <table class="table table-hover">
                <col width="98%">
                <col width="1%">
                <col width="1%">

                <tbody>
                    <g:each in="${dataset}" var="data" status="index">
                        <tr>
                            <td colspan="3" style="${index == 0 ? 'border-top: 0;' : ''}">
                                ${data.date}
                            </td>
                        </tr>
                        <g:each in="${data.details}" var="detail">
                            <tr>
                                <td
                                    data-id="${detail.id}"
                                    data-user="${detail.user}"
                                    data-classroom="${detail.classroom}"
                                    data-date="${data.date}"
                                    style="vertical-align: middle;">
                                    <g:link action="show" id="${detail.id}">${detail.user} en ${detail.classroom}</g:link>
                                </td>
                                <td>
                                    <g:link controller="request" action="edit" id="${detail.id}" class="btn btn-mini btn-link">
                                        <i class="icon-pencil"></i>
                                    </g:link>
                                </td>
                                <td>
                                    <g:form
                                        action="delete"
                                        style="margin: 0;"
                                        id="${detail.id}"
                                        onSubmit="if (!confirm('¿Estás seguro?')) return false;">
                                        <g:hiddenField name="_method" value="DELETE"/>

                                        <button type="submit" class="btn btn-mini btn-link">
                                            <i class="icon-trash"></i>
                                        </button>
                                    </g:form>
                                </td>
                            </tr>
                        </g:each>
                    </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>Sin datos que mostrar</p>
        </g:else>
    </content>

    <content tag="col1">
        <g:if test="${requestState.dataset}">
            <input id="filter-box" placeholder="Filtrar..." class="span2">

            <div id="filter-list">
                <g:each in="${requestState.dataset}" var="dataset">
                    <div
                        data-applicant="${dataset.applicant}"
                        data-total="${dataset.total}"
                        data-requests="${new JsonBuilder(dataset.requests)}">
                        <p class="applicant">${dataset.applicant}</p>

                        <g:each in="${dataset.requests}" var="request">
                            <div class="bar" style="width: ${request.count + 20}px;">
                                ${request.count}
                            </div>

                            <div class="school">${request.school}</div>
                        </g:each>
                    </div>
                </g:each>
            </div>
        </g:if>
    </content>
</g:applyLayout>
