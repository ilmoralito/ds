<ul class="nav nav-tabs">
    <li class="${actionName == 'reportBySchool' ? 'active' : 'not-active'}">
        <g:link action="reportBySchool">Facultades</g:link>
    </li>
    <li class="${actionName == 'reportByClassrooms' ? 'active' : 'no-active'}">
        <g:link action="reportByClassrooms">Aulas</g:link>
    </li>
    <li class="${actionName == 'reportByApplicant' || actionName == 'coordinationReportPerApplicant' ? 'active' : 'not-active'}">
        <g:link action="reportByApplicant">Solicitantes</g:link>
    </li>
    <li class="${actionName == 'reportByDatashows' ? 'active' : 'not-active'}">
        <g:link action="reportByDatashows">Datashows</g:link>
    </li>
    <li class="${actionName == 'reportByBlock' ? 'active' : 'not-active'}">
        <g:link action="reportByBlock">Bloques</g:link>
    </li>
    <li class="${params.type == 'day' ? 'active' : 'not-active'}">
        <g:link action="requestsBy" params="[type:'day']">Dia</g:link>
    </li>
    </li>
    <li class="${params.type == 'report' || actionName == 'reportDetail' ? 'active' : ''}">
        <g:link action="report" params="[type: 'report']">
            Reporte
        </g:link>
    </li>
    <li class="${params.type == 'resumen' ? 'active' : 'not-active'}">
        <g:link action="requestsBy" params="[type:'resumen']">Resumen</g:link>
    </li>
</ul>
