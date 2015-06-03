<div class="row">
	<div class="span10">
		<ds:isUser>
			<div class="pull-right">
				<g:link action="list" class="btn ${actionName == 'list' ? 'active' : ''}">
					Pendientes
				</g:link>
				<g:link action="others" class="btn ${actionName == 'others' ? 'active' : ''}">
					Historial <small><ds:countByStatus status="pending"/></small>
				</g:link>
				<g:if test="${session?.user?.role in grailsApplication.config.ni.edu.uccleon.roles[2..-1]}">
					<g:link action="requestsByCoordination" class="btn ${actionName == 'requestsByCoordination' ? 'active' : ''}">
						Solicitudes programadas
					</g:link>
				</g:if>
			</div>
		</ds:isUser>
	</div>
</div>
