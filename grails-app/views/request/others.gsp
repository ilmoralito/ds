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
		<table class="table table-hover">
			<thead>
				<tr>
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
	</g:if>
	<g:else>
		<ds:isAdmin>
			<div class="alert alert-info">
				<strong>nothing.to.show</strong>
			</div>
		</ds:isAdmin>
		<ds:isUser>
			<g:if test="${!ni.edu.uccleon.Request.list().size()}">
				<div class="well">
					<h4>Bienvendio al sistema de solitudes de datashow</h4>
					<p>
						Recuerda porfavor leer la seccion <g:link uri="/faqs">preguntas comunes</g:link>
						para conocer las condiciones del servicio.
					</p>
				</div>
			</g:if>
			<g:else>
				<div class="alert alert-info">
					<strong>nothing.to.show</strong>
				</div>
			</g:else>
		</ds:isUser>
	</g:else>
</body>
</html>