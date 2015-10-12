<g:applyLayout name="twoColumns">
	<head>
		<title>Profesores</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, roster"/>
	</head>

	<content tag="main">
		<div class="row">
			<div class="span10">
				<g:link event="back" class="pull-right">Regresar</g:link>
			</div>
		</div>

			<h4>Profesores de ${coordination}</h4>
			<table class="table table-hover">
				<colgroup>
					<col span="1" style="width: 5%;">
					<col span="1" style="width: 90%;">
					<col span="1" style="width: 5%;">
				 </colgroup>
				<tbody>
					<tr>
						<td></td>
						<td colspan="2">
							<input id="rosterFilterBox" name="rosterFilterBox" type="text" style="margin-bottom: 0;" placeholder="Filtrar">
						</td>
					</tr>
					<g:each in="${users}" var="user">
						<tr>
							<td style="text-align: center;">
								<g:checkBox name="users" value="${user.email}" checked="${coordination in user.schools}" data-id="${user.id}" data-data="${coordination}" data-flag="coordination"/>
							</td>
							<td class="userFullName">${user.fullName}</td>
							<td width="1">
								<g:link event="classrooms" params="[id:user.id]" class="${coordination in user.schools ? 'show' : 'hide'}">
									Aulas
								</g:link>
							</td>
						</tr>
					</g:each>
				</tbody>
			</table>
	</content>
</g:applyLayout>