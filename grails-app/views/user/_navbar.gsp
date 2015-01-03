<ul class="nav nav-tabs">
	<li class="${actionName == 'profile' ? 'active' : ''}">
		<g:link action="profile">Perfil</g:link>
	</li>
	<li class="${actionName == 'schoolsAndDepartments' ? 'active' : ''}">
		<g:link action="schoolsAndDepartments">Facultades y departamentos</g:link>
	</li>
	<li class="${actionName == 'classrooms' ? 'active' : ''}">
		<g:link action="classrooms">Aulas</g:link>
	</li>
	<li class="${actionName == 'password' ? 'active' : ''}">
		<g:link action="password">Cambiar clave</g:link>
	</li>
</ul>