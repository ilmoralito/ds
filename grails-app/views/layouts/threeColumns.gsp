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
	<div class="span8">
		<g:pageProperty name="page.main"/>
		<ds:flashMessage>${flash.message}</ds:flashMessage>
	</div>
	<div class="span2">
		<g:pageProperty name="page.col1"/>
	</div>
</body>
</html>