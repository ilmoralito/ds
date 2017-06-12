<g:if test="${results}">
    <table class="table">
        <colgroup>
            <col span="1" style="width: 45%;">
            <col span="1" style="width: 55%;">
        </colgroup>

        <thead>
            <tr>
                <th>${membershipTag}</th>
                <th>${quantityLabel}</th>
            </tr>
        </thead>

        <tbody>
            <g:each in="${results}" var="data">
                <tr>
                    <td>${data.member}</td>
                    <td>${data.quantity}</td>
                </tr>
            </g:each>
            <tr>
                <td>TOTAL</td>
                <td>${results.quantity.sum()}</td>
            </tr>
        </tbody>
    </table>
</g:if>
<g:else>
    <p>Sin datos que mostrar</p>
</g:else>
