<g:applyLayout name="twoColumns">
	<head>
		<title>Reporte por departamento</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
	</head>

	<content tag="main">
		<g:render template="reportNavBar"/>

		<div class="row">
			<div class="span10">
				<g:link action="report" params="[type:'report']" fragment="${params?.y}${params?.m}" class="btn">Regresar</g:link>
			</div>
		</div>
		<br>

		<g:if test="${results}">
			<h4>${params?.m} del ${params?.y}, ${params?.s}</h4>

			<table class="table table-hover">
				<thead>
					<tr>
						<th>Solicitado por</th>
						<th>Cantidad</th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${results.sort{ -it.value }}" var="r">
						<tr>
							<td>${r.key}</td>
							<td>${r.value}</td>
						</tr>
					</g:each>
					<tr>
						<td>TOTAL</td>
						<td>
							${results*.value.sum()}
						</td>
					</tr>
				</tbody>
			</table>
		</g:if>
		<g:else>
			<h4>...</h4>
		</g:else>
	</content>
</g:applyLayout>
