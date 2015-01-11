<g:hiddenField name="id" value="${(params?.id) ?: session?.user?.id}"/>
<g:hiddenField name="path" value="${actionName}"/>

<g:passwordField name="password" autofocus="true" class="span4" placeholder="Clave actual"/>

<br>
<g:passwordField name="npassword" class="span4" placeholder="Nueva clavel"/>

<br>
<g:passwordField name="rpassword" class="span4" placeholder="Repetir nueva clave"/>