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
		<div class="span10">
			<div class="pull-right">
				<div class="btn-group">
				  <g:link event="next" class="btn btn-default">Siguiente</g:link>
				  <button class="btn btn-default dropdown-toggle" data-toggle="dropdown">
						<span class="caret"></span>
					</button>
				  <ul class="dropdown-menu">
				  	<g:each in="${dates}" var="date" status="pos">
				  		<li class="${position == pos ? 'active' : ''}">
				  			<g:link event="next" params="[position:pos]">
				  				${date.format("yyyy-MM-dd")} ${ requestInstances.findAll { it.dateOfApplication == date} ? "*" : "" }
				  			</g:link>
				  		</li>
				  	</g:each>
				  	<g:if test="${requestInstances}">
					  	<li class="divider"></li>
					  	<li>
					  		<g:link event="summary">Sumario</g:link>
					  	</li>
				  	</g:if>
				  </ul>
				</div>
				<g:link event="done" class="btn">Confirmar</g:link>
				<g:link event="cancel" class="btn btn-info">Cancelar</g:link>
			</div>
		</div>
	</div>
	<br>

	<h4>${dates[position].format("yyyy-MM-dd")}</h4>
	
	<div class="row">
		<g:each in="${1..datashows}" var="datashow">
			<div class="span2">
				<h4>Datashow ${datashow}</h4>
				<g:form>
					<a href="#" class="showAndHideDetail"><small>Detalle</small></a>
					
					<div class="detail">
						<g:hiddenField name="dateOfApplication" value="${dates[position].format("yyyy-MM-dd")}"/>
						<g:hiddenField name="datashow" value="${datashow}"/>
						<g:render template="userClassrooms" model="[userClassrooms:userClassrooms, req:null]"/>
						<g:render template="userSchools" model="[userSchools:userSchools, req:null]"/>
						<g:textArea name="description" value="${description}" class="input-block-level" style="resize:vertical; max-height:100px;" placeholder="Descripcion"/>
					</div>

					<g:each in="${1..blocks}" var="block">
						<g:set var="isSelected" value="${requests.find { it.datashow == datashow && it.hours.block.contains(block) }}"/>
						<g:set var="isJustAdded" value="${requestInstances.find { it.dateOfApplication == dates[position] && it.datashow == datashow && it.hours.block.contains(block) }}"/>
						<g:set var="trueOrFalse" value="${isSelected || isJustAdded ? 'true' : 'false'}"/>	

						<div class="checkBox">
							<label>
								<g:checkBox name="hours" value="${block}" checked="${trueOrFalse}" disabled="${trueOrFalse}"/>
								<ds:blockToHour block="${block}" doapp="${day}"/>
							</label>
						</div>
					</g:each>

					<g:submitButton name="add" value="Agregar" class="btn"/>
				</g:form>
			</div>
		</g:each>
	</div>	
</body>
</html>