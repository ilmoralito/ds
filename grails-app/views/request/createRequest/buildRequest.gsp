<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Crear solicitud</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<g:if test="${type == 'express'}">
		<div class="row">
			<div class="span10">
				<a href="#" id="popover" data-content="Por cada solicitud expreso, el solicitante se compromete a cumplir las condiciones listadas en la seccion <a href='/faqs'>Preguntas frecuentes</a> de la aplicacion" data-toggle="popover" data-original-title="Sobre solicitudes express" data-placement="bottom">
					<i class="icon-info-sign"></i>
				</a>
			</div>
		</div>
	</g:if>

	<g:hasErrors bean="${req}">
		<g:renderErrors bean="${req}"/>
	</g:hasErrors>

	<g:form>
		<g:render template="form"/>
		<g:submitButton name="create" value="Crear solicitud" class="btn"/>
	</g:form>

	<strong>${message}</strong>
</body>
</html>