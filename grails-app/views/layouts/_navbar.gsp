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