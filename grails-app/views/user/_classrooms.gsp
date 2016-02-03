<div class="row">
  <g:each in="${classrooms}" var="classroom">
    <div class="span2">
      <h4>${classroom.key}</h4>

      <g:each in="${classroom.value}" var="c">
        <label class="checkbox">
          <g:checkBox
            name="classrooms"
            value="${c.code}"
            checked="${userClassrooms?.contains(c.code)}"
            data-id="${user.id}"
            data-data="${c.code}"
            data-flag="classrooms"/>
            ${c.name ?: c.code}
        </label>
      </g:each>
    </div>
  </g:each>
</div>