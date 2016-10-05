<%@ page contentType="text/html" %>
<p>
    Saludos profesor ${user.fullName} le escribo para notificarle que se
    le ha creado una cuenta para administrar las solicitudes de datashow en UCC Leon.
</p>

<p>
    Para crear solicitudes de datashow debera realizarlas atravez del coordinador o
    asistente de la coordinacion.
</p>

<p>
    Para gestionar sus solicitudes puede realizarlo desde la siguiente direccion
    <a href="${host}">${host}</a> usando las credenciales siguientes:
</p>

<p>Email: ${user.email}</p>
<p>Clave por defecto: <b>123</b></p>
<p>Alguna duda estamos por este canal. De nuevo saludos</p>