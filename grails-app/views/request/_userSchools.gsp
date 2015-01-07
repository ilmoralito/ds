<g:set var="userSchools" value="${session?.user?.refresh()?.schools as List}"/>
<g:set var="schoolsAndDepartments" value="${grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments}"/>
<g:set var="allSchoolsAndDepartments" value="${schoolsAndDepartments.schools + schoolsAndDepartments.departments}"/>

<g:if test="${userSchools}">
	<g:if test="${userSchools.size() > 1}">
		<g:select from="${userSchools}" name="school" noSelection="['':'Selecciona facultad o depart']" value="${req?.school}" class="input-block"/>
	</g:if>
	<g:else>
		<g:hiddenField name="school" value="${userSchools[0]}"/>
	</g:else>
</g:if>
<g:else>
	<g:select from="${allSchoolsAndDepartments}" name="school" value="${req?.school}" class="span5"/>
</g:else>