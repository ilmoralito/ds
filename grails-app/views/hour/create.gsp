<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Equipo y Horas</title>
	<r:require modules = "bootstrap-css, bootstrap-responsive-css, jquery-ui, datepicker, app"/>
</head>
<body>
	<!-- avaliables datashows -->
	<g:set var="datashows" value="${(params?.requestType != 'common') ? grailsApplication.config.ni.edu.uccleon.datashows : 2}"/>

	<!-- blocks by day of week -->
	<g:if test="${params?.dayOfApplication == '7'}">
		<g:set var="blocks" value="${grailsApplication.config.ni.edu.uccleon.saturday.blocks}"/>
	</g:if>
	<g:elseif test="${params?.dayOfApplication == '1'}">
		<g:set var="blocks" value="${grailsApplication.config.ni.edu.uccleon.sunday.blocks}"/>
	</g:elseif>
	<g:else>
		<g:set var="blocks" value="${grailsApplication.config.ni.edu.uccleon.blocks}"/>
	</g:else>

	<div class="row">
		<div class="span10">
			<div class="pull-right">
				<g:form controller="request" action="delete">
					<g:hiddenField name="id" value="${params?.requestId}"/>
					<button type="submit" class="btn btn-small"><i class="icon-trash"></i></button>
				</g:form>
			</div>
		</div>

		<g:each in="${1..datashows}" var="datashow" status="i">
			<div class="${(params?.requestType != 'common') ? 'span2' : 'span5'} ">
				<g:form action="create">
					<legend>Datashow ${i}</legend>

					<g:hiddenField name="datashow" value="${i}"/>
					<g:hiddenField name="requestId" value="${params?.requestId}"/>
					<g:hiddenField name="dateOfApplication" value="${params?.dateOfApplication}"/>
					<g:hiddenField name="flag" value="${params?.flag}"/><!--when editing-->

					<g:each in="${1..blocks}" var="block" status="j">
						<label class="checkbox">

							<g:set var="incrementer" value="${j}"/>
							<g:checkBox name="${incrementer}" value="${false}"/> Bloque ${incrementer}

							<g:each in="${requests}" var="request">
								<g:if test="${request.datashow == i && request.hours.block.contains(incrementer)}">
									<g:if test="${params?.requestId.toInteger() == request.id}">
										<g:checkBox name="${incrementer}" disabled="false" value="${true}"/>
									</g:if>
									<g:else>
										<g:checkBox name="${incrementer}" disabled="true" value="${true}"/>
									</g:else>
								</g:if>
							</g:each>

						</label>
					</g:each>
					<br>
					<g:submitButton name="send" value="Confirmar" class="btn"/>
				</g:form>
			</div>
		</g:each>
	</div>
</body>
</html>