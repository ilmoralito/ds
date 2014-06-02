<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Perfil</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
  <g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.classrooms.sort()}"/>
  <g:set var="userSchools" value="${user?.schools}"/>
  <g:set var="userClassrooms" value="${user?.classrooms}"/>

	<ul class="nav nav-tabs">
  	<li class="active"><g:link action="profile">Perfil</g:link></li>
  	<li><g:link action="password">Cambiar clave</g:link></li>
  </ul>

  <g:hasErrors bean="${cmd}">
  	<small><g:renderErrors bean="${cmd}"/></small>
  </g:hasErrors>

  <g:form action="profile">
 		<div class="row">
      <div class="span4">
        <h4>Datos personales</h4>
        <label for="email">Correo</label>
        <g:textField name="email" value="${user?.email}" class="span4" autofocus="true"/>

        <label for="fullName">Nombre completo</label>
        <g:textField name="fullName" value="${user?.fullName}" class="span4"/>
        <br>
        <g:submitButton name="send" value="Confirmar cambios" class="btn"/>
      </div>
      <div class="span3">
        <g:render template="schoolsAndDepartments"/>
      </div>
      <div class="span3">
        <h4>Aulas</h4>
        <g:each in="${classrooms}" var="classroom">
          <label class="checkbox">
            <g:checkBox name="classrooms" value="${classroom}" checked="${userClassrooms?.contains(classroom)}"/> ${classroom}
          </label>
        </g:each>
      </div>
    </div>
  </g:form>
</body>
</html>