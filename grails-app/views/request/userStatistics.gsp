	<g:applyLayout name="twoColumns">
	<head>
		<title>Act...</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
	</head>

	<content tag="main">
		<g:if test="${results}">
			<h4>Resumen de solictudes</h4>

			<table class="table">
				<tbody>
					<g:each in="${results}" var="r">
						<tr>
							<td colspan="2">
								<g:link action="userStatisticsDetail" params="[year:r.key]">${r.key}</g:link>
							</td>
						</tr>
						<g:each in="${r.value}" var="v">
							<tr>
								<td width="1">${v.key}</td>
								<td>${v.value}</td>
							</tr>
						</g:each>
					</g:each>
				</tbody>
			</table>
		</g:if>
		<g:else>
			<p>Nada que mostrar</p>
		</g:else>
	</content>
</g:applyLayout>
