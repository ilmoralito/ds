	<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Solicitudes</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<div class="row">
		<div class="span10">
			<div class="row">
				<ds:isAdmin>
					<div class="span10">
				</ds:isAdmin>
				<ds:isUser>
					<div class="span5">
				</ds:isUser>
					<g:form action="list" class="form-inline">
						<g:textField name="requestFromDate" value="${params?.requestFromDate}" class="input-small" placeholder="Desde..."/>
						<g:textField name="requestToDate" value="${params?.requestToDate}" class="input-small" placeholder="Hasta..."/>
						<button type="submit" class="btn"><i class="icon-search"></i></button>
					</g:form>
				</div>
				<ds:isUser>
				<div class="span5">
					<div class="pull-right">
						<div class="btn-group">
						    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						    	Tipo de solicitud
						    	<span class="caret"></span>
						    </a>
						    <ul class="dropdown-menu">
								<li><g:link action="create" params="[type:'express']">Solicitud expreso</g:link></li>
								<li><g:link action="create">Solicitud</g:link></li>
						    </ul>
					    </div>
					</div>
				</div>
				</ds:isUser>
			</div>
		</div>
	</div>
	<g:if test="${requests}">
		<table class="table table-hover">
			<thead>
				<tr>
					<th></th>
					<ds:isAdmin>
						<th>Solicitado</th>
					</ds:isAdmin>
					<ds:isUser>
						<th></th>
					</ds:isUser>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${requests}" var="request">
					<g:set var="blocks" value="${request.hours.block.collect{it + 1}}"/>
					<tr>
						<td class="td-mini">
							<g:if test="${request.type == 'common'}"></g:if>
							<g:else><i class="icon-user"></i></g:else>
						</td>
						<ds:isAdmin>
							<td>
								<g:if test="${request.description || request.internet || request.audio || request.screen}">
									<g:link action="show" id="${request.id}"><ds:message request="${request}" blocks="${blocks}"/></g:link>
								</g:if>
								<g:else>
									<ds:message request="${request}" blocks="${blocks}"/>
								</g:else>
							</td>
						</ds:isAdmin>
						<ds:isUser>
							<td>
								<g:link action="edit" id="${request.id}">
									<ds:message request="${request}" blocks="${blocks}"/>
								</g:link>
							</td>
						</ds:isUser>
						<ds:isAdmin>
							<td class="td-mini">
								<g:link action="updateStatus" params="[id:request.id, requestFromDate:params?.requestFromDate, requestToDate:params?.requestToDate]">
									<ds:requestStatus status="${request.status}"/>
								</g:link>
							</td>
						</ds:isAdmin>
						<ds:isUser>
							<td class="td-mini"><g:link action="delete" id="${request.id}"><i class="icon-trash"></i></g:link></td>
						</ds:isUser>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
	<g:else>
		<ds:isAdmin>
			<div class="alert alert-info">
				<strong>nothing.to.show</strong>
			</div>
		</ds:isAdmin>
		<ds:isUser>
			<g:if test="${!ni.edu.uccleon.Request.list().size()}">
				<div class="well">
					<h4>Bienvendio al sistema de solitudes de datashow</h4>
					<p>
						Recuerda porfavor leer la seccion <g:link uri="/faqs">preguntas comunes</g:link>
						para conocer las condiciones del servicio.
					</p>
				</div>
			</g:if>
			<g:else>
				<div class="alert alert-info">
					<strong>nothing.to.show</strong>
				</div>
			</g:else>
		</ds:isUser>
	</g:else>
</body>
</html>