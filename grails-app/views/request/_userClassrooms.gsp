<g:if test="${userClassrooms?.size() > 1}">
	<label for=""></label>
	<g:select from="${userClassrooms}" name="classroom" optionKey="code" optionValue="name" noSelection="['':'Selecciona aula']" value="${req?.classroom}" class="input-block"/>
</g:if>
<g:else>
	<g:hiddenField name="classroom" value="${userClassrooms[0].code}"/>
</g:else>
