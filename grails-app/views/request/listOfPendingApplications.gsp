<g:applyLayout name="twoColumns">
    <head>
        <title>Lista de solicitudes pendientes</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="toolbar"/>

        <g:if test="${dataset}">
            <table class="table table-hover">
                <tbody>
                    <g:each in="${dataset}" var="data" status="index">
                        <tr>
                            <td colspan="2" style="${index == 0 ? 'border-top: 0;' : ''}">
                                ${data.date}
                            </td>
                        </tr>
                        <g:each in="${data.details}" var="detail">
                            <tr>
                                <td style="vertical-align: middle;">
                                    <g:link action="show" id="${detail.id}">${detail.user} en ${detail.classroom}</g:link>
                                </td>
                                <td width="1">
                                    <g:form
                                        action="delete"
                                        style="margin: 0;"
                                        id="${detail.id}"
                                        onSubmit="if (!confirm('Are you sure?')) return false;">
                                        <g:hiddenField name="_method" value="DELETE"/>

                                        <button type="submit" class="btn btn-link btn-small">
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
</g:applyLayout>
