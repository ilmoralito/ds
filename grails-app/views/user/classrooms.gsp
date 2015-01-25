<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="layout" content="main">
  <title>Aulas</title>
  <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
  <g:render template="navbar"/>
  <g:form action="classrooms">
    <div class="row">
      <g:each in="${allCls}" var="classroom">
        <div class="span2">
          <h4>${classroom.key}</h4>
          <g:each in="${classroom.value}" var="c">
              <label class="checkbox">
                <g:if test="${c in Map}">
                  <g:set var="key" value="${c.keySet().join()}"/>
                  <g:set var="value" value="${c.values().join()}"/>

                  <g:checkBox name="classrooms" value="${key}" checked="${userSchoolsOrDepartments?.contains(key) || user?.classrooms?.contains(key)}"/> ${value}
                </g:if>
                <g:else>
                  <g:checkBox name="classrooms" value="${c}" checked="${userSchoolsOrDepartments?.contains(c) || user?.classrooms?.contains(c)}"/> ${c}
                </g:else>
              </label>
          </g:each>
        </div>
      </g:each>
    </div>

    <g:submitButton name="send" value="Confirmar cambios" class="btn btn-primary"/>
  </g:form>
</body>
</html>