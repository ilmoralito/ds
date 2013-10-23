<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title><g:layoutTitle default="ds"/></title>
	<r:layoutResources/>
</head>
<body>
	<g:render template="/layouts/navbar"/>
	<br><br><br>
	<div class="container">
		<div class="row">
			<div class="span10 offset2">
				<g:layoutBody/>
			</div>
		</div>
	</div>
	<r:layoutResources/>
</body>
</html>