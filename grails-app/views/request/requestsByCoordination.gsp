<g:applyLayout name="twoColumns">
	<head>
		<title>Solicitudes</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
	</head>

	<content tag="main">
		<g:render template="toolbar"/>
		<g:if test="${results}">
			<br>

			<table class="table table-hover">
				<tbody>
					<g:each in="${results.sort { -it.key }}" var="y">
						<tr>
							<td colspan="2">
								<strong>${y.key}</strong>
							</td>
						</tr>
						<g:each in="${y.value}" var="c">
							<tr>
								<td colspan="2"><strong>${c.key}</strong></td>
							</tr>
							<g:each in="${c.value}" var="m">
								<tr>
									<td width="1">${m.key}</td>
									<td>${m.value}</td>
								</tr>
							</g:each>
						</g:each>
					</g:each>
				</tbody>
			</table>
		</g:if>
		<g:else>
			<h4>Nada que mostrar</h4>
		</g:else>
	</content>
</g:applyLayout>
