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
			<g:form autocomplete="off">
				<g:if test="${type == 'dates'}">
					<g:textField name="date" placeholder="Selecciona fecha(s)" class="span5"/>
				</g:if>
				<g:else>
					<g:textField name="fromDate" placeholder="Desde..." class="span5"/>
					<g:textField name="toDate" placeholder="Hasta..." class="span5"/>
				</g:else>

				<g:submitButton name="${type == 'dates' ? 'addDate' : 'addInterval'}" value="Agregar" class="btn"/>
			</g:form>

			<g:if test="${dates}">
				<table class="table table-striped table-hover">
					<thead>
						<th></th>
						<th width="1"></th>
					</thead>
					<tbody>
						<g:each in="${dates}" var="date" status="index">
							<tr>
								<td>${date.format("yyyy-MM-dd")}</td>
								<td>
									<g:link event="deleteDate" params="[index:index]">
										<i class="icon-trash"></i>
									</g:link>
								</td>
							</tr>
						</g:each>
					</tbody>
				</table>

				<g:link event="confirm" class="btn btn-primary">Confirmar</g:link>
			</g:if>
		</div>
	</div>
</body>
</html>