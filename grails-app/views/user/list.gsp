<g:applyLayout name="threeColumns">
	<head>
		<title>Usuarios</title>
		<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
	</head>

	<content tag="main">
		<g:if test="${users}">
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Usuarios</th>
						<th class="td-mini"></th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${users}" var="user">
						<tr>
							<td><g:link action="show" id="${user.id}">${user.fullName}</g:link></td>
							<td><g:link action="delete" id="${user.id}"><i class="icon-trash"></i></g:link></td>
						</tr>
					</g:each>
				</tbody>
			</table>
		</g:if>
		<g:else>
			<h4>Nada que mostrar</h4>
		</g:else>
	</content>

	<content tag="col1">
		<g:link action="create" class="btn btn-block btn-primary">Crear usuario</g:link>

		<h4>Filtrar por</h4>

		<g:form action="list">
			<label for="fullName">Nombre</label>
			<g:textField name="fullName" value="${params?.fullName}" class="span2" placeholder="Nombre"/>

			<label>Estado</label>
			<label class="checkbox">
				<g:checkBox name="enabled" value="true" checked="${params?.enabled?.contains('true')}"/>
				Activos
			</label>

			<label class="checkbox">
				<g:checkBox name="enabled" value="false" checked="${params?.enabled?.contains('false')}"/>
				Inactivos
			</label>

			<label for="roles">Rol</label>
			<select name="roles" id="roles" multiple="true" class="span2">
				<g:each in="${grailsApplication.config.ni.edu.uccleon.roles}" var="rol">
					<option value="${rol}" ${params?.roles?.contains(rol) ? 'selected' : ''}>${rol}</option>
				</g:each>
			</select>

			<label for="coordinations">Coordinaciones</label>
			<select name="coordinations" id="coordinations" multiple="true" class="span2">
				<g:each in="${coordinations}" var="coordination">
					<option value="${coordination}" ${params?.coordinations?.contains(coordination) ? 'selected' : ''}>
						${coordination}
					</option>
				</g:each>
			</select>

			<label for="departments">Departamentos</label>
			<select name="departments" id="departments" multiple="true" class="span2">
				<g:each in="${departments}" var="department">
					<option value="${department}" ${params?.departments?.contains(department) ? 'selected' : ''}>
						${department}
					</option>
				</g:each>
			</select>

			<button type="submit" class="btn btn-block">Filtrar</button>
		</g:form>
	</content>
</g:applyLayout>
