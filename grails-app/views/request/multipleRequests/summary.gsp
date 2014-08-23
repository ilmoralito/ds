<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Crear solicitud</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<div class="row">
		<div class="span5 offset2">
			<div class="row">
				<div class="span3">
					<h4>Sumario</h4>
				</div>
				<div class="span2">
					<g:link event="back" class="btn btn-small pull-right">Regresar</g:link>
				</div>
			</div>
			<g:if test="${results}">
				<table class="table">
					<tbody>
						<g:each in="${results}" var="result" status="i">
							<tr>
								<td colspan="3"><b>${result.key.format("yyyy-MM-dd")}</b></td>
							</tr>
							<g:each in="${result.value}" var="value" status="index">
								<tr>
									<td>${value.classroom}</td>
									<td>${value.hours.block}</td>
									<td width="1">
										<g:link event="deleteRequestInstance" params="[dateOfApplication:result.key, index:index]">
											<i class="icon-trash"></i>
										</g:link>
									</td>
								</tr>
							</g:each>
						</g:each>
					</tbody>
				</table>
			</g:if>
		</div>
	</div>
</body>
</html>