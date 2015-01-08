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
				<th width="10">B</th>
				<g:each in="${1..datashows}" var="datashow">
					<th>Datashow ${datashow}</th>
				</g:each>
			</thead>
			<tbody>
				<g:each in="${1..blocks}" var="block">
					<tr>
						<td class="blocks" style="vertical-align:middle;">${block}</td>
						<g:each in="${1..datashows}" var="d">
							<g:set var="req" value="${requests.find { it.datashow == d && block in it.hours.block }}"/>
							<g:set var="justAdded" value="${d == params.int('datashow') && block == params.int('block') ? 'justAdded' : ''}"/>
							<g:set var="currentUser" value="${session?.user && session?.user?.email == req?.user?.email && !justAdded ? 'currentUser' : ''}"/>

							<td class="activity ${justAdded} ${currentUser}">
								<g:if test="${req}">
									<g:if test="${session?.user?.role == 'admin'}">
										<div class="btn-group" style="position:absolute; top:1; right:3%;">
											<a class="dropdown-toggle" data-toggle="dropdown" href="#">
												<span class="caret"></span>
											</a>
											<ul class="dropdown-menu">
												<li>
													<g:link action="updateStatus" params="[id:req.id, path:actionName, status:'attended', dateSelected:dateSelected]">
														Atendido
													</g:link>
												</li>
												<li>
													<g:link action="updateStatus" params="[id:req.id, path:actionName, status:'absent', dateSelected:dateSelected]">
														Ausente
													</g:link>
												</li>
												<li>
													<g:link action="updateStatus" params="[id:req.id, path:actionName, status:'canceled', dateSelected:dateSelected]">
														Cancelado
													</g:link>
												</li>
											</ul>
										</div>
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