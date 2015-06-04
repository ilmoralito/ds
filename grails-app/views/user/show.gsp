<g:applyLayout name="threeColumns">
	<head>
		<title>${user?.fullName}</title>
		<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
	</head>

	<content tag="main">
		<h4>Perfil</h4>
		<p>
			<label for="">Nombre complete</label>
			${user?.fullName}
		</p>

		<p>
			<label for="">Correo</label>
			${user?.email}
		</p>

		<g:if test="${user?.schools}">
			<label for="">Facultades</label>
			<ul>
				<g:each in="${user?.schools}" var="school">
					<li>${school}</li>
				</g:each>
			</ul>
		</g:if>

		<g:if test="${user?.classrooms}">
			<label for="">Aulas</label>
			<ul>
				<g:each in="${user.classrooms}" var="classroom">
					<li>${classroom}</li>
				</g:each>
			</ul>
		</g:if>
	</content>

	<content tag="col1">
		<h4>Administrar</h4>
		<g:form action="updateUserRole">
			<g:hiddenField name="id" value="${user?.id}"/>
			<g:select name="role" from="${grailsApplication.config.ni.edu.uccleon.roles}" value="${user?.role}" class="span2"/>
			<g:submitButton name="send" value="Actualizar" class="btn btn-block btn-warning"/>
		</g:form>

		<g:form action="enableDisableUserAccount">
			<g:hiddenField name="id" value="${user?.id}"/>
			<button type="submit" class="btn btn-default btn-block">Estado actual: <ds:isEnabled status="${user.enabled}"/></button>
		</g:form>

		<g:form action="notification">
			<g:hiddenField name="id" value="${user?.id}"/>
			<button type="submit" class="btn btn-info btn-block">Notificar</button>
		</g:form>

		<g:link action="resetPassword" id="${params?.id}" class="btn btn-warning btn-block">Resetear clave</g:link>
	</content>
</g:applyLayout>
