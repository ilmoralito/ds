<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="${(!session?.user) ? 'public' : 'main'}">
	<title>Actividad</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<div class="row">
		<div class="span3 offset3">
			<table class="table table-condensed borderless" style="margin-bottom:5px;">
				<tbody>
					<tr>
						<td width="1">Fecha</td>
						<td>${params?.dateOfApplication}</td>
					</tr>
					<tr>
						<td>Bloque</td>
						<td>${params?.block}</td>
					</tr>
				</tbody>
			</table>
			<g:form action="createRequestFromActivity" class="create-request-from-activity">
				<g:hiddenField name="datashow" value="${params?.datashow}"/>
				<g:hiddenField name="dateOfApplication" value="${params?.dateOfApplication}"/>
				<g:hiddenField name="block" value="${params?.block}"/>

				<!--classrooms-->
				<g:render template="userClassrooms"/>

				<!--schools-->
				<g:render template="userSchools" model="[req:req]"/>

				<g:textArea name="description" value="${req?.description}" class="input-block" style="resize:vertical; max-height:200px;" placeholder="Observacion"/>

				<br>
				<g:submitButton name="send" value="Confirmar" class="btn btn-primary"/>
				<g:link action="activity" params="[dateSelected:params?.dateOfApplication]" class="btn">Regresar</g:link>
			</g:form>
		</div>
	</div>
</body>
</html>