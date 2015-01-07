<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="${(!session?.user) ? 'public' : 'main'}">
	<title>Actividad</title>
	<g:set var="mainStyle" value="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
	<g:set var="activityStyle" value="bootstrap-css, bootstrap-responsive-css, app"/>
	<r:require modules = "${!session?.user ? activityStyle : mainStyle}"/>
</head>
<body>
	<g:set var="dateSelected" value="${dateSelected.format('yyyy-MM-dd')}"/>

	<g:if test="${requests || session?.user?.role == 'user'}">
		<h4>Registro de actividades ${dateSelected}</h4>
		<table class="table table-bordered" style="table-layout: fixed;">
			<thead>
				<g:each in="${1..datashows}" var="datashow">
					<th>Datashow ${datashow}</th>
				</g:each>
			</thead>
			<tbody>
				<g:each in="${1..blocks}" var="block">
					<tr>
						<g:each in="${1..datashows}" var="d">
							<td style="overflow: hidden; width:20%; height:50px; position:relative;">
								<g:set var="req" value="${requests.find { it.datashow == d && block in it.hours.block }}"/>
								<g:if test="${req}">
									<g:if test="${session?.user?.role == 'admin'}">
										
									</g:if>
									<p>${req.user.fullName}</p>
									${req.classroom}
								</g:if>
								<g:else>
									<g:if test="${session?.user?.role == 'user'}">
											<g:link action="createRequestFromActivity" params="[dateOfApplication:dateSelected, datashow:d, block:block]" class="pull-right">+</g:link>
									</g:if>
								</g:else>
							</td>
						</g:each>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<p>No hay actividades programadas ${dateSelected}!</p>
	</g:else>
</body>
</html>