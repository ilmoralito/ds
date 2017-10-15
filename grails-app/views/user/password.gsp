<g:applyLayout name="twoColumns">
    <head>
        <title>Clave</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
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
    </content>
</g:applyLayout>
