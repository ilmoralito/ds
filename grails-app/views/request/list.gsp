	<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Solicitudes</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<g:render template="toolbar"/>
	<g:if test="${requests}">
		<g:form>
			<table class="table table-hover">
				<thead>
					<tr>
						<ds:isAdmin>
							<th></th>
						</ds:isAdmin>
						<th></th>
						<ds:isAdmin>
							<th>${requests.size()} solicitud${(requests.size() > 1 ? 'es' : '') }</th>
						</ds:isAdmin>
						<ds:isUser>
							<th></th>
						</ds:isUser>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${requests}" var="request">
						<g:set var="blocks" value="${request.hours.block.collect{it + 1}}"/>
						<tr>
							<ds:isAdmin>
								<td class="td-mini"><g:checkBox name="requests" value="${request.id}" checked="false"/></td>
							</ds:isAdmin>
							<td class="td-mini">
								<g:if test="${request.type == 'common'}"></g:if>
								<g:else><i class="icon-user"></i></g:else>
							</td>
							<ds:isAdmin>
								<td>
									<g:if test="${request.description || request.internet || request.audio || request.screen}">
										<g:link action="show" id="${request.id}">
											<ds:message request="${request}" blocks="${blocks}"/>
										</g:link>
									</g:if>
									<g:else>
										<ds:message request="${request}" blocks="${blocks}"/>
									</g:else>
								</td>
							</ds:isAdmin>
							<ds:isUser>
								<td>
									<g:link action="editRequest" id="${request.id}">
										<ds:message request="${request}" blocks="${blocks}"/>
									</g:link>
								</td>
							</ds:isUser>
							<ds:isAdmin>
								<td class="td-mini">
									<g:link action="updateStatus" params="[id:request.id, requestFromDate:params?.requestFromDate, requestToDate:params?.requestToDate]">
										<ds:requestStatus status="${request.status}"/>
									</g:link>
								</td>
							</ds:isAdmin>
							<ds:isUser>
								<td class="td-mini">
									<g:if test="${request.status == 'pending'}">
										<g:link action="delete" id="${request.id}"><i class="icon-trash"></i></g:link>
									</g:if>
								</td>
							</ds:isUser>
						</tr>
					</g:each>
				</tbody>
			</table>
			<ds:isAdmin>
				<g:actionSubmit value="Attended" action="updStatus" class="btn"/>
				<g:actionSubmit value="Absent" action="updStatus" class="btn"/>
				<g:actionSubmit value="Canceled" action="updStatus" class="btn"/>
			</ds:isAdmin>
		</g:form>
	</g:if>
	<g:else>
		<ds:isAdmin>
			<div class="alert alert-info">
				<strong>Nada que mostrar</strong>
			</div>
		</ds:isAdmin>
		<ds:isUser>
			<g:if test="${!ni.edu.uccleon.Request.findByUser(session?.user)}">
				<br>
				<h5>Bienvendio al sistema de solitudes de datashow</h5>
				<p>
					Porfavor lee la seccion <g:link uri="/faqs">preguntas comunes</g:link>
					para conocer las condiciones del servicio.
				</p>
			</g:if>
			<g:else>
				<div class="alert alert-info">
					<strong>Nada que mostrar</strong>
				</div>
			</g:else>
		</ds:isUser>
	</g:else>
</body>
</html>