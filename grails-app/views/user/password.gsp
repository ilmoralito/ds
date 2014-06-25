<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Actualizar clave</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<ul class="nav nav-tabs">
    	<li><g:link action="profile">Perfil</g:link></li>
    	<li class="active"><g:link action="password">Cambiar clave</g:link></li>
    </ul>

	<g:hasErrors bean="${cmd}">
		<small><g:renderErrors bean="${cmd}"/></small>
	</g:hasErrors>

	<g:form action="updatePassword">
		<g:render template="updatePasswordForm"/>
		<br>
		<g:submitButton name="send" value="Confirmar nueva clave" class="btn"/>
	</g:form>
</body>
</html>