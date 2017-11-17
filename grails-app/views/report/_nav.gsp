<ul class="nav nav-tabs">
    <li class="${actionName in ['reportBySchool', 'coordinationSummary', 'coordinationSummaryInMonth'] ? 'active' : ''}">
        <g:link action="reportBySchool">Facultades</g:link>
    </li>
    <li class="${actionName == 'reportByClassrooms' ? 'active' : ''}">
        <g:link action="reportByClassrooms">Aulas</g:link>
    </li>
    <li class="${actionName in ['summaryOfApplicants', 'summaryOfApplicationsPerApplicant'] ? 'active' : ''}">
        <g:link action="summaryOfApplicants">Solicitantes</g:link>
    </li>
    <li class="${actionName in ['reportByDatashows', 'datashowReportSummary', 'datashowReportSummaryPerYear'] ? 'active' : ''}">
        <g:link action="reportByDatashows">Projectores</g:link>
    </li>
    <li class="${actionName == 'reportByBlock' ? 'active' : ''}">
        <g:link action="reportByBlock">Bloques</g:link>
    </li>
    <li class="${actionName == 'reportPerDay' ? 'active' : ''}">
        <g:link action="reportPerDay">Dias</g:link>
    </li>
    </li>
    <li class="${actionName in ['resumen', 'reportSummary', 'summaryByCoordination', 'summaryByUser'] ? 'active' : ''}">
        <g:link action="resumen">Resumen</g:link>
    </li>
</ul>
