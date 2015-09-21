<g:form controller="request" action="activity" autocomplete="off">
	<g:textField name="dateSelected" value="${params?.dateSelected}" class="input-small span2" placeholder="Disponibilidad"/>
	<button type="submit" class="btn">Listar</button>
</g:form>

<g:if test="${session?.user?.role in ['coordinador', 'asistente', 'administrativo']}">
	<g:link controller="request" action="create" class="btn btn-primary btn-block" style="margin: -10px 0 10px 0;">
		Crear solicitud
	</g:link>
</g:if>

<ul class="nav nav-tabs nav-stacked">
	<ds:isAdmin>
		<li class="${controllerName == 'user' && !(actionName in ['profile', 'password']) ? 'active' : 'no-active'}">
			<g:link controller="user" action="list">Usuarios</g:link>
		</li>
		<li class="${controllerName == 'request' && actionName in ['requestsBy', 'report', 'detail'] ? 'active' : ''}">
			<g:link controller="request" action="requestsBy" params="[type:'resumen']">Reportes</g:link>
		</li>
	</ds:isAdmin>
	<li class="${(controllerName == 'request' && !(actionName in ['requestsBy', 'report', 'detail'])) ? 'active' : 'no-active'}">
		<g:link controller="request" action="${session?.user?.role == 'admin' ? 'list' : 'listOfPendingApplications'}">Solicitudes</g:link>
	</li>
	<li class="${(controllerName == 'user' && actionName in ['profile', 'password', 'schoolsAndDepartments', 'classrooms']) ? 'active' : 'no-active'}">
		<g:link controller="user" action="profile">Perfil</g:link>
	</li>
	<ds:isAcademic>
		<li class="${actionName == 'coordsAndRooms' ? 'active' : ''}">
			<g:link controller="user" action="admin">Roster</g:link>
		</li>
	</ds:isAcademic>
	<ds:isNotAdmin>
		<li class="${(!controllerName) ? 'active' : 'no-active'}"><g:link uri="/normas">Normas de uso</g:link></li>
	</ds:isNotAdmin>
		<li><g:link controller="logout">Salir</g:link></li>
	</ul>
</ul>
