<g:hiddenField name="id" value="${(params?.id) ?: session?.user?.id}"/>
<g:hiddenField name="path" value="${actionName}"/>

<label for="password">Clave actual</label>
<g:passwordField name="password"/>

<label for="npassword">Nueva clave</label>
<g:passwordField name="npassword"/>

<label for="rpassword">Repetir nueva clave</label>
<g:passwordField name="rpassword"/>