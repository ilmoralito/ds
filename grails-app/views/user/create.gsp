<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Crear usuario</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, app"/>
</head>
<body>
	<g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.cls}"/>

	<div class="row">
		<g:form action="create" autocomplete="off">
			<div class="span4">
				<h4>Usuario</h4>

				<label for="email">Email</label>
				<g:textField name="email" value="${user?.email}" class="span4" autofocus="true"/>

				<label for="fullName">Nombre completo</label>
				<g:textField name="fullName" value="${user?.fullName}" class="span4"/>

				<g:submitButton name="send" value="Guardar y enviar notificacion" class="btn"/>

				<g:hasErrors bean="${user}">
					<p><g:renderErrors bean="${user}"/></p>
				</g:hasErrors>
			</div>

			<div class="span4">
				<g:render template="schoolsAndDepartments"/>
			</div>

			<div class="span2">
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
			</div>
		</g:form>
	</div>
</body>
</html>