<div class="row">
	<div class="span10">
		<ds:isAdmin>
			<div class="pull-right">
				<g:form action="list" class="form-inline">
					<g:textField name="requestFromDate" value="${params?.requestFromDate}" class="input-small" placeholder="Desde..."/>
					<g:textField name="requestToDate" value="${params?.requestToDate}" class="input-small" placeholder="Hasta..."/>
					<button type="submit" class="btn"><i class="icon-search"></i></button>
				</g:form>
			</div>
		</ds:isAdmin>
		<ds:isUser>
			<div class="pull-right">
				<g:link action="list" class="btn ${(actionName == 'list') ? 'active' : ''}">Pendientes</g:link>
				<g:link action="others" class="btn ${(actionName == 'others') ? 'active' : ''}">Historial <small><ds:countByStatus status="pending"/></small></g:link>
			</div>
		</ds:isUser>
	</div>
</div>
