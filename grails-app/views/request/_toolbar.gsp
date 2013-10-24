<div class="row">
	<div class="span10">
		<div class="row">
			<ds:isAdmin>
				<div class="span10">
			</ds:isAdmin>
			<ds:isUser>
				<div class="span5">
			</ds:isUser>
				<g:form action="list" class="form-inline">
					<g:textField name="requestFromDate" value="${params?.requestFromDate}" class="input-small" placeholder="Desde..."/>
					<g:textField name="requestToDate" value="${params?.requestToDate}" class="input-small" placeholder="Hasta..."/>
					<button type="submit" class="btn"><i class="icon-search"></i></button>
				</g:form>
			</div>
			<ds:isUser>
				<div class="span5">
					<div class="pull-right">
						<g:if test="${session?.user?.role != 'admin'}">
							<div class="btn-group">
								<g:link action="list" class="btn ${(actionName == 'list') ? 'active' : ''}">Solicitudes pendientes</g:link>
								<g:link action="others" class="btn ${(actionName == 'others') ? 'active' : ''}">Otras solicitudes</g:link>
							</div>
						</g:if>
						<div class="btn-group">
						    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						    	Crear solicitud
						    	<span class="caret"></span>
						    </a>
						    <ul class="dropdown-menu">
								<li><g:link action="createRequest" params="[type:'express']">Solicitud expreso</g:link></li>
								<li><g:link action="createRequest">Solicitud</g:link></li>
						    </ul>
						</div>
					</div>
				</div>
			</ds:isUser>
		</div>
	</div>
</div>