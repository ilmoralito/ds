<g:applyLayout name="twoColumns">
    <head>
        <title>Aulas</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, classrooms"/>
    </head>

    <content tag="main">
        <div class="row">
            <div class="span5">
                <p>Aulas de ${user.fullName}</p>
            </div>
            <div class="span5">
                <g:link event="back" class="pull-right">Regresar</g:link>
            </div>
        </div>

        <g:render template="classrooms" model="[classrooms: classrooms]"/>

        <g:javascript>
            window.ajaxURL = "${createLink(controller: 'user', action: 'addingOrRemovingUserCoordinationsOrClassrooms')}"
        </g:javascript>
    </content>
</g:applyLayout>
