<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Disponibilidad de solicitudes</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
	<h4>${requests.size()} solicitud(es) en la fecha: ${params.q}</h4>
	<g:if test="${requests}">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>Solicitdado por</th>
					<th>Aula</th>
					<th>Facultad</th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${requests}" var="request">
					<tr>
						<td>${request.user.fullName}</td>
						<td>${request.classroom}</td>
						<td>${request.school}</td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
</body>
</html>