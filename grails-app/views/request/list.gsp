<g:applyLayout name="${session?.user?.role == 'user' ? 'twoColumns' : 'threeColumns'}">
	<head>
		<title>Solicitudes</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app, requestList"/>
	</head>

	<content tag="main">
		<g:render template="toolbar"/>
		<g:if test="${requests}">
			<g:form>
				<table class="table table-hover">
					<thead>
						<tr>
							<ds:isAdmin>
								<th width="1">
									<input type="checkbox" name="trigger" id="trigger">
								</th>
							</ds:isAdmin>
							<ds:isAdmin>
								<th colspan="2">${requests.size()} solicitud${(requests.size() > 1 ? 'es' : '') }</th>
							</ds:isAdmin>
							<ds:isUser>
								<th></th>
							</ds:isUser>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${requests}" var="request">
							<g:set var="blocks" value="${request.hours.block.collect{it}}"/>
							<tr>
								<ds:isAdmin>
									<td><g:checkBox name="requests" value="${request.id}" checked="false" class="requests"/></td>
								</ds:isAdmin>
								<td class="td-mini">
									${request.type == "common" ? "General" : "Express"}
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
					<g:actionSubmit value="Atendido" action="updStatus" class="btn"/>
					<g:actionSubmit value="Ausente" action="updStatus" class="btn"/>
					<g:actionSubmit value="Cancelado" action="updStatus" class="btn"/>
				</ds:isAdmin>
			</g:form>
		</g:if>
		<g:else>
			<ds:isAdmin>
				<p>Nada que mostrar</p>
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
						<p>Nada que mostrar</p>
					</div>
				</g:else>
			</ds:isUser>
		</g:else>
	</content>

	<content tag="col1">
		<g:form action="activity" autocomplete="off">
			<g:render template="filter"/>
		</g:form>
	</content>
</g:applyLayout>
