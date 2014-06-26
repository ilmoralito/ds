<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="public">
	<title>Login</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, appPublicStyle"/>
</head>
<body>
	<g:form action="login">
		<g:textField name="email" value="${params?.email}" class="span4" autofocus="true" placeholder="Email"/>
		<g:passwordField name="password" class="span4" placeholder="Clave"/>
		<br>
		<g:submitButton name="send" value="Entrar" class="btn"/>
	</g:form>
</body>
</html>