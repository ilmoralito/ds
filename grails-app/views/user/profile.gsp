<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Perfil</title>
	<r:require modules="bootstrap-css, app"/>
</head>
<body>
	<ul class="nav nav-tabs">
    	<li class="active"><g:link action="profile">Perfil</g:link></li>
    	<li><g:link action="password">Clave</g:link></li>
    </ul>

    <g:hasErrors bean="${cmd}">
    	<small><g:renderErrors bean="${cmd}"/></small>
    </g:hasErrors>

    <g:form action="profile">
    	<div class="row">
    		<div class="span10"><g:submitButton name="send" value="Confirmar" class="btn pull-right"/></div>
    	</div>

   		<label for="email">Correo</label>
   		<g:textField name="email" value="${user?.email}" autofocus="true"/>

   		<label for="fullName">Nombre completo</label>
   		<g:textField name="fullName" value="${user?.fullName}"/>

   		<label for="schools">Facultades</label>
   		<g:each in="${grailsApplication.config.ni.edu.uccleon.schools}" var="school" status="i">
   			<label class="checkbox">
   				<g:checkBox name="schools.${school}" value="${user.schools.contains(ni.edu.uccleon.School.findByName(school))}"/>
	   			${school}
   			</label>
   		</g:each>

		<label for="classrooms">Agregar aulas</label>
		<g:each in="${grailsApplication.config.ni.edu.uccleon.classrooms}" var="classroom" status="i">
			<label class="checkbox">
				<g:checkBox name="userClassrooms.${classroom}" value="${user.userClassrooms.contains(ni.edu.uccleon.UserClassroom.findByClassroom(classroom))}"/>
				${classroom}
			</label>
		</g:each>
    </g:form>
</body>
</html>