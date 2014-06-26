<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Crear usuario</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, app"/>
</head>
<body>
	<div class="row">
		<g:form action="create" autocomplete="off">
			<div class="span4">
				<h4>Usuario</h4>

				<label for="email">Email</label>
				<g:textField name="email" value="${user?.email}" class="span4" autofocus="true"/>

				<label for="fullName">Nombre completo</label>
				<g:textField name="fullName" value="${user?.fullName}" class="span4"/>

				<g:submitButton name="send" value="Guardar y enviar notificacion" class="btn"/>

				<g:hasErrors bean="${user}">
					<p><g:renderErrors bean="${user}"/></p>
				</g:hasErrors>
			</div>

			<div class="span4">
				<g:render template="schoolsAndDepartments"/>
			</div>

			<div class="span2">
				<g:render template="classrooms"/>
			</div>
		</g:form>
	</div>
</body>
</html>