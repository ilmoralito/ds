<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main">
    <title>Detalle de solicitud</title>
    <r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
    <section class="clearfix">
        <g:link url="${request.getHeader('referer')}">Regresar</g:link>

        <g:link action="edit" id="${requestInstance.id}" class="btn btn-primary pull-right">
            Editar
        </g:link>
    </section>

    <table class="table table-hover">
        <colgroup>
            <col span="1" style="width: 20%;">
            <col span="1" style="width: 80%;">
        </colgroup>
        <tbody>
            <tr>
                <td colspan="2">
                    <strong>Detalle</strong>
                </td>
            </tr>
            <tr>
                <td>Solicitado por</td>
                <td>${requestInstance.user}</td>
            </tr>
            <tr>
                <td>Coordinacion</td>
                <td>${requestInstance.school}</td>
            </tr>
            <tr>
                <td>Aula</td>
                <td>${requestInstance.classroom}</td>
            </tr>
            <tr>
                <td>Fecha de solicitud</td>
                <td><g:formatDate format="yyyy-MM-dd" date="${requestInstance.dateOfApplication}"/></td>
            </tr>
            <tr>
                <td colspan="2">
                    <strong>Medios</strong>
                </td>
            </tr>
            <tr>
                <td>Internet</td>
                <td>${requestInstance.internet ? 'Si' : 'No'}</td>
            </tr>
            <tr>
                <td>Parlantes</td>
                <td>${requestInstance.audio ? 'Si' : 'No'}</td>
            </tr>
            <tr>
                <td>Pantalla</td>
                <td>${requestInstance.screen ? 'Si' : 'No'}</td>
            </tr>
            <tr>
                <td>Puntero</td>
                <td>${requestInstance.pointer ? 'Si' : 'No'}</td>
            </tr>
            <tr>
                <td>Computadora</td>
                <td>${requestInstance.cpu ? 'Si' : 'No'}</td>
            </tr>
            <tr>
                <td colspan="2">
                    <strong>Informacion</strong>
                </td>
            </tr>
            <tr>
                <td>Observacion</td>
                <td>${requestInstance.description}</td>
            </tr>
            <tr>
                <td>Bloques</td>
                <td>${requestInstance.hours.collect { it.block + 1 }.sort().join(', ')}</td>
            </tr>
            <tr>
                <td colspan="2">
                    <strong>Meta</strong>
                </td>
            </tr>
            <tr>
                <td>Creacion de solicitud</td>
                <td><g:formatDate format="yyyy-MM-dd HH:mm" date="${requestInstance.dateCreated}"/></td>
            </tr>
            <tr>
                <td>Ultima actualizacion</td>
                <td><g:formatDate format="yyyy-MM-dd HH:mm" date="${requestInstance.lastUpdated}"/></td>
            </tr>
        </tbody>
    </table>
</body>
</html>