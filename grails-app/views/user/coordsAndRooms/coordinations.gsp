<g:applyLayout name="twoColumns">
    <head>
        <title>Selecciona coordinacion</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <p>Selecciona coordinacion</p>

        <g:form>
            <g:each in="${userCoordinations}" var="coordination">
                <div class="radio">
                    <label>
                        <g:radio name="coordination" value="${coordination}"/>
                        ${coordination}
                    </label>
                </div>
            </g:each>

            <g:submitButton name="confirm" value="Continuar" class="btn btn-primary" style="margin-top: 10px;"/>
        </g:form>
    </content>
</g:applyLayout>
