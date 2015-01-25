<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Perfil</title>
  <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
  <g:set var="userSchools" value="${user?.schools}"/>
  <g:set var="userClassrooms" value="${user?.classrooms}"/>

  <g:render template="navbar"/>

  <g:form action="schoolsAndDepartments">
    <g:render template="schoolsAndDepartments"/>

    <g:submitButton name="send" value="Confirmar cambios" class="btn btn-primary"/>
  </g:form>
</body>
</html>