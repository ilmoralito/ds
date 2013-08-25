<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Solicitud</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>

	<g:link action="updateStatus" params="[id:req.id, path:'show']" class="btn pull-right">
		<ds:requestStatus status="${req.status}"/>
	</g:link>

	<br><br>

	<table class="table">
		<tr>
			<td>Por</td>
			<td>${req?.user?.fullName}</td>
		</tr>
		<tr>
			<td>Fecha</td>
			<td><g:formatDate format="yyyy-MM-dd" date="${req?.dateOfApplication}"/></td>
		</tr>
		<tr>
			<td>Aula</td>
			<td>${req?.classroom}</td>
		</tr>
		<tr>
			<td>Facultad</td>
			<td>${req?.school}</td>
		</tr>
		<tr class="${(req?.internet) ? 'info' : 'no-class'}">
			<td>Internet</td>
			<td>${(req?.internet) ? "Si" : "No"}</td>
		</tr>
		<tr class="${(req?.audio) ? 'info' : 'no-class'}">
			<td>Audio</td>
			<td>${(req?.audio) ? "Si" : "No"}</td>
		</tr>
		<tr class="${(req?.screen) ? 'info' : 'no-class'}">
			<td>Pantalla</td>
			<td>${(req?.screen) ? "Si" : "No"}</td>
		</tr>
		<tr class="${(req?.description) ? 'info' : 'no-class'}">
			<td>Observacion</td>
			<td>${req?.description}</td>
		</tr>
		<tr>
			<td>Bloque(s)</td>
			<td>${req?.hours.block.collect { it + 1 }}</td>
		</tr>
	</table>
</body>
</html>