<%@ page contentType="text/html"%>

<p>Saludos</p>

<p>El siguiente es el resumen de actividades de medios audiovisuales por coordinación correspondiente al mes ${monthName} del año ${year}</p>

<table cellspacing="0" cellpadding="0" border="1" width="100%">
    <thead>
        <tr>
            <th width="100">Coordinacion</th>
            <th width="100">Pendientes</th>
            <th width="100">Atendidos</th>
            <th width="100">Sin retirar</th>
            <th width="100">Cancelados</th>
            <th width="100">TOTAL</th>
        </tr>
    </thead>

    <tbody>
        <g:each in="${summary}" var="s">
            <tr>
                <td width="100">${s.school}</td>
                <td width="100">${s.pending}</td>
                <td width="100">${s.attended}</td>
                <td width="100">${s.absent}</td>
                <td width="100">${s.canceled}</td>
                <td width="100">${s.total}</td>
            </tr>
        </g:each>

        <tr>
            <td>TOTAL</td>
            <td>${summary.pending.sum()}</td>
            <td>${summary.attended.sum()}</td>
            <td>${summary.absent.sum()}</td>
            <td>${summary.canceled.sum()}</td>
            <td>${summary.total.sum()}</td>
        </tr>
    </tbody>
</table>

<p>Este mensaje se ha generado automaticamente en la fecha y hora ${new Date().format('yyyy-MM-dd HH:mm')}</p>
