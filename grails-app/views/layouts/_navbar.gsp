<div class="navbar navbar-inverse navbar-static-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="#">SSMA</a>

            <g:form controller="login" action="auth" autocomplete="off" class="navbar-form pull-right">
                <input type="email" name="email" id="email" value="${params?.email}" placeholder="nombre.apellido@ucc.edu.ni" />
                <g:passwordField name="password" placeholder="Clave"/>

                <g:submitButton name="send" value="Entrar" class="btn btn-primary"/>
            </g:form>
        </div>
    </div>
</div>
