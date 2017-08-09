<section>
    <g:form controller="request" action="activity" method="GET" autocomplete="off">
        <g:textField name="date" value="${params?.date}" class="input-small span2" placeholder="Actividad"/>
        <button type="submit" class="btn">Listar</button>
    </g:form>
</section>

<ds:createRequest/>

<section>
    <ul class="nav nav-tabs nav-stacked">
        <li class="${(controllerName == 'request' && !(actionName in ['reportBySchool', 'reportByClassrooms', 'reportByApplicant', 'coordinationReportPerApplicant', 'reportByDatashows', 'reportByBlock', 'reportPerDay', 'reportPerMonth', 'coordinationReportPerMonth', 'resumen', 'reportSummary'])) ? 'active' : 'no-active'}">
            <g:link controller="request" action="${session?.user?.role == 'admin' ? 'list' : 'listOfPendingApplications'}">
                Solicitudes
            </g:link>
        </li>

        <ds:isAdmin>
            <li class="${controllerName == 'user' && !(actionName in ['profile', 'classrooms']) ? 'active' : 'no-active'}">
                <g:link controller="user" action="list">
                    Usuarios
                </g:link>
            </li>
            <li class="${controllerName == 'report' ? 'active' : ''}">
                <g:link controller="report" action="resumen">
                    Reportes
                </g:link>
            </li>
        </ds:isAdmin>

        <ds:isAcademic>
            <li class="${actionName == 'coordsAndRooms' ? 'active' : ''}">
                <g:link controller="user" action="admin">Roster</g:link>
            </li>
        </ds:isAcademic>

        <ds:isSupervisor>
            <li class="${controllerName == 'report' ? 'active' : ''}">
                <g:link controller="report" action="resumen">Reportes</g:link>
            </li>
        </ds:isSupervisor>

        <ds:isCoordinator>
            <li class="${controllerName == 'report' ? 'active' : ''}">
                <g:if test="${session.user.schools.size() > 1}">
                    <g:link controller="report" action="coordinationList">
                        Reporte
                    </g:link>
                </g:if>
                <g:else>
                    <g:link controller="report" action="facultySummary" params="[school: session.user.schools.toArray()[0]]">
                        Reporte
                    </g:link>
                </g:else>
            </li>
        </ds:isCoordinator>

        <li class="${(controllerName == 'user' && actionName in ['profile', 'schoolsAndDepartments', 'classrooms']) ? 'active' : 'no-active'}">
            <g:link controller="user" action="profile">Perfil</g:link>
        </li>

        <ds:isNotAdmin>
            <li class="${(!controllerName) ? 'active' : 'no-active'}">
                <g:link uri="/normas">Normas de uso</g:link>
            </li>
        </ds:isNotAdmin>

        <li><g:link controller="logout" action="getOut">Salir</g:link></li>
    </ul>
</section>
