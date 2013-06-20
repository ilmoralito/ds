<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Actualizar clave</title>
	<r:require modules="bootstrap-css, app"/>
</head>
<body>
	<g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.classrooms}"/>

	<ul class="nav nav-tabs">
    	<li><g:link action="profile">Perfil</g:link></li>
    	<li><g:link action="password">Cambiar clave</g:link></li>
    	<li class="active"><g:link action="schools">Agregar aulas</g:link></li>
    </ul>

	<g:form action="updatePassword">

		<g:submitButton name="send" value="Confirmar nueva clave" class="btn"/>
	</g:form>
</body>
</html>