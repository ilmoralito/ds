<g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.classrooms.sort()}"/>

<h4>Aulas</h4>
<g:each in="${classrooms}" var="classroom">
	<label class="checkbox">
		<g:checkBox name="classrooms" value="${classroom}" checked="${userClassrooms?.contains(classroom) || user?.classrooms?.contains(classroom)}"/> ${classroom}
	</label>
</g:each>