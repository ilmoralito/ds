<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="public">
	<title>Login</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-collapse, login"/>
</head>
<body>
	<g:form action="login" autocomplete="off" style="padding-top:50px;">
		<g:textField name="email" value="${params?.email}" class="span4" placeholder="Email"/>
		<g:passwordField name="password" class="span4" placeholder="Clave"/>
		<g:submitButton name="send" value="Entrar" class="btn btn-primary"/>
	</g:form>
</body>
</html>