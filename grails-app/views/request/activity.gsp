<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="${(!session?.user) ? 'public' : 'main'}">
	<title>Actividad</title>
	<g:set var="mainStyle" value="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app, activity"/>
	<g:set var="activityStyle" value="bootstrap-css, bootstrap-responsive-css, app"/>
	<r:require modules = "${!session?.user ? activityStyle : mainStyle}"/>
</head>
<body>
	<g:set var="dateSelected" value="${dateSelected.format('yyyy-MM-dd')}"/>

	<g:if test="${requests || session?.user?.role == 'user'}">
		<h4>Registro de actividades ${dateSelected}</h4>
		<table class="table table-condensed borderless">
			<thead>
				<g:each in="${1..datashows}" var="datashow">
					<th>Datashow ${datashow}</th>
				</g:each>
			</thead>
			<tbody>
				<g:each in="${1..blocks}" var="block">
					<tr>
						<g:each in="${1..datashows}" var="d">
							<td>
								<g:set var="req" value="${requests.find { it.datashow == d && block in it.hours.block }}"/>
								<g:if test="${req}">
									<div class="well well-small" style="position:relative;">
										<g:if test="${session?.user?.role == 'admin'}">
											<div class="btn-group" style="position:absolute; top:0; right:0;">
									    	<a class="btn btn-mini dropdown-toggle" data-toggle="dropdown" href="#">
													Aplicar
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
									</div>
								</g:if>
								<g:else>
									<g:if test="${session?.user?.role == 'user'}">
										<div class="well well-small">
											<a href="#" class="btn btn-mini pull-right act">+</a>
											<g:form action="todo" class="create-request-from-activity">
												<g:set var="userClassrooms" value="${ni.edu.uccleon.User.findByEmail(session?.user?.email).classrooms as List}"/>
												<g:set var="userSchools" value="${ni.edu.uccleon.User.findByEmail(session?.user?.email).schools as List}"/>
												<g:set var="classrooms" value="${grailsApplication.config.ni.edu.uccleon.classrooms}"/>
												<g:set var="schoolsAndDepartments" value="${grailsApplication.config.ni.edu.uccleon.schoolsAndDepartments}"/>
												<g:set var="allSchoolsAndDepartments" value="${schoolsAndDepartments.schools + schoolsAndDepartments.departments}"/>

												<g:hiddenField name="datashow" value="${d}"/>
												<g:hiddenField name="dateOfApplication" value="${dateSelected}"/>
												<g:hiddenField name="block" value="${block}"/>

												<!--classrooms-->
												<g:render template="userClassrooms" model="[userClassrooms:userClassrooms, req:req]"/>

												<!--schools-->
												<g:render template="userSchools" model="[userSchools:userSchools, req:req]"/>

												<label for="description">Observacion</label>
												<g:textArea name="description" value="${req?.description}" style="resize:vertical; max-height:200px;"/>

												<br>
												<g:submitButton name="send" value="Confirmar" class="btn btn-primary btn-mini"/>
											</g:form>
										</div>
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