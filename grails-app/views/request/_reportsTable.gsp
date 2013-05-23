<table class="table">
	<thead>
		<tr>
        	<th>Resultado</th>
    		<th>Cantidad</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${results}" var="result">
			<tr>
				<td>${result.users.fullName}</td>
				<td>${result.count}</td>
			</tr>
		</g:each>
	</tbody>
</table>