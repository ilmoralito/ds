<g:hasErrors bean="${flash.errors}">
    <div class="alert alert-info">
        <ul style="margin-bottom: 0;">
            <g:eachError bean="${flash.errors}">
                <li>
                    <g:message error="${it}"/>
                </li>
            </g:eachError>
        </ul>
    </div>
</g:hasErrors>
