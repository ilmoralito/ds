<g:set var="userClassrooms" value="${ni.edu.uccleon.User.findByEmail(session?.user?.email).classrooms}"/>
<g:set var="userSchools" value="${ni.edu.uccleon.User.findByEmail(session?.user?.email).schools}"/>
<g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.classrooms}"/>
<g:set var="schoolsAndDepartments" value="${grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments}"/>
<g:set var="allSchoolsAndDepartments" value="${schoolsAndDepartments.schools + schoolsAndDepartments.departments}"/>

<div class="row">
	<div class="span8">
		<label for="dateOfApplication">Fecha de solicitud</label>
		<g:textField name="dateOfApplication" value="${(actionName == 'createRequest' && type == 'express') ? new Date().format('yyyy-MM-dd') : g.formatDate(date:req?.dateOfApplication, format:'yyyy-MM-dd')}" autocomplete="off"/>

		<!--classrooms-->
		<g:if test="${userClassrooms}">
			<g:if test="${userClassrooms.size() > 1}">
				<label for="classroom">Aula</label>
				<g:select from="${userClassrooms}" name="classroom" value="${req?.classroom}"/>
			</g:if>
			<g:else>
				<g:hiddenField name="classroom" value="${userClassrooms}"/>
			</g:else>
		</g:if>
		<g:else>
			<label for="classroom">Aula</label>
			<g:select from="${classrooms}" name="classroom" value="${req?.classroom}"/>
		</g:else>

		<!--schools-->
		<g:if test="${userSchools}">
			<g:if test="${userSchools.size() > 1}">
				<label for="school">A nombre de</label>
				<g:select from="${userSchools}" name="school" value="${req?.school}"/>
			</g:if>
			<g:else>
				<g:hiddenField name="school" value="${userSchools}"/>
			</g:else>
		</g:if>
		<g:else>
			<label for="classroom">A nombre de</label>
			<g:select from="${allSchoolsAndDepartments}" name="school" value="${req?.school}"/>
		</g:else>

		<label for="description">Observacion</label>
		<g:textArea name="description" value="${req?.description}" class="input-block-level"/>
	</div>
	<div class="span2">
		<div class="checkbox"><g:checkBox name="audio" value="${req?.audio}"/> Parlantes</div>
		<div class="checkbox"><g:checkBox name="screen" value="${req?.screen}"/> Pantalla</div>
		<div class="checkbox"><g:checkBox name="internet" value="${req?.internet}"/> Internet</div>
	</div>
</div>