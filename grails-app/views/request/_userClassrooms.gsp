<g:if test="${userClassrooms}">
	<g:if test="${userClassrooms.size() > 1}">
		<g:select from="${userClassrooms}" name="classroom" noSelection="['':'Escoge aula']" value="${req?.classroom}" class="span2"/>
	</g:if>
	<g:else>
		<g:hiddenField name="classroom" value="${userClassrooms[0]}"/>
	</g:else>
</g:if>
<g:else>
	<g:select from="${classrooms}" name="classroom" value="${req?.classroom}"/>
</g:else>