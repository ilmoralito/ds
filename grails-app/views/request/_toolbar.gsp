<ds:isNotAdmin>
    <ul class="nav nav-tabs" style="margin-bottom: 10px;">
        <li class="${actionName == 'listOfPendingApplications' ? 'active' : ''}">
            <g:link action="listOfPendingApplications">Pendientes</g:link>
        </li>
        <li class="${actionName in ['userStatistics', 'userStatisticsDetail'] ? 'active' : ''}">
            <g:link action="userStatistics">Resumen</g:link>
        </li>
        <li class="${actionName == 'requestsByCoordination' ? 'active' : ''}">
            <g:if test="${session?.user?.role in ['coordinador', 'asistente']}">
                <g:link action="requestsByCoordination">
                    Actividad
                </g:link>
            </g:if>
        </li>
    </ul>
</ds:isNotAdmin>
