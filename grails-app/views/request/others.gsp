	<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Solicitudes</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<g:render template="toolbar"/>

	<g:if test="${requests['attended']}">
		<h4>Atendidas</h4>
	</g:if>
	<g:render template="othersTable" model="[requests:requests['attended']]"/>

	<g:if test="${requests['absent']}">
		<h4>Ausentes</h4>
	</g:if>
	<g:render template="othersTable" model="[requests:requests['absent']]"/>

	<g:if test="${requests['canceled']}">
		<h4>Canceladas</h4>
	</g:if>
	<g:render template="othersTable" model="[requests:requests['canceled']]"/>
</body>
</html>