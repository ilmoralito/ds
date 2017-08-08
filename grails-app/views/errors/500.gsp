<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="errors">
    <title><g:if env="development">Grails Runtime Exception</g:if><g:else>Error</g:else></title>
    <r:require modules="bootstrap-css, bootstrap-responsive-css"/>
</head>
<body>
    <h1>whoops... error 500</h1>
    <g:if env="development">
        <g:renderException exception="${exception}" />
    </g:if>
    <g:else>
        <div class="well well-small">
            Ha ocurrido un error en el sistema. Porfavor informa a soporte técnico para poder asistirte
        </div>
    </g:else>
</body>
</html>