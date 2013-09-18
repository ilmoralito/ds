<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Perfil</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
    <g:set var="schools" value="${grailsApplication.config.ni.edu.uccleon.schools}"/>
    <g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.classrooms.sort()}"/>
    <g:set var="userSchools" value="${user?.schools}"/>
    <g:set var="userClassrooms" value="${user?.userClassrooms}"/>

	<ul class="nav nav-tabs">
    	<li class="active"><g:link action="profile">Perfil</g:link></li>
    	<li><g:link action="password">Cambiar clave</g:link></li>
    </ul>

    <g:hasErrors bean="${cmd}">
    	<small><g:renderErrors bean="${cmd}"/></small>
    </g:hasErrors>

    <g:form action="profile">
   		<div class="row">
            <div class="span10">
                <div class="row">
                    <div class="span5">
                        <h4>Datos personales</h4>
                        <label for="email">Correo</label>
                        <g:textField name="email" value="${user?.email}" class="span4" autofocus="true"/>

                        <label for="fullName">Nombre completo</label>
                        <g:textField name="fullName" value="${user?.fullName}" class="span4"/>
                    </div>

                    <div class="span5">
                        <g:submitButton name="send" value="Confirmar cambios" class="btn pull-right"/>
                    </div>
                </div>

                <div class="row">
                    <div class="span5">
                        <h4>Facultades</h4>
                        <g:each in="${schools}" var="school">
                            <label class="checkbox">
                                <g:checkBox name="schools" value="${school}" checked="${userSchools.contains(ni.edu.uccleon.School.findByName(school))}"/> ${school}
                            </label>
                        </g:each>
                    </div>

                    <div class="span5">
                        <h4>Aulas</h4>
                        <g:each in="${classrooms}" var="classroom">
                            <label class="checkbox">
                                <g:checkBox name="classrooms" value="${classroom}" checked="${userClassrooms?.contains(ni.edu.uccleon.UserClassroom.findByClassroom(classroom))}"/> ${classroom}
                            </label>
                        </g:each>
                    </div>
                </div>
            </div>
        </div>
    </g:form>
</body>
</html>