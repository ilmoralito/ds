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
	<g:if test="${actionName == 'activity' && !session?.user}">
		<g:render template="/layouts/navbar"/>
	</g:if>

	<div class="container main">
		<div class="row">
			<g:layoutBody/>
		</div>
	</div>

	<r:layoutResources/>
</body>
</html>