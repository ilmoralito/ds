<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Equipo y Horas</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<!-- avaliables datashows -->
	<g:set var="datashows" value="${(req.type == 'express') ? grailsApplication.config.ni.edu.uccleon.datashows : 2}"/>

	<!-- request day -->
	<g:set var="day" value="${req.dateOfApplication[Calendar.DAY_OF_WEEK]}"/>

	<!-- blocks by day of week -->
	<g:if test="${day == 7}">
		<g:set var="blocks" value="${grailsApplication.config.ni.edu.uccleon.saturday.blocks}"/>
	</g:if>
	<g:elseif test="${day == 1}">
		<g:set var="blocks" value="${grailsApplication.config.ni.edu.uccleon.sunday.blocks}"/>
	</g:elseif>
	<g:else>
		<g:set var="blocks" value="${grailsApplication.config.ni.edu.uccleon.blocks}"/>
	</g:else>

	<div class="row">
		<div class="span5">
			<h4>Solicitud del ${req.dateOfApplication.format("yyyy-MM-dd")} en ${req.classroom}</h4>
		</div>
		<div class="span5">
			<g:link event="cancel" class="btn pull-right">Cancelar</g:link>
		</div>
	</div>
	<div class="row">
		<g:each in="${1..datashows}" var="datashow" status="i">
			<div class="${(req.type == 'express') ? 'span2' : 'span5'}">

				<g:set var="datashow" value="${i + 1}"/>

				<g:form params="[datashow:datashow]">
					<h4>Datashow ${datashow}</h4>
					<g:each in="${1..blocks}" var="block" status="j">
						<label class="checkbox">
							<ds:blockToHour block="${j + 1}" doapp="${day}"/>
							<g:if test="${requests.find {it.datashow == datashow && it?.hours?.block?.contains(j)}}">
								<g:if test="${req.datashow == datashow && req.hours.block.contains(j)}">
									<g:checkBox name="blocks" value="${j}" checked="${true}"/>
								</g:if>
								<g:else>
									<g:checkBox name="blocks" value="${j}" checked="${true}" disabled="true"/>
								</g:else>
							</g:if>
							<g:else>
								<g:checkBox name="blocks" value="${j}" checked="${false}"/>
							</g:else>
						</label>
					</g:each>
					<g:submitButton name="confirm" value="Confirmar" class="btn"/>
				</g:form>
			</div>
		</g:each>
	</div>
</body>
</html>