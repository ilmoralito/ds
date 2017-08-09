<g:applyLayout name="twoColumns">
    <head>
        <title>Lista de coordinaciones</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
    </head>

    <content tag="main">
        <p>Selecciona una coordinacion</p>

        <g:each in="${coordinationList}" var="school">
            <p>
                <g:link action="facultySummary" params="[school: school]">${school}</g:link>
            </p>
        </g:each>
    </content>
</g:applyLayout>
