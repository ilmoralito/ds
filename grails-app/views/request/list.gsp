<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Solicitudes</title>
	<r:require modules = "bootstrap-css, app"/>
</head>
<body>
	<div class="row">
		<div class="span10">
			<g:link action="create" class="btn pull-right">Crea una solicitud</g:link>
		</div>
	</div>
	<br>
	<g:if test="${requests}">
		<table class="table table-hover">
			<thead>
				<tr>
					<ds:isAdmin>
						<th><i class="icon-comment"></i></th>
						<th>Solicitado por</th>
					</ds:isAdmin>
					<th>Fecha</th>
					<th>Aula</th>
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
								<ds:isTrue enabled="${request.description}">
									<g:link action="show" id="${request.id}"><i class="icon-comment"></i></g:link>
								</ds:isTrue>
							</td>
							<td>${request.user.fullName}</td>
						</ds:isAdmin>
						<td>${request.dateOfApplication}</td>
						<td>${request.classroom}</td>
						<ds:isAdmin>
							<td class="td-mini">
								<g:link action="enable" id="${request.id}">
									<ds:isRequestEnabled enabled="${request.enabled}"/>
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
		<p>
			<br>
			<strong>nothing.to.show</strong>
		</p>
	</g:else>
</body>
</html>