<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="${(!session?.user) ? 'public' : 'main'}">
	<title>Faqs</title>
	<g:set var="mainStyle" value="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
	<g:set var="faqsStyle" value="bootstrap-css, bootstrap-responsive-css"/>
	<r:require modules="${(!session?.user) ? faqsStyle : mainStyle}"/>
</head>
<body>
	<h4>Tipos de solicitudes y condiciones</h4>
	<p>
		Las solicitudes <strong>comunes</strong> se deben realizar con tres dias de anticipacion.
		Llevar y retirar los equipos sera realizado por los responsables de medios audiovisuales.
		Se disponen de dos equipos para este tipo de solicitud.
	</p>

	<p>
		Las solicitudes <strong>expresas</strong> se pueden realizar el mismo dia y se consta de
		cinco equipos. En este tipo de solicitud el solicitante se compromete a cumplir las siguientes normas:
	
		<ol>
			<li>El solicitante sera responsable del equipo. Implica retitrar, instalar y regresar el equipo personalmente.</li>
			<li>Retirar el equipo que incluira solamente el datashow la extencion y regleta.</li>
		</ol>

		Las solicitudes realizadas <strong>Por fechas</strong> o por <strong>Intervalo</strong> seran concideradas de tipo
		<strong>expreso</strong> y aplican las condiciones listadas anteriormente.
	</p>
</body>
</html>