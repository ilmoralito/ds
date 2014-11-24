<g:if test="${type == 'resumen'}">
	<table class="table table-hover">
		<thead>
			<th colspan="2">Año</th>
		</thead>
		<tbody>
			<g:each in="${results.keySet()}" var="year">
				<tr>
					<td colspan="2"><strong>${year}</strong></td>
				</tr>
				<g:each in="${results[year].keySet()}" var="month">
					<g:if test="${results[year][month]}">
						<tr>
							<td width="1">${month}</td>
							<td>${results[year][month]}</td>
						</tr>
					</g:if>
				</g:each>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
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
</g:else>
