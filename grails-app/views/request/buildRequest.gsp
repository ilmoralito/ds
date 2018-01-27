<g:applyLayout name="threeColumns">
    <head>
        <title>Construir solicitud</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, bootstrap-tooltip, bootstrap-popover, jquery-ui, datepicker, buildRequest"/>
    </head>

    <content tag="main">
        <g:form controller="request" name="form" action="storeRequest" autocomplete="off">
            <g:hiddenField name="dateOfApplication" value="${dateOfApplication.format('yyyy-MM-dd')}"/>
            <g:hiddenField name="school" value="${school}"/>
            <g:hiddenField name="datashow"/>

            <ds:usersBySchool school="${school}"/>

            <ds:userClassrooms selected="${params?.classroom}"/>

            <ds:blockWidget blockWidget="${blockWidget}"/>
        </g:form>
    </content>

    <content tag="col1">
        <div class="form-group">
            <label>Medios</label>

            <label class="checkbox">
                <g:checkBox name="audio" form="form" value="true" checked="false"/> Parlante
            </label>

            <label class="checkbox">
                <g:checkBox name="screen" form="form" value="true" checked="false"/> Pantalla
            </label>

            <label class="checkbox">
                <g:checkBox name="pointer" form="form" value="true" checked="false"/> Puntero
            </label>

            <label class="checkbox">
                <g:checkBox name="cpu" form="form" value="true" checked="false"/> Computadora
            </label>

            <label class="checkbox">
                <g:checkBox id="internet" name="internet" form="form" value="true" checked="false"/> Internet
            </label>
        </div>

        <div class="form-group">
            <label for="description">Observacion</label>
            <g:textArea name="description" form="form" class="form-control span2"/>
        </div>
    </content>
</g:applyLayout>
