	<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Solicitudes</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<g:render template="toolbar"/>

	<g:if test="${requests}">
		<br>
		<table class="table">
			<tbody>
				<g:each in="${requests.keySet()}" var="request" status="i">
					<tr>
						<td><b><ds:requestStatus status="${request}"/></b></td>
					</tr>
					<tr>
						<td style="padding:0;">
							<table class="table" style="margin-bottom:0;">
								<g:if test="${i == 0}">
									<thead>
										<th>Fecha de solicitud</th>
										<th>Aula</th>
										<th>Tipo</th>
									</thead>
								</g:if>
								<tbody>
									<g:each in="${requests[request]}" var="r">
										<tr>
											<td style="width:15%;">${r?.dateOfApplication?.format("yyyy-MM-dd")}</td>
											<td style="width:15%;">${r?.classroom}</td>
											<td style="width:60%;">${r?.type}</td>
										</tr>
									</g:each>
								</tbody>
							</table>
						</td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
</body>
</html>