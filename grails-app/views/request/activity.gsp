<g:applyLayout name="${layout}">
    <head>
        <title>Actividades</title>
        <g:set var="mainStyle" value="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, activity"/>
        <g:set var="activityStyle" value="bootstrap-css, bootstrap-modal, publicActivity"/>
        <r:require modules = "${!session?.user ? activityStyle : mainStyle}"/>

        <g:if test="${!session.user}">
            <script>
                listTodayActivitiesURL = "${createLink(controller: 'request', action: 'listTodayActivities')}";
            </script>
        </g:if>
    </head>
    <content tag="main">
        <ds:activitiesTable requests="${requestList}" datashows="${datashows}" dateOfApplication="${dateOfApplication}" layout="${layout}"/>

        <ds:isAdmin>
            <g:if test="${requestList}">
                <g:each in="${statusList}" var="status">
                    <g:link
                        action="updateAllStatus"
                        params="[status: status.english, applicationDate: dateOfApplication.format('yyyy-MM-dd')]"
                        class="btn"
                        onclick="if (!confirm('¿Seguro de continuar?')) return false">
                        ${status.spanish}
                    </g:link>
                </g:each>
            </g:if>
        </ds:isAdmin>

        <div id="modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3>Requerimientos</h3>
            </div>
            <div class="modal-body">
                <ul id="requirement"></ul>
            </div>
        </div>

        <div id="target"></div>
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
