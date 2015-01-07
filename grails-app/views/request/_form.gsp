<div class="row">
	<div class="span8">
		<g:hiddenField name="type" value="${req?.type ?: type}"/>

		<label for="dateOfApplication">Fecha de solicitud</label>
		<g:textField name="dateOfApplication" value="${(actionName == 'createRequest' && type == 'express') ? new Date().format('yyyy-MM-dd') : g.formatDate(date:req?.dateOfApplication, format:'yyyy-MM-dd')}" autocomplete="off"/>

		<!--classrooms-->
		<g:render template="userClassrooms" model="[userClassrooms:userClassrooms, req:req]"/>

		<!--schools-->
		<g:render template="userSchools" model="[userSchools:userSchools, req:req]"/>

		<label for="description">Observacion</label>
		<g:textArea name="description" value="${req?.description}" class="input-block-level" style="resize:vertical; max-height:200px;"/>
	</div>
	<div class="span2">
		<div class="checkbox"><g:checkBox name="audio" value="${req?.audio}"/> Parlantes</div>
		<div class="checkbox"><g:checkBox name="screen" value="${req?.screen}"/> Pantalla</div>
		<div class="checkbox"><g:checkBox name="internet" value="${req?.internet}"/> Internet</div>

		<br>
		<small>Administrar aulas, facultades o departamentos desde <g:link controller="user" action="profile">perfil</g:link></small>
	</div>
</div>