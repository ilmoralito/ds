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
				<div class="span5">
					<g:link event="back" class="btn btn-small pull-right">Regresar</g:link>
				</div>
			</div>
			<div class="row">
				<div class="span5">
					<h4>Sumario</h4>
				</div>
			</div>
			<g:if test="${results}">
				<table class="table">
					<tbody>
						<g:each in="${results}" var="result">
							<!--rDate = requestDate-->
							<g:set var="rDate" value="${result.key.format("yyyy-MM-dd")}"/>
							<tr>
								<td colspan="3">
									<b>${rDate}</b>
								</td>
							</tr>
							<g:each in="${result.value}" var="instance" status="index">
								<tr>
									<td>${instance.classroom}</td>
									<td>${instance.hours}</td>
									<td width="1">
										<g:link event="deleteRequestInstance" params="[rDate:rDate, index:index]">
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