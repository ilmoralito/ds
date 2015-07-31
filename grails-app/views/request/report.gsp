<g:applyLayout name="twoColumns">
	<head>
		<title>Reporte por departamento</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, app"/>
	</head>

	<content tag="main">
		<g:render template="reportNavBar"/>

		<table class="table table-hover">
			<tbody>
					<g:each in="${results.sort {-it.key}}" var="y">
						<tr>
							<td colspan="2" style="border: 0;"><strong>${y.key}</strong></td>
						</tr>

						<g:each in="${y.value}" var="m">
							<tr>
								<td colspan="2"><strong>${m.key}</strong></td>
							</tr>

							<g:each in="${m.value.sort{ -it.value }}" var="s">
								<tr>
									<td style="width:20%;">
										<g:link action="detail" params="[y:y.key, m:m.key, s:s.key]">
											${s.key}
										</g:link>
									</td>
									<td>${s.value}</td>
								</tr>
							</g:each>
						</g:each>
					</g:each>
			</tbody>
		</table>
	</content>
</g:applyLayout>
