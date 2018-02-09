<g:applyLayout name="oneColumnSmall">
    <head>
        <title>Filtrar solicitudes</title>
        <r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, users"/>
    </head>

    <content tag="main">
        <g:form action="applyFilter" autocomplete="off">
            <label for="requestFromDate">Desde</label>
            <g:textField name="requestFromDate" value="${params?.requestFromDate}" class="span8"/>

            <label for="requestToDate">Hasta</label>
            <g:textField name="requestToDate" value="${params?.requestToDate}" class="span8"/>

            <g:submitButton name="submit" value="Filtrar" class="btn btn-primary"/>
            <g:link action="list" class="btn btn-default">Regresar</g:link>
        </g:form>
    </content>
</g:applyLayout>
