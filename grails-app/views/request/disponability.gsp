<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Actividad</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
	<g:set var="datashows" value="${grailsApplication.config.ni.edu.uccleon.datashows}"/>

	<h4>${requests.size()} solicitudes el: ${(params.q) ?: new Date().format("yyyy-MM-dd")}</h4>
	<div class="row">
		<g:each in="${1..datashows}" var="datashow" status="i">
			<div class="span2">
				<h4>Datashow ${i + 1}</h4>
				<g:each in="${1..blocks}" var="block">
					<g:if test="${requests.find {it.datashow == datashow && it?.hours?.block?.contains(block)}}">
						<div style="padding:5px; margin-top:15px; border:1px solid #DDD; border-radius:3px;">
							<g:findAll in="${requests}" expr="it.datashow == datashow && it?.hours?.block?.contains(block)">
								<small>${it.user.fullName} <br> <strong>${it.classroom}</strong></small>
							</g:findAll>
							<br>
							<small><ds:blockToHour block="${block}" doapp="${day}"/></small>
						</div>
					</g:if>
					<g:else>
						<ds:blockToHour block="${block}" doapp="${day}"/>
					</g:else>
					<br>
				</g:each>
			</div>
		</g:each>
	</div>
</body>
</html>