<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="main">
	<title>Crear solicitud</title>
	<r:require modules="bootstrap-css, app"/>
</head>
<body>
	<ul class="nav nav-tabs">
    	<li><g:link action="requestsBySchools">Por Facultades</g:link></li>
    	<li  class="active"><g:link action="requestsByClassrooms">Por aulas</g:link></li>
    	<li><g:link action="requestsByUsers">Por usuarios</g:link></li>
    </ul>

	<g:render template="reportsForm"/>

    <g:if test="${results}">
    	<table class="table">
    		<thead>
    			<tr>
    				<th>Aula</th>
    				<th>Cantidad</th>
    			</tr>
    		</thead>
    		<tbody>
    			<g:each in="${results}" var="result">
    				<tr>
    					<td>${result.classroom}</td>
    					<td>${result.count}</td>
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