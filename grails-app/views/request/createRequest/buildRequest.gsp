<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Crear solicitud</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, getUserClassroomsAndSchools"/>
</head>
<body>
	<g:form>
		<g:render template="form"/>
		<g:submitButton name="create" value="Continuar" class="btn btn-primary"/>
	</g:form>

	<g:hasErrors bean="${requestErrors}">
		<g:renderErrors bean="${requestErrors}"/>
	</g:hasErrors>

	<strong>${message}</strong>
</body>
</html>