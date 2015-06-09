	<g:applyLayout name="twoColumns">
	<head>
		<title>Detalle</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
	</head>

	<content tag="main">
		<g:render template="toolbar"/>

		<h4>${params.y}</h4>

		<table class="table table-hover">
			<thead>
				<tr>
					<th>Meses</th>
					<th>Pendientes</th>
					<th>Atendidos</th>
					<th>Cancelados</th>
					<th>Sin retirar</th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${results}" var="r">
					<tr>
						<td>${r.key}</td>
						<td>${r.value["Pendiente"]}</td>
						<td>${r.value["Atendido"]}</td>
						<td>${r.value["Cancelado"]}</td>
						<td>${r.value["Sin retirar"]}</td>
					</tr>
				</g:each>
				<tr>
					<td>Totales</td>
					<td>${results.values()["Pendiente"].sum()}</td>
					<td>${results.values()["Atendido"].sum()}</td>
					<td>${results.values()["Cancelado"].sum()}</td>
					<td>${results.values()["Sin retirar"].sum()}</td>
				</tr>
			</tbody>
		</table>
	</content>
</g:applyLayout>
