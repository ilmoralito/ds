<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Crear solicitud</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>

	<ds:isAnExpressRequest type="${params?.type}">
		<div class="alert alert-info">
			Por cada solicitud expreso, el solicitante
			se compromete a cumplir las condiciones listadas en la seccion
			<g:link uri="/faqs">preguntas comunes</g:link> de este sitio.
		</div>
	</ds:isAnExpressRequest>

	<g:hasErrors bean="${req}">
		<g:renderErrors bean="${req}"/>
	</g:hasErrors>

	<g:form action="create">
		<g:hiddenField name="type" value="${(params.type) ?: 'common'}"/>
		<g:render template="form"/>
		<g:submitButton name="send" value="Crear solicitud" class="btn"/>
	</g:form>

</body>
</html>