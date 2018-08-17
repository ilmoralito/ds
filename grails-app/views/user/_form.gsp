<g:set var="role" value="${params.role ?: user.role}"/>

<g:hiddenField name="role" value="${role}"/>

<section>
    <label for="fullName">Nombres y apellidos</label>
    <g:textField name="fullName" value="${user?.fullName}" class="span8"/>

    <label for="email">${role == 'user' ? 'Correo' : 'Correo institucional'}</label>
    <g:textField name="email" value="${user?.email}" placeholder="${role != 'user' ? 'nombre.apellido@ucc.edu.ni' : ''}" class="span8"/>
</section>

<section>
    <g:if test="${userModel.schools.size() == 1}">
        <g:hiddenField name="schools" value="${userModel.schools[0]}"/>
    </g:if>
    <g:else>
        <label>Coordinacion</label>
        <g:each in="${userModel.schools.sort()}" var="school">
            <label class="checkbox">
                <g:checkBox
                    name="schools"
                    value="${school}"
                    checked="${user?.schools?.tokenize(',')?.contains(school)}"/>
                    ${school}
            </label>
        </g:each>
    </g:else>

    <g:if test="${role != 'admin'}">
        <section>
            <label>Aulas</label>
            <g:each in="${userModel.classrooms}" var="classroom">
                <details ${userModel.classrooms[classroom.key].code.any { code -> code in user?.classrooms?.tokenize(',') } ? 'open' : ''}>
                    <summary>${classroom.key}</summary>
                    <g:each in="${userModel.classrooms[classroom.key]}" var="c">
                        <label class="checkbox">
                            <g:checkBox
                                form="form"
                                name="classrooms"
                                value="${c.code}"
                                checked="${user?.classrooms?.tokenize(',')?.contains(c.code)}"/>
                                ${c.name ?: c.code}
                        </label>
                    </g:each>
                </details>
            </g:each>
        </section>
    </g:if>
</section>
