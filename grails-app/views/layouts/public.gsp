<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title><g:layoutTitle default="ds"/></title>
	<r:layoutResources/>
</head>
<body>
	<g:render template="/layouts/navbar"/>

	<div class="container">
		<div class="row">
			<g:if test="${actionName == 'login'}">
				<div class="span4 offset4">
			</g:if>
			<g:else>
				<div class="span10 offset1">
			</g:else>
				<g:layoutBody/>

				<g:if test="${flash?.message}">
					<p>${flash.message}</p>
				</g:if>
			</div>
		</div>
	</div>
	<r:layoutResources/>
</body>
</html>