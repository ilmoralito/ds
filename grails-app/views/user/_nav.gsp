<ul class="nav nav-tabs">
    <li class="${actionName == 'show' ? 'active' : ''}">
        <g:link action="show" id="${params.id}">Generales</g:link>
    </li>
    <li class="${actionName in ['record', 'recordsByYear', 'recordsDetail', 'recordsDetailByYear', 'recordsDetailSummary', 'recordsDetailSummaryByYear'] ? 'active' : ''}">
        <g:link action="record" id="${params.id}">Historial de solicitudes</g:link>
    </li>
</ul>
