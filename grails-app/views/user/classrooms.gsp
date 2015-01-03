<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="layout" content="main">
  <title>Aulas</title>
  <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
  <g:set var="userClassrooms" value="${user?.classrooms}"/>

  <g:render template="navbar"/>

  <g:form action="classrooms">
    <g:render template="classrooms"/>

    <g:submitButton name="send" value="Confirmar cambios" class="btn"/>
  </g:form>
</body>
</html>