<label for="classroom">Aula</label>
<g:select
	from="${userClassrooms}"
	name="classroom"
	optionKey="code"
	optionValue="name"
	value="${req?.classroom}"
	class="input-block"/>
