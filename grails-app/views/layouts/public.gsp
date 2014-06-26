<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title><g:layoutTitle default="ds"/></title>
	<r:layoutResources/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span10 offset1">
				<ul class="app-nav pull-right">
	  	    <li class="${actionName == 'login' ? 'active' : ''}">
	  	    	<g:link controller="user" action="login">Iniciar sesion</g:link>
	  	    </li>
	  	    <li class="${actionName == 'activity' ? 'active' : ''}">
	  	    	<g:link controller="request" action="activity">Actividad de hoy</g:link>
	  	    </li>
	  	    <li class="${request.forwardURI == '/faqs' || request.forwardURI == '/ds/faqs' ? 'active' : ''}">
	  	    	<g:link uri="/faqs">FAQ's</g:link>
	  	    </li>
	    	</ul>
			</div>
		</div>
		<div class="row">
			<g:if test="${actionName == 'login'}">
				<div class="span4 offset4">
			</g:if>
			<g:else>
				<div class="span10 offset1">
			</g:else>
				<g:layoutBody/>
			</div>
		</div>
	</div>
	<r:layoutResources/>
</body>
</html>