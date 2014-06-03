<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title><g:layoutTitle default="Solicitud"/></title>
	<r:layoutResources/>
</head>
<body>
	<div class="container main">
		<div class="row">
			<div class="span2">
				<!--orden de reparacion-->
				<div class="well well-small">
					<g:form controller="request" action="activity" class="form-inline">
						<g:textField name="q" value="${params?.q}" class="input-small" placeholder="Disponibilidad"/>
						<button type="submit" class="btn"><i class="icon-search"></i></button>
					</g:form>
				</div>

				<ds:isAdmin>
					<ul class="nav nav-tabs nav-stacked">
						<li><g:link controller="user" action="list">Usuarios</g:link></li>
						<li><g:link controller="request" action="requestsBy" params="[type:'schools']">Reportes</g:link></li>
					</ul>
				</ds:isAdmin>

				<ul class="nav nav-tabs nav-stacked">
					<li class="${(controllerName == 'request') ? 'active' : 'no-active'}"><g:link controller="request" action="list">Solicitudes</g:link></li>
					<li class="${(controllerName == 'user') ? 'active' : 'no-active'}"><g:link controller="user" action="profile">Perfil</g:link></li>
					<ds:isUser>
						<li class="${(!controllerName) ? 'active' : 'no-active'}"><g:link uri="/faqs">Preguntas frecuentes</g:link></li>
					</ds:isUser>
    			</ul>

    			<ul class="nav nav-tabs nav-stacked">
    				<li><g:link controller="user" action="logout">Salir</g:link></li>
    			</ul>
			</div>
			<div class="span10">
				<g:layoutBody/>
				<ds:flashMessage><br><div class="alert alert-info">${flash.message}</div></ds:flashMessage>
			</div>
		</div>
	</div>
	<r:layoutResources/>
</body>
</html>