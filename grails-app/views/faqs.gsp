<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="${(session?.user) ? 'main' : 'faqs'}">
	<title>Faqs</title>
	<g:set var="mainStyle" value="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
	<g:set var="faqsStyle" value="bootstrap-css, bootstrap-responsive-css"/>
	<r:require modules="${(!session?.user) ? faqsStyle : mainStyle}"/>

</head>
<body>
	<h1>Preguntas comunes</h1>
	<h4>1. ¿Cuanto tiempo tengo para realizar una solicitud?</h4>
	<p>
		Las solicitudes <strong>comunes</strong> se deben realizar con tres dias de anticipacion,
		esta tarea sera realizada por los responsables de datashow.
		Las solicitudes <strong>expresas</strong> se pueden realizar el mismo dia con el compromiso
		que el solicitante debera cumplir con las siguientes normas:

		<ol>
			<li>Retirar el equipo que incluira solamente datashow, extencion y regleta.</li>
			<li>Debera instalar el equipo, en caso de algun inconveniente procurara Soporte Tecnico brindar ayuda.</li>
			<li>Debera regresar el equipo al concluir el bloque para el que se realizo la solicitud.</li>
			<li>El solicitante sera responsable del equipo.</li>
		</ol>
	</p>

	<g:if test="${!session?.user}">
		<g:link uri="/" class="btn">Iniciar sesion</g:link>
	</g:if>
</body>
</html>