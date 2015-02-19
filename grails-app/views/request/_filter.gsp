<label for="users">Usuarios</label>
<select name="users" id="users" multiple="true" class="span2">
	<g:each in="${users}" var="user">
		<option value="${user.email}" ${params.list('users')?.contains(user.email) ? 'selected' : ''}>
			${user.fullName}
		</option>
	</g:each>
</select>

<label for="classrooms">Aulas</label>
<select name="classrooms" id="classrooms" multiple="true" class="span2">
	<g:each in="${classrooms}" var="classroom">
		<option value="${classroom.code}" ${params.list('classrooms')?.contains(classroom.code) ? 'selected' : ''}>
			${classroom.name}
		</option>
	</g:each>
</select>

<label for="schools">Facultades y <a href="#" id="departments">departamentos</a></label>
<select name="schools" id="schools" multiple="true" class="span2">
	<g:each in="${schoolsAndDepartments}" var="school">
		<option value="${school}" ${params.list('schools')?.contains(school) ? 'selected' : ''}>${school}</option>
	</g:each>
</select>

<label for="types">Tipos</label>
<label class="checkbox inline">
	<g:checkBox name="types" value="common" checked="${params?.types?.contains('common')}"/>
	General
</label>

<label class="checkbox inline">
	<g:checkBox name="types" value="express" checked="${params?.types?.contains('express')}"/>
	Expreso
</label>
