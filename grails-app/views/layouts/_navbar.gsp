<div class="navbar navbar-inverse navbar-static-top">
  <div class="navbar-inner">
    <div class="container">

      <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>

      <a class="brand" href="#">SSMA</a>

      <div class="nav-collapse collapse">
        <ul class="nav pull-right">
          <li class="${actionName == 'login' ? 'active' : ''}">
            <g:link controller="user" action="login">Iniciar sesion</g:link>
          </li>
          <li class="${actionName == 'activity' ? 'active' : ''}">
            <g:link controller="request" action="activity">Solicitudes del dia</g:link>
          </li>
        </ul>
      </div>

    </div>
  </div>
</div>