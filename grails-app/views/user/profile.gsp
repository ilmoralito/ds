<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main">
    <title>Perfil</title>
    <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
    <g:render template="navbar"/>

    <div class="row">
        <div class="span5">
            <g:form action="profile" autocomplete="off">
                <label for="fullName">Nombre y apellido</label>
                <g:textField
                    name="fullName"
                    value="${user?.fullName}"
                    class="span5"
                    placeholder="Nombre completo"/>

                <g:submitButton name="send" value="Confirmar" class="btn btn-primary"/>
            </g:form>
        </div>
        <div class="span5">
            <label for="email">Email</label>
            <p>${user?.email}</p>

            <label>Coordinaciones</label>
            <ul>
                <g:each in="${user?.schools}" var="coordination">
                    <li>${coordination}</li>
                </g:each>
            </ul>
        </div>
    </div>
</body>
</html>