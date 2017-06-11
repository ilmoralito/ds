<ul class="nav nav-tabs">
    <li class="${actionName == 'reportBySchool' ? 'active' : 'not-active'}">
        <g:link action="reportBySchool">Facultades</g:link>
    </li>
    <li class="${actionName == 'reportByClassrooms' ? 'active' : 'no-active'}">
        <g:link action="reportByClassrooms">Aulas</g:link>
    </li>
    <li class="${params.type == 'users' ? 'active' : 'not-active'}">
        <g:link action="requestsBy" params="[type:'users']">Por usuarios</g:link>
    </li>
    <li class="${params.type == 'datashows' ? 'active' : 'not-active'}">
        <g:link action="requestsBy" params="[type:'datashows']">Por datashow</g:link>
    </li>
    <li class="${params.type == 'blocks' ? 'active' : 'not-active'}">
        <g:link action="requestsBy" params="[type:'blocks']">Por bloque</g:link>
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
