<g:applyLayout name="${layout}">
    <head>
        <title>Actividades</title>
        <g:set var="mainStyle" value="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app, activity"/>
        <g:set var="activityStyle" value="bootstrap-css, app"/>
        <r:require modules = "${!session?.user ? activityStyle : mainStyle}"/>
    </head>

    <content tag="main">
        <ds:activitiesTable requests="${requests}" datashows="${datashows}" blocks="${blocks}" dateOfApplication="${dateOfApplication}"/>
    </content>

    <content tag="col1">
        <g:form action="filter" autocomplete="off">

            <g:submitButton name="filter" value="Filtrar" class="btn btn-primary btn-block"/>
        </g:form>
    </content>
</g:applyLayout>
