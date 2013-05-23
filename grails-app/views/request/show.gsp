<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Solicitud</title>
	<r:require modules="bootstrap-css, app"/>
</head>
<body>
	<p>
		<p><strong>Fecha de solicitud</strong></p>
		${req?.dateOfApplication}
	</p>

	<p>
		<p><strong>Aula</strong></p>
		${req?.classroom}
	</p>

	<p>
		<p><strong>Facultad</strong></p>
		${req?.school}
	</p>

	<p>
		<p><strong>Observacion</strong></p>
		${req?.description}
	</p>

	<hr>

	<g:link action="enable" params="[id:req.id, path:'show']" class="btn">
		<ds:isRequestEnabled enabled="${req.enabled}"/>
	</g:link>
</body>
</html>