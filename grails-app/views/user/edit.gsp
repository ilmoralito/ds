<g:applyLayout name="oneColumnSmall">
    <head>
        <title>Editar usuario</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:form action="update" id="${user.id}" autocomplete="off" name="form">
            <g:render template="form"/>

            <g:submitButton name="send" value="Actualizar" class="btn btn-primary"/>
            <g:link action="show" id="${user.id}" class="btn btn-default">Regresar</g:link>

            <g:hasErrors bean="${errors}">
                <p>
                    <g:renderErrors bean="${errors}"/>
                </p>
            </g:hasErrors>
        </g:form>
    </content>
</g:applyLayout>
