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
    <div class="row">
      <g:each in="${allCls}" var="classroom">
        <div class="span2">
          <h4>${classroom.key}</h4>
          <g:each in="${classroom.value}" var="c">
            <label class="checkbox">
              <g:checkBox name="classrooms" value="${c}" checked="${userClassrooms?.contains(c) || user?.classrooms?.contains(c)}"/> ${c}
            </label>
          </g:each>
        </div>
      </g:each>
    </div>

    <g:submitButton name="send" value="Confirmar cambios" class="btn"/>
  </g:form>
</body>
</html>