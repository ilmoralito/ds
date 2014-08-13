<g:if test="${userSchools}">
	<g:if test="${userSchools.size() > 1}">
		<g:select from="${userSchools}" name="school" noSelection="['':'Escoge facultad']" value="${req?.school}" class="span2"/>
	</g:if>
	<g:else>
		<g:hiddenField name="school" value="${userSchools[0]}"/>
	</g:else>
</g:if>
<g:else>
	<g:select from="${allSchoolsAndDepartments}" name="school" value="${req?.school}" class="span5"/>
</g:else>