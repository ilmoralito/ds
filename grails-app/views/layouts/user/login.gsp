<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title><g:layoutTitle default="ds"/></title>
	<r:layoutResources/>
</head>
<body>
	<div class="container main">
		<div class="row">
			<div class="span4 offset4">
				<g:layoutBody/>
				<ds:flashMessage><strong>${flash.message}</strong></ds:flashMessage>
			</div>
		</div>
	</div>
	<r:layoutResources/>
</body>
</html>