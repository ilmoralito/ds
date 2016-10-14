<g:applyLayout name="twoColumns">
    <head>
        <title>Aulas</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="navbar"/>

        <div class="clearfix">
            <g:submitButton name="send" value="Confirmar" form="classrooms" class="btn btn-primary pull-right"/>
        </div>

        <g:form action="classrooms" name="classrooms">
            <g:render template="classrooms" model="[classrooms: allCls, userClassrooms: user?.classrooms]"/>
        </g:form>
    </content>
</g:applyLayout>
