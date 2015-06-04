<label for="classroom">Aula</label>
<g:select from="${userClassrooms}" name="classroom" optionKey="code" optionValue="name" noSelection="['':'Selecciona aula']" value="${req?.classroom}" class="input-block"/>
