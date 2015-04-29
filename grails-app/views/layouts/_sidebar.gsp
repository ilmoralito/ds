<g:form controller="request" action="activity" autocomplete="off" style="margin:1;">
	<g:textField name="dateSelected" value="${params?.dateSelected}" class="input-small span2" placeholder="Disponibilidad"/>
	<button type="submit" class="btn">Listar</button>
</g:form>

<ds:isUser>
	<div class="btn-group" style="margin:-10px 0 10px 0;">
	  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
    	Crear solicitud
    <span class="caret"></span>
  	</a>
	  <ul class="dropdown-menu">
			<li><g:link controller="request" action="createRequest" params="[type:'express']">Expreso</g:link></li>
			<li><g:link controller="request" action="createRequest">General</g:link></li>
			<li class=divider></li>
			<li><g:link controller="request" action="multipleRequests" params="[type:'dates']">Fechas</g:link></li>
			<li><g:link controller="request" action="multipleRequests" params="[type:'interval']">Intervalo</g:link></li>
	   </ul>
	</div>
</ds:isUser>

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
		<g:link controller="request" action="list">Solicitudes</g:link>
	</li>
	<li class="${(controllerName == 'user' && actionName in ['profile', 'password', 'schoolsAndDepartments', 'classrooms']) ? 'active' : 'no-active'}">
		<g:link controller="user" action="profile">Perfil</g:link>
	</li>
	<ds:isUser>
		<li class="${(!controllerName) ? 'active' : 'no-active'}"><g:link uri="/normas">Normas de uso</g:link></li>
	</ds:isUser>
		<li><g:link controller="user" action="logout">Salir</g:link></li>
	</ul>
</ul>
