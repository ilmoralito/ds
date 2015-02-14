<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="master"/>
	<title><g:layoutTitle/></title>
	<g:layoutHead/>
</head>
<body>
	<div class="span2">
		<g:render template="/layouts/sidebar"/>
	</div>
	<div class="span3">
		<g:pageProperty name="page.main"/>
	</div>
	<div class="span3">
		<g:pageProperty name="page.col1"/>
	</div>
	<div class="span4">
		<g:pageProperty name="page.col2"/>
	</div>
</body>
</html>