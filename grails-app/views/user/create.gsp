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

	<g:form action="create">
		<g:hiddenField name="enabled" value="true"/>

		<label for="email">Email</label>
		<g:textField name="email" value="${user?.email}" class="span6" autofocus="true"/>

		<label for="fullName">Nombre completo</label>
		<g:textField name="fullName" value="${user?.email}" class="span6"/>

		<label for="password">Clave de paso</label>
		<g:passwordField name="password"/>

		<label for="schools">Facultade(s)</label>
		<g:each in="${schools}" var="school">
			<label class="checkbox">
					<g:checkBox name="schools" value="${school}" checked="false"/> ${school}
			</label>
		</g:each>

		<br>

		<g:submitButton name="send" value="Guardar" class="btn"/>
	</g:form>
</body>
</html>