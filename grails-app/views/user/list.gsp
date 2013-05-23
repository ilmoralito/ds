<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Usuarios</title>
	<r:require modules="bootstrap-css, app"/>
</head>
<body>
	<g:if test="${users}">
		<div class="row">
			<div class="span10">
				<g:form action="list" class="form-inline pull-right">
					<g:textField name="q" value="${params?.q}" placeholder="Por Nombre o Correo o Facultad"/>
					<button type="submit" class="btn"><i class="icon-search"></i></button>

					<g:link action="list" params="[confirmed:true]" class="btn">Confirmados</g:link>
					<g:link action="list" params="[confirmed:false]" class="btn">No confirmados</g:link>
				</g:form>
			</div>
		</div>

		<small>${users.size()} usuarios listados</small>
		<table class="table table-hover">
			<thead>
				<tr>
					<th class="td-mini"></th>
					<th>Nombre</th>
					<th class="td-mini"></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${users}" var="user">
					<tr>
						<td><ds:isTrue enabled="${user.enabled}"><i class="icon-ok"></i></ds:isTrue></td>
						<td><g:link action="show" id="${user.id}">${user.fullName}</g:link></td>
						<td><g:link action="delete" id="${user.id}"><i class="icon-trash"></i></g:link></td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<strong>nothing.to.show</strong>
	</g:else>
</body>
</html>