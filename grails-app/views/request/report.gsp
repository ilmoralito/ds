<g:applyLayout name="twoColumns">
    <head>
        <title>Reporte por departamento</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, app"/>
    </head>

    <content tag="main">
        <g:render template="reportNavBar"/>

        <g:if test="${data}">
            <g:each in="${data}" var="d">
                <h4>${d.year}</h4>

                <table class="table">
                    <tbody>
                        <tr>
                            <g:each in="${d.months}" var="m">
                                <tr>
                                    <td colspan="2">
                                        <strong>${m.month}</strong>
                                    </td>
                                </tr>

                                <g:each in="${m.coordinations.sort { -it.size }}" var="c">
                                    <colgroup>
                                        <col span="1" style="width: 20%;">
                                        <col span="1" style="width: 80%;">
                                    </colgroup>
                                    <tr>
                                        <td>
                                            <g:link
                                                action="detail"
                                                params="[y: d.year, m: m.month, s: c.coordination]">
                                                ${c.coordination}
                                            </g:link>
                                        </td>
                                        <td>${c.size}</td>
                                    </tr>
                                </g:each>
                            </g:each>
                        </tr>
                    </tbody>
                </table>
                
            </g:each>
        </g:if>
        <g:else>
            <h4>...</h4>
        </g:else>
    </content>
</g:applyLayout>
