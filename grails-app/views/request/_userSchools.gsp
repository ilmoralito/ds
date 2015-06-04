<g:set var="userSchools" value="${session?.user?.refresh()?.schools as List}"/>
<!--
<g:set var="schoolsAndDepartments" value="${grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments}"/>
-->

<label for="school">Coordinacion</label>
<g:select from="${userSchools}" name="school" noSelection="['':'Selecciona coordinacion']" value="${req?.school}" class="input-block"/>
