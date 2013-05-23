<div class="row">
	<div class="span10">
		<div class="pull-right">
			<g:form action="${actionName}" class="form-inline">
				<g:textField name="from" value="${params?.from}" class="input-small" placeholder="Desde"/>
				<g:textField name="to" value="${params?.to}" class="input-small" placeholder="Hasta"/>
				<button type="submit" class="btn"><i class="icon-search"></i></button>
			</g:form>
		</div>
	</div>
</div>