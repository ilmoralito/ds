<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Reporte de solictudes</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
	<g:render template="reportNavBar"/>

  <g:if test="${results}">
  	<g:render template="reportsTable"/>
  </g:if>
  <g:else>
  	<h4>...</h4>
  </g:else>
</body>
</html>