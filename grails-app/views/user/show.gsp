<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Mostrar Usuario</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
	<h4>Perfil</h4>
	<p>
		<label for="">Nombre complete</label>
		${user?.fullName}
	</p>

	<p>
		<label for="">Correo</label>
		${user?.email}
	</p>

	<g:if test="${user?.schools}">
		<label for="">Facultades</label>
		<ul>
			<g:each in="${user?.schools}" var="school">
				<li>${school}</li>
			</g:each>
		</ul>
	</g:if>

	<g:if test="${user?.classrooms}">
		<label for="">Aulas</label>
		<ul>
			<g:each in="${user.classrooms}" var="classroom">
				<li>${classroom}</li>
			</g:each>
		</ul>
	</g:if>

	<h4>Administrar</h4>
	<g:form action="enableDisableUserAccount">
		<g:hiddenField name="id" value="${user?.id}"/>
		<button type="submit" class="btn btn-default">Estado <ds:isEnabled status="${user.enabled}"/></button>
	</g:form>

	<g:form action="notification">
		<g:hiddenField name="id" value="${user?.id}"/>
		<button type="submit" class="btn btn-default">Volver a notificar</button>
	</g:form>

	<g:link action="resetPassword" id="${params?.id}" class="btn btn-default">Confirmar resetear clave</g:link>
</body>
</html>