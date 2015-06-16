<g:applyLayout name="twoColumns">
  <head>
    <title>Aulas</title>
    <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
  </head>

  <content tag="main">
    <g:render template="navbar"/>

    <g:form action="classrooms">
      <div class="row">
        <div class="span10">
          <g:submitButton name="send" value="Confirmar cambios" class="btn btn-primary pull-right"/>
        </div>
      </div>

      <div class="row">
        <g:each in="${allCls}" var="classroom">
          <div class="span2">
            <h4>${classroom.key}</h4>
            <g:each in="${classroom.value}" var="c">
              <label class="checkbox">
                <g:checkBox name="classrooms" value="${c.code}" checked="${userClassrooms?.contains(c.code)}"/> ${c.name ?: c.code}
              </label>
            </g:each>
          </div>
        </g:each>
      </div>
    </g:form>
  </content>
</g:applyLayout>
