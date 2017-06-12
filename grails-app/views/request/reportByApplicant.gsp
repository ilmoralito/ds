<g:applyLayout name="threeColumns">
    <head>
        <title>Reporte por soliciantes</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="reportNavBar"/>

        <g:render template="generalReportTable" model="[results: results, membershipTag: 'Solicitante', quantityLabel: 'Cantidad de solicitudes']"/>
    </content>

    <content tag="col1">
        <g:form action="reportByApplicant" autocomplete="off">
            <g:render template="years" model="[years: yearFilter.years]"/>

            <g:submitButton name="send" value="Filtrar" class="btn btn-primary btn-block"/>
        </g:form>
    </content>
</g:applyLayout>
