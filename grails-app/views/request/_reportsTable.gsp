<g:if test="${type == 'resumen'}">
	<table class="table table-hover">
		<tbody>
			<g:each in="${results}" var="year">
				<tr>
					<td colspan="2"><strong>${year.key}</strong></td>
				</tr>
				<g:each in="${year.value}" var="month">
					<tr>
						<td width="1">${month.key}</td>
						<td>${month.value}</td>
					</tr>
				</g:each>
				<tr>
					<td>Total</td>
					<td>${totalRequestInYears[year.key]}</td>
				</tr>
				<tr>
					<td>Promedio</td>
					<td>
						<g:formatNumber number="${totalRequestInYears[year.key] / results[year.key].size()}" type="number" maxFractionDigits="2" roundingMode="HALF_DOWN"/>
					</td>
				</tr>
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
