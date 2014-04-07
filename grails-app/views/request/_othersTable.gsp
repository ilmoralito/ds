<g:if test="${requests}">
	<table class="table table-hover">
		<tbody>
			<g:each in="${requests}" var="request" status="i">
				<tr>
					<td width="1">${i + 1}</td>
					<td width="1">${request?.dateOfApplication?.format("yyyy-MM-dd")}</td>
					<td width="1">${request?.classroom}</td>
					<td>${request?.type}</td>
				</tr>
	    </g:each>
	  </tbody>
	</table>
</g:if>