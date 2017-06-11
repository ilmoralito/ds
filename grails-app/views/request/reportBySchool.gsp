<g:applyLayout name="threeColumns">
    <head>
        <title>Reporte por coordinacion</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="reportNavBar"/>

        <g:if test="${data}">
            <table class="table">
                <thead>
                    <tr>
                        <th>Mes</th>
                        <th>Cantidad en mes</th>
                    </tr>
                </thead>

                <tbody>
                    <g:each in="${data}" var="d">
                        <tr>
                            <td>${d.month}</td>
                            <td>${d.quantity}</td>
                        </tr>
                    </g:each>

                    <tr>
                        <td>TOTAL</td>
                        <td>${data.quantity.sum()}</td>
                    </tr>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>Sin datos que mostrar</p>
        </g:else>
    </content>

    <content tag="col1">
        <g:form action="reportBySchool" autocomplete="off">
            <label for="school">Facultades</label>
            <select name="school" class="span2">
                <g:each in="${schoolsFilter.schools}" var="area">
                    <option disabled="true">
                        ${area.getKey() == 'schools' ? 'Facultades' : 'Coordinaciones'}
                    </option>

                    <g:each in="${area.getValue()}" var="coordination">
                        <option value="${coordination}" ${coordination == params?.school ? 'selected' : ''}>${coordination}</option>
                    </g:each>
                </g:each>
            </select>

            <g:render template="years" model="[years: schoolsFilter.years]"/>

            <g:submitButton name="send" value="Aplicar" class="btn btn-primary btn-block"/>
        </g:form>
    </content>
</g:applyLayout>
