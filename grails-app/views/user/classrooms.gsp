<g:applyLayout name="twoColumns">
    <head>
        <title>Aulas</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, adminUserClassrooms"/>
    </head>

    <content tag="main">
        <g:render template="navbar"/>

        <p id="message" style="height: 5px; margin-top: -7px; margin-bottom: 15px;"></p>

        <div class="row">
            <g:each in="${classroomList}" var="code">
                <div class="span2">
                    <label>${code.code}</label>

                    <g:each in="${code.classrooms}" var="classroom">
                        <label class="checkbox">
                            <g:checkBox
                                value="${classroom.code}"
                                checked="${classroom.code in userClassrooms}"
                                data-classroom-code="${classroom.code}"
                                data-user-id="${user.id}"/>
                            ${classroom.name ?: classroom.code}
                        </label>
                    </g:each>
                </div>
            </g:each>
        </div>

        <script>
            window.serverURL = "${grailsApplication.config.grails.serverURL}"
        </script>
    </content>
</g:applyLayout>
