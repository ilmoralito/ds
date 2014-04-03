<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Crear usuario</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, app"/>
</head>
<body>
	<g:set var="schools" value="${grailsApplication.config.ni.edu.uccleon.schools}"/>

	<h4>Crear usuario</h4>

	<g:hasErrors bean="${user}">
		<g:renderErrors bean="${user}"/>
	</g:hasErrors>

	<div class="row">
		<g:form action="create" autocomplete="off">
			<div class="span5">
				<g:hiddenField name="enabled" value="true"/>

				<label for="email">Email</label>
				<g:textField name="email" value="${user?.email}" class="span5" autofocus="true"/>

				<label for="fullName">Nombre completo</label>
				<g:textField name="fullName" value="${user?.email}" class="span5"/>
				<g:submitButton name="send" value="Guardar" class="btn"/>
			</div>

			<div class="span5">
				<g:render template="schoolsAndDepartments"/>
			</div>
		</g:form>
	</div>
</body>
</html>