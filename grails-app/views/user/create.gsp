<g:applyLayout name="fourColumns">
	<head>
		<title>Crear usuario</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, createUser"/>
	</head>

	<content tag="main">
		<g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.cls}"/>

		<g:form action="create" autocomplete="off">
			<h4>Usuario</h4>
			<label for="email"></label>
			<g:textField name="email" value="${user?.email}" class="span3" autofocus="true" placeholder="Email"/>

			<label for="fullName"></label>
			<g:textField name="fullName" value="${user?.fullName}" class="span3" placeholder="Nombre y apellido"/>

			<g:submitButton name="send" value="Guardar y enviar notificacion" class="btn btn-primary"/>

			<g:hasErrors bean="${user}">
				<p><g:renderErrors bean="${user}"/></p>
			</g:hasErrors>
		</g:form>
	</content>

	<content tag="col1">
		<g:render template="schoolsAndDepartments"/>
	</content>

	<content tag="col2">
		<h4>Aulas</h4>
		<g:each in="${classrooms}" var="classroom">
			<details>
				<summary><strong>${classroom.key}</strong></summary>
				<g:each in="${classrooms[classroom.key]}" var="c">
					<label class="checkbox">
						<g:checkBox name="classrooms" value="${c.code}" checked="${userClassrooms?.contains(c.code)}"/> ${c.name ?: c.code}
					</label>
				</g:each>
			</details>
		</g:each>
	</content>
</g:applyLayout>
