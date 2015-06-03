<g:applyLayout name="${session?.user?.role in grailsApplication.config.ni.edu.uccleon.roles[1..-1] ? 'twoColumns' : 'threeColumns'}">
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
		<h4 class="activity-heading">Filtrar por</h4>
		<g:form action="list" autocomplete="off">
			<label for="">Fechas</label>
			<g:textField name="requestFromDate" value="${params?.requestFromDate}" class="span2" placeholder="Desde"/>

			<label for=""></label>
			<g:textField name="requestToDate" value="${params?.requestToDate}" class="span2" placeholder="Hasta"/>

			<g:render template="filter"/>

			<label for="" style="margin-top:7px;">Estado</label>
			<label for="" class="checkbox">
				<g:checkBox name="status" value="pending" checked="${params?.status?.contains('pending')}"/>
				Pendiente
			</label>

			<label for="" class="checkbox">
				<g:checkBox name="status" value="attended" checked="${params?.status?.contains('attended')}"/>
				Atendido
			</label>

			<label for="" class="checkbox">
				<g:checkBox name="status" value="absent" checked="${params?.status?.contains('absent')}"/>
				Ausente
			</label>

			<label for="" class="checkbox">
				<g:checkBox name="status" value="canceled" checked="${params?.status?.contains('canceled')}"/>
				Cancelado
			</label>

			<g:submitButton name="submit" value="Filtrar" class="btn btn-primary btn-block filter-button"/>
		</g:form>
	</content>
</g:applyLayout>
