<g:applyLayout name="${layout}">
    <head>
        <title>Actividades</title>
        <g:set var="mainStyle" value="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app, activity"/>
        <g:set var="activityStyle" value="bootstrap-css, app"/>
        <r:require modules = "${!session?.user ? activityStyle : mainStyle}"/>
    </head>

    <content tag="main">
        <ds:activitiesTable requests="${requests}" datashows="${datashows}" dateOfApplication="${dateOfApplication}"/>
    </content>

    <content tag="col1">
        <ul class="nav nav-tabs">
            <li class="${!params.tab ? 'active' : ''}">
                <g:link action="activity" params="[date: dateOfApplication.format('yyyy-MM-dd') ?: '']">
                    Filtro
                </g:link>
            </li>
            <li class="${params.tab ? 'active' : ''}">
                <g:link action="activity" params="[date: dateOfApplication.format('yyyy-MM-dd') ?: '', tab: 'resumen']">
                    Resumen
                </g:link>
            </li>
        </ul>

        <g:if test="${!params.tab}">
            Implementar filtro con parametros de las solicitudes listadas
        </g:if>
        <g:else>
            Implementar resumen de las solicitudes de la fecha
        </g:else>
    </content>
</g:applyLayout>
