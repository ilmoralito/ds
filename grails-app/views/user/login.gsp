<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="public">
	<title>Login</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css"/>
</head>
<body>
	<g:form action="login">
		<label for="email">Email</label>
		<g:textField name="email" value="${params?.email}" class="span4" autofocus="true"/>

		<label for="password">Clave</label>
		<g:passwordField name="password" class="span4"/>
		<br>
		<g:submitButton name="send" value="Iniciar sesion" class="btn"/>
	</g:form>
</body>
</html>