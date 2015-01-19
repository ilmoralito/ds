<g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.cls*.value.flatten()}"/>

<h4>Aulas</h4>
<g:each in="${classrooms}" var="classroom">
	<label class="checkbox">
		<g:if test="${classroom in Map}">
			<g:set var="key" value="${classroom.keySet().join()}"/>
			<g:set var="value" value="${classroom.values().join()}"/>

			<g:checkBox name="classrooms" value="${key}" checked="${userClassrooms?.contains(key) || user?.classrooms?.contains(key)}"/> ${value}
		</g:if>
		<g:else>
			<g:checkBox name="classrooms" value="${classroom}" checked="${userClassrooms?.contains(classroom) || user?.classrooms?.contains(classroom)}"/> ${classroom}
		</g:else>
	</label>
</g:each>