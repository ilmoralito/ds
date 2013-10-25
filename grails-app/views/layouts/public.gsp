<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title><g:layoutTitle default="ds"/></title>
	<r:layoutResources/>
	<style>
		.navbar-fixed-top .navbar-inner, .navbar-static-top .navbar-inner {
    		box-shadow: 0 1px 10px rgba(255, 255, 255, 1);
    		border-top:1px solid orange;
		}

		.navbar .nav > .active > a, .navbar .nav > .active > a:hover, .navbar .nav > .active > a:focus {
	    	background-color: #FFF;
	    	box-shadow: 0 2px 2px rgba(0, 0, 0, 0.1000) inset;
	    	color: #555555;
	    	text-decoration: none;
		}

		.navbar-inner {
		    background-color: #FFF;
		    background-image: linear-gradient(to bottom, #FFF, #FFF);
		    background-repeat: repeat-x;
		    border: 1px solid #FFF;
		    border-radius: 4px 4px 4px 4px;
		    -moz-box-shadow: 0 1px 4px #FFF;
		    -o-box-shadow: 0 1px 4px #FFF;
		    -webkit-box-shadow: 0 1px 4px #FFF;
		    min-height: 40px;
		    padding-left: 20px;
		    padding-right: 20px;
		}
	</style>
</head>
<body>
	<g:render template="/layouts/navbar"/>
	<br><br><br>
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