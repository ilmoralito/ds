<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title><g:layoutTitle default="Solicitud"/></title>
	<r:layoutResources/>
</head>
<body>
	<div class="container main">
		<div class="row">
			<div class="span2">
				<g:render template="/layouts/sidebar"/>
			</div>
			<div class="span10">
				<g:layoutBody/>
				<ds:flashMessage>${flash.message}</ds:flashMessage>
			</div>
		</div>
	</div>
	<r:layoutResources/>
</body>
</html>