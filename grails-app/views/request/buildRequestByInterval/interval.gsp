<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Crear solicitud</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<h4>Selecciona intervalo de fecha</h4>
	<div class="row">
		<div class="span5">
			<g:form autocomplete="off">
				<g:textField name="date" class="span5" placeholder="Fecha"/>
				<g:submitButton name="addDate" value="Agregar" class="btn"/>
			</g:form>
			<g:if test="${datesByDateInterval}">
				<table class="table table-striped">
					<thead>
						<th></th>
						<th width="1"></th>
					</thead>
					<tbody>
						<g:each in="${datesByDateInterval.sort()}" var="date" status="index">
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
				<g:link event="confirmAddByDateInterval" class="btn">Confirmar</g:link>
			</g:if>
		</div>
		<div class="span5">
			<g:form>
				<g:textField name="fromDate" class="span5" placeholder="Desde"/>
				<g:textField name="toDate" class="span5" placeholder="Hasta"/>
				<g:submitButton name="addDatesInterval" value="Agregar intervalo" class="btn"/>
			</g:form>
			<g:if test="${datesInterval}">
				<table class="table table-striped">
					<thead>
						<th></th>
						<th width="1"></th>
					</thead>
					<tbody>
						<g:each in="${datesInterval}" var="date" status="index">
							<tr>
								<td>${date.format("yyyy-MM-dd")}</td>
								<td>
									<g:link event="deleteDateInterval" params="[index:index]">
										<i class="icon-trash"></i>
									</g:link>
								</td>
							</tr>
						</g:each>
					</tbody>
				</table>
				<g:link event="confirmAddInterval" class="btn">Confirmar</g:link>
			</g:if>
		</div>
	</div>
</body>
</html>