<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
        	<a class="brand" href="/ds">DS</a>
        	<ul class="nav pull-right">
        	    <li class="${actionName == 'login' ? 'active' : ''}"><g:link controller="user" action="login">Iniciar sesion</g:link></li>
        	    <li class="${actionName == 'activity' ? 'active' : ''}"><g:link controller="request" action="activity">Actividad de hoy</g:link></li>
        	    <li class="${request.forwardURI == '/ds/faqs' ? 'active' : ''}"><g:link uri="/faqs">Preguntas comunes</g:link></li>
        	</ul>
        </div>
    </div>
</div>