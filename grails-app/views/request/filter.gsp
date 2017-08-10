<g:applyLayout name="oneColumnSmall">
    <head>
        <title>Filtrar solicitudes</title>
        <r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, users"/>
    </head>

    <content tag="main">
        <g:form action="applyFilter">
            <label for="requestFromDate">Desde</label>
            <g:textField name="requestFromDate" value="${params?.requestFromDate}" class="span8"/>

            <label for="requestToDate">Hasta</label>
            <g:textField name="requestToDate" value="${params?.requestToDate}" class="span8"/>

            <details>
                <summary>Solicitantes</summary>
                <g:each in="${users}" var="user">
                    <label class="checkbox">
                        <g:checkBox name="users" value="${user.email}" checked="false"/> ${user.fullName}
                    </label>
                </g:each>
            </details>

            <details>
                <summary>Aulas</summary>
                <g:each in="${classrooms}" var="classroom">
                    <label class="checkbox">
                        <g:checkBox name="classrooms" value="${classroom.code}" checked="false"/> ${classroom.name}
                    </label>
                </g:each>
            </details>

            <details>
                <summary>Facultades</summary>
                <g:each in="${schools}" var="school">
                    <label class="checkbox">
                        <g:checkBox name="schools" value="${school}" checked="false"/> ${school}
                    </label>
                </g:each>
            </details>

            <details>
                <summary>Departamentos</summary>
                <g:each in="${departments}" var="department">
                    <label class="checkbox">
                        <g:checkBox name="departments" value="${department}" checked="false"/> ${department}
                    </label>
                </g:each>
            </details>

            <details>
                <summary>Estados</summary>
                <g:each in="${requestStatus}" var="status">
                    <label class="checkbox">
                        <g:checkBox
                            name="status"
                            value="${status.english}"
                            checked="${params?.status?.contains(status.english)}"/>
                        ${status.spanish}
                    </label>
                </g:each>
            </details>

            <br>
            <g:submitButton name="submit" value="Filtrar" class="btn btn-primary"/>
            <g:link action="list" class="btn btn-default">Regresar</g:link>
        </g:form>
    </content>
</g:applyLayout>
