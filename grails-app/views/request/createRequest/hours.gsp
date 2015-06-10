<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Equipo y Horas</title>
	<r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app"/>
</head>
<body>
	<!-- coordination data -->
	<g:set var="data" value="${grailsApplication.config.ni.edu.uccleon.data}"/>

	<!-- avaliables datashows -->
	<g:set var="datashows" value="${req.type == 'express' ? grailsApplication.config.ni.edu.uccleon.datashows : 2}"/>

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
			El ${req.dateOfApplication.format("yyyy-MM-dd")} en ${req.classroom} por ${req.school}
		</div>
		<div class="span5">
			<g:link event="delete" class="pull-right"><i class="icon-trash"></i></g:link>
		</div>
	</div>

	<div class="row">
		<g:each in="${1..datashows}" var="datashow" status="i">
			<div class="${(req.type == 'express') ? 'span2' : 'span5'}">

				<g:set var="datashow" value="${i + 1}"/>

				<g:form params="[datashow:datashow]">
					<h4>${datashow}</h4>
					<g:each in="${0..blocks}" var="block" status="j">
						<g:set var="isChecked" value="${requests.find {it.datashow == datashow && it?.hours?.block?.contains(j)} ? true : false}"/>
						<g:set var="allowed" value="${data.find { it.coordination == req.school }.datashow}"/>
						<g:set var="isDisabled" value="${datashow == allowed && !isChecked ? false : true}"/>

						<label class="checkbox">
							<ds:blockToHour block="${j + 1}" doapp="${day}"/>
							<g:checkBox name="blocks" value="${j}" checked="${isChecked}" disabled="${isDisabled}"/>
						</label>
					</g:each>
					<g:submitButton name="confirm" value="Confirmar" class="btn btn-small btn-block ${isDisabled ? 'disabled' : ''}"/>
				</g:form>
			</div>
		</g:each>
	</div>
</body>
</html>