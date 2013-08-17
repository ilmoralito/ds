<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Crear solicitud</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
	<g:hasErrors bean="${req}">
		<g:renderErrors bean="${req}"/>
	</g:hasErrors>

	<g:form>
		<g:render template="form"/>
		<g:submitButton name="create" value="Crear solicitud" class="btn"/>
	</g:form>

	<strong>${message}</strong>
</body>
</html>