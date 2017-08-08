<ul class="nav nav-pills nav-stacked">
    <li class="${params.year == null ? 'active' : ''}">
        <g:link action="${actionName}">Global</g:link>
    </li>

    <g:each in="${yearList}" var="year">
        <li class="${params.int('year') == year ? 'active' : ''}">
            <g:link action="${actionName}" params="[year: year]">${year}</g:link>
        </li>
    </g:each>
</ul>
