<ul class="nav nav-tabs">
    <li class="${actionName == 'reportBySchool' ? 'active' : ''}">
        <g:link action="reportBySchool">Facultades</g:link>
    </li>
    <li class="${actionName == 'reportByClassrooms' ? 'active' : ''}">
        <g:link action="reportByClassrooms">Aulas</g:link>
    </li>
    <li class="${actionName == 'reportByApplicant' || actionName == 'coordinationReportPerApplicant' ? 'active' : ''}">
        <g:link action="reportByApplicant">Solicitantes</g:link>
    </li>
    <li class="${actionName == 'reportByDatashows' ? 'active' : ''}">
        <g:link action="reportByDatashows">Datashows</g:link>
    </li>
    <li class="${actionName == 'reportByBlock' ? 'active' : ''}">
        <g:link action="reportByBlock">Bloques</g:link>
    </li>
    <li class="${actionName == 'reportPerDay' ? 'active' : ''}">
        <g:link action="reportPerDay">Dias</g:link>
    </li>
    </li>
    <li class="${actionName == 'reportPerMonth' || actionName == 'coordinationReportPerMonth' ? 'active' : ''}">
        <g:link action="reportPerMonth">Reporte</g:link>
    </li>
    <li class="${params.type == 'resumen' ? 'active' : ''}">
        <g:link action="requestsBy" params="[type:'resumen']">Resumen</g:link>
    </li>
</ul>
