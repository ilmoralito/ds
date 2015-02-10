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

  <g:form action="profile">
    <label for="email"></label>
    <g:textField name="email" value="${user?.email}" class="span4" autofocus="true" placeholder="Email"/>

    <label for="fullName"></label>
    <g:textField name="fullName" value="${user?.fullName}" class="span4" placeholder="Nombre completo"/>

    <br>
    <g:submitButton name="send" value="Confirmar cambios" class="btn btn-primary"/>
  </g:form>
</body>
</html>