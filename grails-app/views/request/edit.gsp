<g:applyLayout name="threeColumns">
    <head>
        <title>Editar solicitud</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, bootstrap-tooltip, bootstrap-popover, jquery-ui, datepicker, buildRequest"/>
    </head>

    <content tag="main">
        <g:form controller="request" name="form" action="update" autocomplete="off">
            <g:hiddenField name="dateOfApplication" value="${requestInstance.dateOfApplication.format('yyyy-MM-dd')}"/>
            <g:hiddenField name="school" value="${requestInstance.school}"/>
            <g:hiddenField name="id" value="${requestInstance.id}"/>
            <g:hiddenField name="datashow"/>

            <ds:usersBySchool school="${requestInstance.school}" user="${requestInstance.user}"/>
            <ds:userClassrooms selected="${requestInstance.classroom}" user="${requestInstance.user}"/>

            <ds:blockWidget blockWidget="${blockWidget}" requestInstance="${requestInstance}"/>
        </g:form>
    </content>

    <content tag="col1">
        <div class="form-group">
            <label>Medios</label>

            <label class="checkbox">
                <g:checkBox
                    name="audio"
                    form="form"
                    value="${requestInstance?.audio}"
                    checked="${requestInstance?.audio}"/> Parlante
            </label>

            <label class="checkbox">
                <g:checkBox
                    name="screen"
                    form="form"
                    value="${requestInstance?.screen}"
                    checked="${requestInstance?.screen}"/> Pantalla
            </label>

            <label class="checkbox">
                <g:checkBox
                    name="pointer"
                    form="form"
                    value="${requestInstance?.pointer}"
                    checked="${requestInstance?.pointer}"/> Puntero
            </label>

            <label class="checkbox">
                <g:checkBox
                    name="cpu"
                    form="form"
                    value="${requestInstance?.cpu}"
                    checked="${requestInstance?.cpu}"/> Computadora
            </label>

            <label class="checkbox">
                <g:checkBox
                    id="internet"
                    name="internet"
                    form="form"
                    value="${requestInstance?.internet}"
                    checked="${requestInstance?.internet}"/> Internet
            </label>
        </div>

        <div class="form-group">
            <label for="description">Observacion</label>
            <g:textArea
                name="description"
                form="form"
                value="${requestInstance?.description}"
                class="form-control span2"/>
        </div>
    </content>
</g:applyLayout>
