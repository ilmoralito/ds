<g:applyLayout name="${session?.user?.role in grailsApplication.config.ni.edu.uccleon.roles[1..-1] ? 'twoColumns' : 'threeColumns'}">
	<head>
		<title>Solicitudes</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app, requestList"/>
	</head>

	<content tag="main">
		<g:if test="${requestsByBlock}">
			<table class="table">
				<thead>
					<th colspan="3">
						<input type="checkbox" name="trigger" id="trigger">
					</th>
				</thead>
				<tbody>
					<g:each in="${requestsByBlock}" var="b">
						<tr>
							<td colspan="3">Bloque ${b.block + 1}</td>
						</tr>
						<g:each in="${b.requests}" var="r">
							<tr>
								<td width="1">
									<g:checkBox name="requests" value="${r.id}" checked="false" class="requests" form="status"/>
								</td>
								<td>
									<g:if test="${r.description || r.internet || r.audio || r.screen}">
										<g:link action="show" id="${r.id}">
											Por ${r.user.fullName} de ${r.school} en ${r.classroom}
										</g:link>
									</g:if>
									<g:else>
										Por ${r.user.fullName} de ${r.school} en ${r.classroom}
									</g:else>
								</td>
								<td width="1">
									<g:link action="updateStatus" params="[id:r.id, requestFromDate:params?.requestFromDate, requestToDate:params?.requestToDate]">
										<ds:requestStatus status="${r.status}"/>
									</g:link>
								</td>
							</tr>
						</g:each>
					</g:each>
				</tbody>
			</table>

			<g:form action="list" name="status">
				<g:actionSubmit value="Atendido" action="updStatus" class="btn"/>
				<g:actionSubmit value="Ausente" action="updStatus" class="btn"/>
				<g:actionSubmit value="Cancelado" action="updStatus" class="btn"/>
			</g:form>
		</g:if>
		<g:else>
			<p>Nada que mostrar</p>
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
