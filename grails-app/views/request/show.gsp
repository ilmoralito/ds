<g:applyLayout name="twoColumns">
    <head>
        <title>Detalle de solicitud</title>
    <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, requestShow"/>
    </head>

    <content tag="main">
        <section class="clearfix">
            <g:link url="${request.getHeader('referer')}" class="btn">Regresar</g:link>

            <ds:isNotAdmin>
                <div class="clearfix pull-right">
                    <g:link
                        controller="request"
                        action="edit"
                        id="${requestInstance.id}"
                        class="btn btn-primary">Editar</g:link>
                    <a
                        href="#"
                        action="delete"
                        class="btn btn-danger"
                        onclick="if (confirm('Are you sure?')) document.querySelector('#deleteForm').submit();">
                        Eliminar
                    </a>
                </div>
                <g:form name="deleteForm" action="delete" id="${requestInstance.id}">
                    <g:hiddenField name="_method" value="DELETE"/>
                </g:form>
            </ds:isNotAdmin>
        </section>

        <table class="table table-hover">
            <colgroup>
                <col span="1" style="width: 20%;">
                <col span="1" style="width: 80%;">
            </colgroup>
            <tbody>
                <tr>
                    <td colspan="2" style="border-top: 0;">
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
                    <td>
                        <ds:classroom classroomCode="${requestInstance.classroom}"/>
                    </td>
                </tr>
                <tr>
                    <td>Datashow</td>
                    <td>${requestInstance.datashow}</td>
                </tr>
                <tr>
                    <td>Fecha de solicitud</td>
                    <td>${requestInstance.dateOfApplication}</td>
                </tr>

                <g:set var="keys" value="['internet', 'audio', 'screen', 'pointer', 'cpu']" />

                <g:if test="${requestInstance.subMap(keys).any { it.value } }">
                    <tr>
                        <td>Requerimientos</td>
                        <td>
                            <ds:medias
                                Internet="${requestInstance.internet}"
                                Parlantes="${requestInstance.audio}"
                                Pantalla="${requestInstance.screen}"
                                Puntero="${requestInstance.pointer}"
                                Computadora="${requestInstance.cpu}"/>
                        </td>
                    </tr>
                </g:if>

                <tr>
                    <td colspan="2">
                        <strong>Informacion</strong>
                    </td>
                </tr>
                <g:if test="${requestInstance.description}">
                    <tr>
                        <td>Observacion</td>
                        <td>${requestInstance.description}</td>
                    </tr>
                </g:if>
                <tr>
                    <td>Bloques</td>
                    <td>${requestInstance.blocks.tokenize(',').collect { it.toInteger() + 1 }.join(', ')}</td>
                </tr>
                <ds:isAdmin>
                    <tr>
                        <td colspan="2">
                            <strong>Meta</strong>
                        </td>
                    </tr>
                    <tr>
                        <td>Creacion de solicitud</td>
                        <td>${requestInstance.dateCreated}</td>
                    </tr>
                    <tr>
                        <td>Ultima actualizacion</td>
                        <td>${requestInstance.lastUpdated}</td>
                    </tr>
                </ds:isAdmin>
            </tbody>
        </table>
    </content>

    <content tag="col1">

    </content>
</g:applyLayout>
