<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main">
    <title>Cambiar clave</title>
    <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
    <g:render template="navbar"/>

    <g:form action="updatePassword">
        <g:hiddenField name="id" value="${session?.user?.id}"/>

        <label for="password">Clave actual</label>
        <g:passwordField name="password"/>

        <label for="npassword">Nueva clave</label>
        <g:passwordField name="npassword"/>

        <label for="rpassword">Repetir nueva clave</label>
        <g:passwordField name="rpassword"/>

        <br>
        <g:submitButton name="send" value="Confirmar" class="btn btn-primary"/>
    </g:form>
</body>
</html>