<div class="row">
	<div class="span10">
		<ds:isUser>
			<div class="pull-right">
				<g:link action="list" class="btn ${(actionName == 'list') ? 'active' : ''}">Pendientes</g:link>
				<g:link action="others" class="btn ${(actionName == 'others') ? 'active' : ''}">Historial <small><ds:countByStatus status="pending"/></small></g:link>
			</div>
		</ds:isUser>
	</div>
</div>
