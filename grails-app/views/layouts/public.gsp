<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
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
			</div>
		</div>
	</div>
	<r:layoutResources/>
</body>
</html>