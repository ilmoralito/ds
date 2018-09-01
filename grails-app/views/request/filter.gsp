<g:applyLayout name="oneColumnSmall">
    <head>
        <title>Filtrar solicitudes</title>
        <r:require modules = "bootstrap-css, bootstrap-js, bootstrap-responsive-css, jquery-ui, datepicker, users"/>
    </head>

    <content tag="main">
        <g:form action="applyFilter" autocomplete="off">
            <div class="row">
                <div class="span4">
                    <label for="since">Desde</label>
                    <g:textField name="since" value="${params?.since}" class="span4"/>
                </div>
                <div class="span4">
                    <label for="till">Hasta</label>
                    <g:textField name="till" value="${params?.till}" class="span4"/>
                </div>
            </div>

            <g:submitButton name="submit" value="Filtrar" class="btn btn-primary"/>
            <g:link action="list" class="btn btn-default">Regresar</g:link>
        </g:form>

    </content>
</g:applyLayout>
