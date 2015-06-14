<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Editar solicitud</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, getUserClassroomsAndSchools"/>
</head>
<body>
	<g:hasErrors bean="${req}">
		<g:renderErrors bean="${req}"/>
	</g:hasErrors>

	<g:form>
		<g:render template="form"/>
		<g:submitButton name="confirm" value="Confirmar y continuar" class="btn"/>
	</g:form>

	<strong>${message}</strong>
</body>
</html>