<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Perfil</title>
  <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
  <g:render template="navbar"/>

  <g:hasErrors bean="${cmd}">
  	<small><g:renderErrors bean="${cmd}"/></small>
  </g:hasErrors>

  <div class="row">
    <div class="span5">
      <g:form action="profile">
        <g:textField name="email" value="${user?.email}" class="span5" autofocus="true" placeholder="Email"/>

        <g:textField name="fullName" value="${user?.fullName}" class="span5" placeholder="Nombre completo"/>
        <br>
        <g:submitButton name="send" value="Actualizar perfil" class="btn btn-primary"/>
      </g:form>
    </div>

    <div class="span5">
      <g:form action="updatePassword">
        <g:hiddenField name="id" value="${session?.user?.id}"/>

        <g:passwordField name="password" class="span5" placeholder="Clave actual"/>

        <g:passwordField name="npassword" class="span5" placeholder="Nueva clavel"/>

        <g:passwordField name="rpassword" class="span5" placeholder="Repetir nueva clave"/>
        <br>
        <g:submitButton name="send" value="Confirmar cambio de clave" class="btn btn-primary"/>
      </g:form>
    </div>
  </div>
</body>
</html>