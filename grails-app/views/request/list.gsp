	<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Solicitudes</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
	<div class="row">
		<ds:isUser>
			<div class="span10">
				<div class="pull-right">
					<g:link action="create" params="[type:'express']" class="btn">Crea una solicitud expreso</g:link>
					<g:link action="create" class="btn">Crea una solicitud</g:link>
				</div>
				<br><br>
			</div>
		</ds:isUser>
	</div>
	<g:if test="${requests}">
		<table class="table table-hover">
			<thead>
				<tr>
					<ds:isAdmin>
						<th><i class="icon-comment"></i></th>
						<th>Solicitado</th>
						<th>Tipo</th>
					</ds:isAdmin>
					<ds:isUser>
						<th>Fecha</th>
					</ds:isUser>
					<th>Aula</th>
					<th>Data</th>
					<th>Bloque</th>
					<ds:isAdmin>
						<th></th>
					</ds:isAdmin>
					<ds:isUser>
						<th></th>
						<th></th>
					</ds:isUser>
				</tr>
			</thead>
			<tbody>
				<g:each in="${requests}" var="request">
					<tr>
						<ds:isAdmin>
							<td class="td-mini">
								<ds:isTrue enabled="${request.description || request.internet || request.audio || request.screen}">
									<g:link action="show" id="${request.id}"><i class="icon-comment"></i></g:link>
								</ds:isTrue>
							</td>
							<td>Por <strong>${request.user.fullName}</strong> en <strong>${request.classroom}</strong></td>
							<td>${request.type}</td>
						</ds:isAdmin>
						<ds:isUser>
							<td><g:formatDate format="yyyy-MM-dd" date="${request.dateOfApplication}"/></td>
						</ds:isUser>
						<td>${request.classroom}</td>
						<td>${request.datashow}</td>
						<td>
							<!--TODO:create a tag lib for this script-->
							<g:if test="${request.hours.size() == 1}">
								${request.hours.block[0]}
							</g:if>
							<g:else>
								<g:each in="${request.hours.block}" var="hour" status="i">
									<g:if test="${i != request.hours.size() - 1}">
										${hour},
									</g:if>
									<g:else>
										${hour}
									</g:else>
								</g:each>
							</g:else>
						</td>
						<td></td>
						<ds:isAdmin>
							<td class="td-mini">
								<g:link action="updateStatus" id="${request.id}">
									<ds:requestStatus status="${request.status}"/>
								</g:link>
							</td>
						</ds:isAdmin>
						<ds:isUser>
							<td class="td-mini"><g:link action="edit" id="${request.id}"><i class="icon-pencil"></i></g:link></td>
							<td class="td-mini"><g:link action="delete" id="${request.id}"><i class="icon-trash"></i></g:link></td>
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
			<div class="well">
				<h4>Bienvendio al sistema de solitudes de datashow</h4>
				<p>
					Recuerda porfavor leer la seccion <g:link uri="/faqs">preguntas comunes</g:link>
					para conocer las condiciones del servicio.
				</p>
			</div>
		</ds:isUser>
	</g:else>
</body>
</html>