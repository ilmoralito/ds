<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Actualizar clave</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<g:render template="navbar"/>

	<g:hasErrors bean="${cmd}">
		<small><g:renderErrors bean="${cmd}"/></small>
	</g:hasErrors>

	<g:form action="updatePassword">
		<g:render template="updatePasswordForm"/>
		<br>
		<g:submitButton name="send" value="Confirmar cambios" class="btn btn-primary"/>
	</g:form>
</body>
</html>