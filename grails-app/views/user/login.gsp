<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Login</title>
	<r:require modules="bootstrap-css, app"/>
</head>
<body>
	<g:form action="login" autocomplete="off">
		<label for="email">Email</label>
		<g:textField name="email" class="span4" autofocus="true"/>

		<label for="password">Clave</label>
		<g:passwordField name="password" class="span4"/>

		<g:submitButton name="send" value="Iniciar sesion" class="btn"/>
		<g:link action="register" class="btn">Registrate</g:link>
	</g:form>
</body>
</html>