<g:set var="schools" value="${grailsApplication.config.ni.edu.uccleon.schools}"/>

<label for="email">Email</label>
<g:textField name="email" value="${user?.email}" autofocus="true" class="span4"/>

<label for="password">Clave</label>
<g:passwordField name="password" class="span4"/>

<label for="rpassword">Repetir Clave</label>
<g:passwordField name="rpassword" class="span4"/>

<label for="fullName">Nombre completo</label>
<g:textField name="fullName" value="${user?.fullName}" class="input6" class="span4"/>

<label for="schools">Facultades</label>
<g:each in="${schools}" var="school" status="i">
	<label class="checkbox">
		<g:checkBox name="${school}" value="${user?.school}"/>
		${school}
	</label>
</g:each>