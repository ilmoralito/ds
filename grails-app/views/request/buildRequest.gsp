<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="main">
    <title>Crear solicitud</title>
    <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, getUserClassroomsAndSchools"/>
</head>
<body>
    <g:form controller="request" action="storeRequest" autocomplete="off">
        <g:hiddenField name="school" value="${school}"/>
        <g:hiddenField name="dateOfApplication" value="${dateOfApplication}"/>
        <g:hiddenField name="datashow"/>

        <ds:usersBySchool school="${school}"/>

        <ds:userClassrooms selectedClassroom="${params?.classroom}"/>

        <section id="media">
            <label>Medios</label>

            <label class="checkbox inline">
                <g:checkBox name="audio" value="true" checked="false"/> Parlante
            </label>

            <label class="checkbox inline">
                <g:checkBox name="screen" value="true" checked="false"/> Pantalla
            </label>

            <label class="checkbox inline">
                <g:checkBox name="pointer" value="true" checked="false"/> Puntero
            </label>

            <label class="checkbox inline">
                <g:checkBox name="cpu" value="true" checked="false"/> Computadora
            </label>

            <label class="checkbox inline">
                <g:checkBox id="internet" name="internet" value="true" checked="false"/> Internet
            </label>
        </section>

        <div class="form-group">
            <label for="description">Observacion</label>
            <g:textArea name="description" class="form-control input-block-level" style=""/>
        </div>

        <ds:blockWidget blockWidget="${blockWidget}"/>
    </g:form>
</body>
</html>