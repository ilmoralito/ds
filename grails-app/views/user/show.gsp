<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Mostrar Usuario</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
	<div class="row">
		<div class="span3">
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

		</div>
		<div class="span5">
			<h4>Actualizar clave</h4>

			<g:hasErrors bean="${cmd}">
				<small><g:renderErrors bean="${cmd}"/></small>
			</g:hasErrors>

			<g:form action="updatePassword">
				<g:render template="updatePasswordForm"/>
				<br>
				<g:submitButton name="send" value="Actualizar" class="btn"/>
			</g:form>

			<h4>Resetear clave</h4>
			<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ut, in, rerum, accusamus incidunt minus similique quia consequatur modi aperiam earum aliquam minima culpa illum nam laudantium error vero ipsa totam!</p>
			<g:link action="resetPassword" id="${params?.id}" class="btn btn-warning">Confirmar resetear clave</g:link>
		</div>
		<div class="span2">
			<h4>Habilitar/deshabilitar cuenta</h4>
			<g:form action="enableDisableUserAccount">
				<g:hiddenField name="id" value="${user?.id}"/>
				<button type="submit" class="btn btn-success btn-block"><ds:isEnabled status="${user.enabled}"/></button>
			</g:form>

			<h4>Notificar</h4>
			<g:form action="notification">
				<g:hiddenField name="id" value="${user?.id}"/>
				<button type="submit" class="btn btn-info btn-block">Notificar</button>
			</g:form>
		</div>
	</div>
</body>
</html>