<g:set var="userClassrooms" value="${ni.edu.uccleon.User.findByEmail(session?.user?.email).userClassrooms.classroom}"/>
<g:set var="userSchools" value="${ni.edu.uccleon.User.findByEmail(session?.user?.email).schools}"/>
<g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.classrooms}"/>
<g:set var="schools" value="${grailsApplication.config.ni.edu.uccleon.schools}"/>

<label for="dateOfApplication">Fecha</label>
<g:textField name="dateOfApplication" value="${req?.dateOfApplication}"/>

<!--classrooms-->
<g:if test="${userClassrooms}">
	<g:if test="${userClassrooms.size() > 1}">
		<label for="classroom">Aula</label>
		<g:select from="${userClassrooms}" name="classroom" value="${req?.classroom}"/>
	</g:if>
	<g:else>
		<g:hiddenField name="classroom" value="${userClassrooms[0]}"/>
	</g:else>
</g:if>
<g:else>
	<label for="classroom">Aula</label>
	<g:select from="${classrooms}" name="classroom" value="${req?.classroom}"/>
</g:else>

<!--schools-->
<g:if test="${userSchools}">
	<g:if test="${userSchools.size() > 1}">
		<label for="school">Aula</label>
		<g:select from="${userSchools}" name="school" value="${req?.school}"/>
	</g:if>
	<g:else>
		<g:hiddenField name="school" value="${userSchools[0]}"/>
	</g:else>
</g:if>
<g:else>
	<label for="classroom">Aula</label>
	<g:select from="${schools}" name="school" value="${req?.school}"/>
</g:else>


<label for="description">Observacion</label>
<g:textArea name="description" value="${req?.description}" class="input-block-level"/>