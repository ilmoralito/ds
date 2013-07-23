<table class="table table-hover">
	<thead>
		<tr>
        	<th><ds:renderTitle title="${params.type}"/></th>
    		<th>Cantidad</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${results}" var="result">
			<tr>
				<td>
					<g:if test="${params.type == 'users'}">
						<g:link action="listBy" params="[email:result.property.email, type:'user']">${result.property.fullName}</g:link>
					</g:if>
					<g:else>
						${result.property}
					</g:else>
				</td>
				<td>${result.count}</td>
			</tr>
		</g:each>
		<tr>
			<td>Total</td>
			<td>${total}</td>
		</tr>
	</tbody>
</table>