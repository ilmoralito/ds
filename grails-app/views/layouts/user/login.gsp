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
				<div class="row">
					<div class="span4">
					<g:link uri="/faqs" class="pull-right">Preguntas comunes</g:link>
					</div>
				</div>
				<g:layoutBody/>
				<ds:flashMessage><strong>${flash.message}</strong></ds:flashMessage>
			</div>
		</div>
	</div>
	<r:layoutResources/>
</body>
</html>