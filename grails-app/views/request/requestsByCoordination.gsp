<g:applyLayout name="twoColumns">
    <head>
        <title>Solicitudes</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="toolbar"/>

        <g:if test="${results}">
            <table class="table table-hover">
                <tbody>
                    <g:each in="${results}" var="year">
                        <tr>
                            <td colspan="2" style="border-top: 0;">
                                <strong>${year.year}</strong>
                            </td>
                        </tr>

                        <g:each in="${year.schools}" var="school">
                            <tr>
                                <td colspan="2">
                                    <strong>${school.name}</strong>
                                </td>
                            </tr>

                            <g:each in="${school.months}" var="month">
                                <tr>
                                    <td width="1">${month.name}</td>
                                    <td>${month.count}</td>
                                </tr>
                            </g:each>
                        </g:each>
                    </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>Nada que mostrar</p>
        </g:else>
  </content>
</g:applyLayout>
