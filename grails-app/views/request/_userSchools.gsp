<g:set var="userSchools" value="${session?.user?.refresh()?.schools as List}"/>

<label for="school">Coordinacion</label>
<g:select
	from="${userSchools}"
	name="school"
	value="${req?.school}"
	class="input-block"/>
