<table class="table table-hover table-bordered">
    <col width="10%">
    <col width="90%">

    <thead>
        <tr>
            <th>${label}</th>
            <th>Cantidad</th>
        </tr>
    </thead>

    <tbody>
        <g:each in="${dataset.sort { it.key }}" var="data">
            <tr>
                <td>${data.key}</td>
                <td>${data.value}</td>
            </tr>
        </g:each>
    </tbody>
</table>
