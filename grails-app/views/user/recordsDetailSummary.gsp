<g:applyLayout name="twoColumns">
    <head>
        <title>Historial sumario</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <g:render template="nav"/>

        <table class="table table-hover table-bordered">
            <col width="5%">
            <col width="5%">
            <col width="20%">
            <col width="39%">
            <col width="12%">
            <col width="13%">
            <col width="5%">

            <thead>
                <tr>
                    <th style="text-align: center;">Dia</th>
                    <th>Aula</th>
                    <th>Requerimientos</th>
                    <th>Comentario</th>
                    <th>Estado</th>
                    <th>Bloques</th>
                    <th></th>
                </tr>
            </thead>

            <tbody>
                <g:each in="${dataset}" var="data">
                    <g:each in="${data.dataset}" var="request" status="i">
                        <tr>
                            <g:if test="${i == 0}">
                                <td rowspan="${data.dataset.size()}" style="text-align: center; vertical-align: middle;">${data.dayOfMonth}</td>
                            </g:if>
                            <td>${request.classroom}</td>
                            <td>${request.requirements}</td>
                            <td>${request.description}</td>
                            <td>${request.status}</td>
                            <td>${request.blocks}</td>
                            <td>
                                <g:link controller="request" action="show" id="${request.id}">Mostrar</g:link>
                            </td>
                        </tr>
                    </g:each>
                </g:each>
            </tbody>
        </table>
    </content>
</g:applyLayout>
