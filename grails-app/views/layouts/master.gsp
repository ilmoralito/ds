<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title><g:layoutTitle default="ds"/></title>
	<r:layoutResources/>
	<g:layoutHead/>
</head>
<body>
	<div class="container main">
		<div class="row">
			<g:layoutBody/>
			<ds:flashMessage>${flash.message}</ds:flashMessage>
		</div>
	</div>

	<r:layoutResources/>
</body>
</html>