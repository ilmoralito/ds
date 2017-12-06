<g:applyLayout name="oneColumnSmall">
    <head>
        <title>Usuario</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:form action="update" id="${user.id}" autocomplete="off" name="form">
            <g:render template="form"/>

            <g:submitButton name="send" value="Actualizar" class="btn btn-primary"/>
            <g:link action="user" action="show" id="${user.id}" class="btn btn-default">Regresar</g:link>

            <g:hasErrors bean="${user}">
                <p>
                    <g:renderErrors bean="${user}"/>
                </p>
            </g:hasErrors>
        </g:form>
    </content>
</g:applyLayout>
