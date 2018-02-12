<section>
    <label for="fullName">Nombres y apellidos</label>
    <g:textField name="fullName" value="${user.fullName}" class="span8"/>

    <label for="email">Correo</label>
    <g:textField name="email" value="${user.email}" class="span8"/>
</section>

<section>
    <label>Rol</label>
    <g:each in="${userModel.roles}" var="role">
        <label class="radio">
            <g:radio
                name="role"
                value="${role}"
                checked="${user?.role == role}"/>
                ${role.capitalize()}
        </label>
    </g:each>
</section>

<section>
    <section>
        <label>Coordinaciones y departamentos</label>

        <details ${userModel.schoolsAndDepartments.schools.any { school -> school in user.schools.tokenize(',') } ? 'open' : ''}>
            <summary>Coordinaciones</summary>
            <g:each in="${userModel.schoolsAndDepartments.schools.sort()}" var="school">
                <label class="checkbox">
                    <g:checkBox
                        name="schools"
                        value="${school}"
                        checked="${user.schools.tokenize(',').contains(school)}"/>
                        ${school}
                </label>
            </g:each>
        </details>
    </section>

    <section>
        <details ${userModel.schoolsAndDepartments.departments.any { department -> department in user.schools.tokenize(',') } ? 'open' : ''}>
            <summary>Departamentos</summary>
            <g:each in="${userModel.schoolsAndDepartments.departments.sort()}" var="department">
                <label class="checkbox">
                    <g:checkBox
                        name="schools"
                        value="${department}"
                        checked="${user.schools.tokenize(',').contains(department)}"/>
                        ${department}
                </label>
            </g:each>
        </details>
    </section>

    <section>
        <label>Aulas</label>
        <g:each in="${userModel.classrooms}" var="classroom">
            <details ${userModel.classrooms[classroom.key].code.any { code -> code in user.classrooms.tokenize(',') } ? 'open' : ''}>
                <summary>${classroom.key}</summary>
                <g:each in="${userModel.classrooms[classroom.key]}" var="c">
                    <label class="checkbox">
                        <g:checkBox
                            form="form"
                            name="classrooms"
                            value="${c.code}"
                            checked="${user.classrooms.tokenize(',').contains(c.code)}"/>
                            ${c.name ?: c.code}
                    </label>
                </g:each>
            </details>
        </g:each>
    </section>
</section>
