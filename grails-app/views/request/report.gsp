<g:applyLayout name="twoColumns">
	<head>
		<title>Reporte por departamento</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
	</head>

	<content tag="main">
		<g:render template="reportNavBar"/>

		<g:each in="${results}" var="year">
			<h4>${year.key}</h4>

			<g:each in="${year.value}" var="month">
				<h5>${month.key}</h5>

				<table class="table">
					<colgroup>
						<col span="1" style="width: 25%;">
						<col span="1" style="width: 75%;">
					</colgroup>
					<g:each in="${month.value.sort{ -it.value }}" var="school">
						<tr>
							<td style="border:0;">
								<g:link action="detail" params="[y:year.key, m:month.key, s:school.key]">
									${school.key}
								</g:link>
							</td>
							<td style="border:0;">
								<div style="width:${school.value}px;" class="bar-chart">${school.value}</div>
							</td>
						</tr>
					</g:each>
				</table>
			</g:each>
		</g:each>
	</content>
</g:applyLayout>
