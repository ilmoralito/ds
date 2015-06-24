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

      <g:render template="classrooms" model="[classrooms: allCls]"/>
    </g:form>
  </content>
</g:applyLayout>
