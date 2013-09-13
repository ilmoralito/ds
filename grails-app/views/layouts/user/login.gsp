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
						<div class="btn-group pull-right">
					    	<g:link uri="/activity" class="btn btn-link">Actividad de hoy</g:link>
					    	<g:link uri="/faqs" class="btn btn-link">Faqs</g:link>
					    </div>
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