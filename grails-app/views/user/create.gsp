<g:applyLayout name="oneColumnSmall">
    <head>
        <title>Crear usuario</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, app"/>
    </head>

    <content tag="main">
        <g:form action="save" autocomplete="off" name="form">
            <g:render template="form"/>

            <g:submitButton name="send" value="Confirmar y enviar notificacion" class="btn btn-primary"/>
            <g:link action="user" action="list" class="btn btn-default">Regresar</g:link>

            <g:hasErrors bean="${errors}">
                <p>
                    <g:renderErrors bean="${errors}"/>
                </p>
            </g:hasErrors>
        </g:form>
    </content>
</g:applyLayout>
