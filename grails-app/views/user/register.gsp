<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="user/login">
	<title>Register</title>
	<r:require modules="bootstrap-css, app"/>
</head>
<body>
	<h4>Registrate</h4>

	<g:hasErrors bean="${cmd}">
		<small><g:renderErrors bean="${cmd}"/></small>
	</g:hasErrors>

	<g:form action="register">
		<g:render template="form"/>
		<hr>
		<g:submitButton name="send" value="Confirmar registro" class="btn"/>
		<g:link action="login" class="btn">Ir a Login</g:link>
	</g:form>
</body>
</html>