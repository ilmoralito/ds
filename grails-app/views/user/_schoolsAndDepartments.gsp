<g:set var="schoolsAndDepartments" value="${grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments}"/>
<g:set var="hideForUsers" value="${session?.user?.role == 'user' ? true : false}"/>

<g:if test="${actionName == 'create'}">
	<p>Coordinaciones</p>
	<g:each in="${schoolsAndDepartments.schools.sort()}" var="school">
    <label class="checkbox">
      <g:checkBox form="form" name="schools" value="${school}" checked="${userSchools?.contains(school) || user?.schools?.contains(school)}"/> ${school}
    </label>
	</g:each>

	<p>Departamentos</p>
	<g:each in="${schoolsAndDepartments.departments.sort()}" var="department">
    <label class="checkbox">
      <g:checkBox form="form" name="schools" value="${department}" checked="${userSchools?.contains(department) || user?.schools?.contains(department)}"/> ${department}
    </label>
	</g:each>
</g:if>
<g:else>
	<div class="row">
		<div class="span5">
			<p>Coordinaciones</p>
			<g:each in="${schoolsAndDepartments.schools.sort()}" var="school">
		    <label class="checkbox">
	        <g:checkBox name="schools" value="${school}" checked="${userSchools?.contains(school) || user?.schools?.contains(school)}" disabled="${hideForUsers}"/> ${school}
		    </label>
			</g:each>
		</div>
		<div class="span5">
			<p>Departamentos</p>
			<g:each in="${schoolsAndDepartments.departments.sort()}" var="department">
		    <label class="checkbox">
	        <g:checkBox name="schools" value="${department}" checked="${userSchools?.contains(department) || user?.schools?.contains(department)}" disabled="${hideForUsers}"/> ${department}
		    </label>
			</g:each>
		</div>
	</div>
</g:else>
