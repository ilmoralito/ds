<g:set var="schoolsAndDepartments" value="${grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments}"/>

<g:if test="${actionName == 'create'}">
	<h4>Coordinaciones</h4>
	<g:each in="${schoolsAndDepartments.schools.sort()}" var="school">
    <label class="checkbox">
      <g:checkBox form="form" name="schools" value="${school}" checked="${userSchools?.contains(school) || user?.schools?.contains(school)}"/> ${school}
    </label>
	</g:each>

	<h4>Departamentos</h4>
	<g:each in="${schoolsAndDepartments.departments.sort()}" var="department">
    <label class="checkbox">
      <g:checkBox form="form" name="schools" value="${department}" checked="${userSchools?.contains(department) || user?.schools?.contains(department)}"/> ${department}
    </label>
	</g:each>
</g:if>
<g:else>
	<div class="row">
		<div class="span5">
			<h4>Coordinaciones</h4>
			<g:each in="${schoolsAndDepartments.schools.sort()}" var="school">
		    <label class="checkbox">
	        <g:checkBox name="schools" value="${school}" checked="${userSchools?.contains(school) || user?.schools?.contains(school)}"/> ${school}
		    </label>
			</g:each>
		</div>
		<div class="span5">
			<h4>Departamentos</h4>
			<g:each in="${schoolsAndDepartments.departments.sort()}" var="department">
		    <label class="checkbox">
	        <g:checkBox name="schools" value="${department}" checked="${userSchools?.contains(department) || user?.schools?.contains(department)}"/> ${department}
		    </label>
			</g:each>
		</div>
	</div>
</g:else>
