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
				<g:form controller="request" action="activity" autocomplete="off" style="margin:0;">
					<div class="input-append">
						<g:textField name="q" value="${params?.q}" class="input-small" placeholder="Disponibilidad"/>
						<button type="submit" class="btn">Listar</button>
					</div>
				</g:form>

				<ds:isUser>
					<div class="btn-group" style="margin:0 0 10px 0;">
					  <button class="btn btn-default">Solicitar datashows</button>
					  <button class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							<span class="caret"></span>
						</button>
					  <ul class="dropdown-menu">
							<li><g:link controller="request" action="createRequest" params="[type:'express']">Expreso</g:link></li>
							<li><g:link controller="request" action="createRequest">General</g:link></li>
					   </ul>
					</div>
				</ds:isUser>

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