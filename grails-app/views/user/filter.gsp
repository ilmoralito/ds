<g:applyLayout name="oneColumnSmall">
    <head>
        <title>Filtrar usuario</title>
        <r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, users"/>
    </head>

    <content tag="main">
        <g:form action="applyFilter">
            <label>Estado</label>
            <label class="checkbox">
                <g:checkBox name="enabled" value="true" checked="${params.list('enabled').contains('true')}"/> Activos
            </label>

            <label class="checkbox">
                <g:checkBox name="enabled" value="false" checked="${params.list('enabled').contains('false')}"/> Inactivos
            </label>

            <ds:rol listOfRoles="${params.list('roles')}"/>

            <ds:coordinations area="schools" list="${params.list('schools')}"/>

            <ds:coordinations area="departments" list="${params.list('departments')}"/>

            <br>
            <button type="submit" class="btn btn-primary">Filtrar</button>
            <g:link action="list" class="btn btn-default">Regresar</g:link>
        </g:form>
    </content>
</g:applyLayout>