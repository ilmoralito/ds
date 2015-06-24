<g:applyLayout name="twoColumns">
	<head>
		<title>Aulas</title>
		<r:require modules="bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
	</head>

	<content tag="main">
		<div class="row">
			<div class="span10">
				<g:link event="back" class="pull-right">Regresar</g:link>
			</div>
		</div>

		<h4>Aulas de ${user.fullName}</h4>

		<g:render template="classrooms" model="[classrooms: classrooms]"/>
	</content>
</g:applyLayout>