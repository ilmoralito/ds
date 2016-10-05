<%@ page contentType="text/html" %>
<p>
    Saludos profesor ${user.fullName} se le ha creado una cuenta
    para administrar las solicitudes de datashow en UCC Leon.
</p>

<p>Dirijase al coordinador o asistente para realizar una solicitud</p>

<p>Hola <b>${user.fullName}</b> le paso los datos de solicitudes de datashow</p>
<p>La direccion del sitio y  credenciales son:</p>
<p>Direccion: <a href="${host}">${host}</a></p>
<p>Email: ${user.email}</p>
<p>Clave por defecto: <b>123</b></p>
<p>Alguna duda estamos por este canal. Gracias por su colaboracion</p>