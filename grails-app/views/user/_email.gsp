<%@ page contentType="text/html" %>

<p>Hola ${authority}</p>

<p>Escribo para notificarte que se creado una cuenta para <b>${fullName}</b> asignandolo a la siguientes coordinaciones y aulas:</p>

<p>Coordinaciones</p>
<ul>
    <g:each in="${schools}" var="school">
        <li>${school}</li>
    </g:each>
</ul>

<p>Aulas</p>
<ul>
    <g:each in="${classrooms}" var="classroom">
        <li>${classroom}</li>
    </g:each>
</ul>

<p>Para algo mas por los canales habituales</p>

<p>Saludos</p>
