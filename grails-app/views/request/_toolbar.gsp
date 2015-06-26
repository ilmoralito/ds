<div class="row">
	<div class="span10">
		<ds:isNotAdmin>
			<div class="pull-right">
				<g:link action="listOfPendingApplications" class="btn ${actionName == 'listOfPendingApplications' ? 'active' : ''}">
					Pendientes
				</g:link>
				<g:link action="userStatistics" class="btn ${actionName in ['userStatistics', 'userStatisticsDetail'] ? 'active' : ''}">
					Resumen
				</g:link>
				<g:if test="${session?.user?.role in ['coordinador', 'asistente']}">
					<g:link action="requestsByCoordination" class="btn ${actionName == 'requestsByCoordination' ? 'active' : ''}">
						Solicitudes programadas
					</g:link>
				</g:if>
			</div>
		</ds:isNotAdmin>
	</div>
</div>
