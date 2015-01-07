<g:set var="userClassrooms" value="${session?.user?.refresh()?.classrooms as List}"/>
<g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.classrooms}"/>

<g:if test="${userClassrooms}">
	<g:if test="${userClassrooms.size() > 1}">
		<g:select from="${userClassrooms}" name="classroom" noSelection="['':'Selecciona aula']" value="${req?.classroom}" class="input-block"/>
	</g:if>
	<g:else>
		<g:hiddenField name="classroom" value="${userClassrooms[0]}"/>
	</g:else>
</g:if>
<g:else>
	<g:select from="${classrooms}" name="classroom" value="${req?.classroom}"/>
</g:else>