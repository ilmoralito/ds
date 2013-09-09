<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Disponibilidad de solicitudes</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
	<h4>${requests.size()} solicitud(es) en la fecha: ${(params.q) ?: new Date().format("yyyy-MM-dd")}</h4>
	<g:if test="${requests}">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>Solicitdado por</th>
					<th>Aula y bloque</th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${requests}" var="request">
					<tr>
						<td>${request.user.fullName} (<small>${request.school}</small>)</td>
						<td>${request.classroom} en el bloque ${request.hours.block}</td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
</body>
</html>