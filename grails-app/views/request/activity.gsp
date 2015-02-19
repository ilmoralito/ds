<g:applyLayout name="${layout}">
	<head>
		<title>Activiades</title>
		<g:set var="mainStyle" value="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app, activity"/>
		<g:set var="activityStyle" value="bootstrap-css, bootstrap-responsive-css, app"/>
		<r:require modules = "${!session?.user ? activityStyle : mainStyle}"/>
	</head>

	<content tag="main">
		<g:set var="dateSelected" value="${dateSelected.format('yyyy-MM-dd')}"/>

		<g:if test="${requests || session?.user?.role == 'user'}">
			<h4 class="activity-heading">${requests.size()} actividades ${dateSelected}</h4>

			<table class="table table-bordered" style="table-layout: fixed;">
				<thead>
					<th width="10">B</th>
					<g:each in="${1..datashows}" var="datashow">
						<th>Datashow ${datashow}</th>
					</g:each>
				</thead>
				<tbody>
					<g:each in="${0..blocks}" var="block">
						<tr>
							<td class="blocks" style="vertical-align:middle;">${block + 1}</td>

							<g:each in="${1..datashows}" var="d">
								<g:set var="req" value="${requests.find { it.datashow == d && block in it.hours.block }}"/>
								<g:set var="justAdded" value="${d == params.int('datashow') && block == params.int('block') ? 'justAdded' : ''}"/>
								<g:set var="currentUser" value="${session?.user && session?.user?.email == req?.user?.email && !justAdded ? 'currentUser' : ''}"/>
								<g:set var="isDraggable" value="${req && req?.user?.email == session?.user?.email}"/>

								<td id="${req?.id}" class="activity ${justAdded} ${currentUser}" draggable="${isDraggable}" ondragstart="${isDraggable ? 'drag(event)' : ''}" ondrop="${!isDraggable && !req ? 'drop(event)' : ''}" ondragover="${!isDraggable && !req ? 'allowDrop(event)' : ''}" ondragend="dragend(event)" data-datashow="${d}" data-block="${block}" data-doa="${dateSelected}">
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
										<g:if test="${req.type == 'common'}">
											<i class="icon-user"></i>
										</g:if>
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
	</content>

	<content tag="col1">
		<g:form action="activity" autocomplete="off">
			<g:hiddenField name="dateSelected" value="${dateSelected}"/>
			<g:render template="filter"/>
		</g:form>
	</content>
</g:applyLayout>
