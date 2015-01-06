<g:if test="${type == 'resumen'}">
	<g:each in="${results}" var="result">
		<h4>${result.key}</h4>
		<table class="table">
			<tbody>
				<g:each in="${result.value}" var="data">
					<tr>
						<td width="1" style="border:0; vertical-align:middle;">${data.key}</td>
						<td style="border:0;">
							<div style="width:${data.value}px; background:#444; padding:5px; text-align:right; color:#FFF;">${data.value}</div>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>

		<table class="table">
			<tbody>
				<tr>
					<td width="1">Total</td>
					<td>${totalRequestInYears[result.key]}</td>
				</tr>
				<tr>
					<td width="1">Promedio</td>
					<td>
						<g:formatNumber number="${totalRequestInYears[result.key] / results[result.key].size()}" type="number" maxFractionDigits="2" roundingMode="HALF_DOWN"/>
					</td>
				</tr>
			</tbody>
		</table>
	</g:each>
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
