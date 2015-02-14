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
    <div class="row">
      <div class="span10">
        <g:submitButton name="send" value="Confirmar cambios" class="btn btn-primary pull-right"/>
      </div>
    </div>

    <g:render template="schoolsAndDepartments"/>
  </g:form>
</body>
</html>