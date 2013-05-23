<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Mostar solicitud</title>
	<r:require modules="bootstrap-css, app"/>
</head>
<body>
	<g:hasErrors bean="${req}">
		<g:renderErrors bean="${req}"/>
	</g:hasErrors>

	<g:form action="update">
		<g:hiddenField name="id" value="${params?.id}"/>
		<g:render template="form"/>
		<g:submitButton name="send" value="Actualizar" class="btn"/>
	</g:form>
</body>
</html>