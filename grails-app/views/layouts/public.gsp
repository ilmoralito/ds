<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title><g:layoutTitle default="ds"/></title>
	<r:layoutResources/>
</head>
<body>
	<div class="navbar navbar-inverse navbar-static-top">
  	<div class="navbar-inner">
    	<div class="container">
    		<a class="brand" href="#">UCC-DS</a>
    			<ul class="nav pull-right">
    				<li class="${actionName == 'login' ? 'active' : ''}">
		  	    	<g:link controller="user" action="login">Iniciar sesion</g:link>
		  	    </li>
		  	    <li class="${actionName == 'activity' ? 'active' : ''}">
		  	    	<g:link controller="request" action="activity">Solicitudes del dia</g:link>
		  	    </li>
		  	    <li class="${request.forwardURI == '/faqs' || request.forwardURI == '/ds/faqs' ? 'active' : ''}">
		  	    	<g:link uri="/normas">Normas de uso</g:link>
		  	    </li>
    			</ul>
    	</div>
  	</div>
  </div>

	<div class="container">
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