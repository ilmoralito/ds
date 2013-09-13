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

	<h4>${requests?.size()} solicitudes el ${(params.q) ?: new Date().format("yyyy-MM-dd")}</h4>
	<g:if test="${requests}">
		<div class="row">
			<g:each in="${1..datashows}" var="datashow">
				<div class="span2">
					<h4>Datashow ${datashow}</h4>
					<g:each in="${1..blocks}" var="block">
						<p>
							<g:if test="${requests.find {it.datashow == datashow && it?.hours?.block?.contains(block - 1)}}">
								<g:findAll in="${requests}" expr="it.datashow == datashow && it?.hours?.block?.contains(block - 1)">
									<div class="well well-small">
										<small>
											<strong>Por: ${it.user.fullName}</strong>
											<br>
											<strong>${it.classroom}</strong>
											<br>
											<ds:blockToHour block="${block}" doapp="${day}"/>
										</small>
									</div>
								</g:findAll>
							</g:if>
							<g:else>
								<ds:blockToHour block="${block}" doapp="${day}"/>
							</g:else>
						</p>
					</g:each>
				</div>
			</g:each>
		</div>
	</g:if>
</body>
</html>