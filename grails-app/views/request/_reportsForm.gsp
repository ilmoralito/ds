<div class="row">
	<div class="span10">
		<div class="pull-right">
			<g:form action="requestsBy" class="form-inline">
				<g:hiddenField name="type" value="${params.type}"/>

				<g:textField name="from" value="${params?.from}" class="input-small" placeholder="Desde"/>
				<g:textField name="to" value="${params?.to}" class="input-small" placeholder="Hasta"/>
				<button type="submit" class="btn"><i class="icon-search"></i></button>
			</g:form>
		</div>
	</div>
</div>