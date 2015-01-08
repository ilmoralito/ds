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
				<a href="#" id="popover" data-content="Por cada solicitud expreso, el solicitante se compromete a cumplir las condiciones listadas en la seccion <a href='/faqs'>Normas de uso</a>" data-toggle="popover" data-original-title="Sobre solicitudes express" data-placement="bottom">
					<i class="icon-info-sign"></i>
				</a>
			</div>
		</div>
	</g:if>

	<g:form>
		<g:render template="form"/>
		<g:submitButton name="create" value="Crear solicitud" class="btn"/>
	</g:form>

	<g:hasErrors bean="${requestErrors}">
		<g:renderErrors bean="${requestErrors}"/>
	</g:hasErrors>

	<strong>${message}</strong>
</body>
</html>