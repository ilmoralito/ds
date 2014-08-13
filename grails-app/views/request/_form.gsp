<g:set var="userClassrooms" value="${ni.edu.uccleon.User.findByEmail(session?.user?.email).classrooms as List}"/>
<g:set var="userSchools" value="${ni.edu.uccleon.User.findByEmail(session?.user?.email).schools as List}"/>
<g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.classrooms}"/>
<g:set var="schoolsAndDepartments" value="${grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments}"/>
<g:set var="allSchoolsAndDepartments" value="${schoolsAndDepartments.schools + schoolsAndDepartments.departments}"/>

<div class="row">
	<div class="span8">
		<g:hiddenField name="type" value="${req?.type ?: type}"/>

		<label for="dateOfApplication">Fecha de solicitud</label>
		<g:textField name="dateOfApplication" value="${(actionName == 'createRequest' && type == 'express') ? new Date().format('yyyy-MM-dd') : g.formatDate(date:req?.dateOfApplication, format:'yyyy-MM-dd')}" autocomplete="off"/>

		<!--classrooms-->
		<g:render template="userClassrooms" model="[userClassrooms:userClassrooms, req:req]"/>
		<span class="help-block">Administrar aulas desde <g:link controller="user" action="profile">perfil</g:link></span>

		<!--schools-->
		<g:render template="userSchools" model="[userSchools:userSchools, req:req]"/>

		<label for="description">Observacion</label>
		<g:textArea name="description" value="${req?.description}" class="input-block-level"/>
	</div>
	<div class="span2">
		<div class="checkbox"><g:checkBox name="audio" value="${req?.audio}"/> Parlantes</div>
		<div class="checkbox"><g:checkBox name="screen" value="${req?.screen}"/> Pantalla</div>
		<div class="checkbox"><g:checkBox name="internet" value="${req?.internet}"/> Internet</div>
	</div>
</div>