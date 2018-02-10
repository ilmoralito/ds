<ul class="nav nav-tabs" style="margin-bottom: 10px;">
    <li class="${actionName == 'profile' ? 'active' : ''}">
        <g:link action="profile">Perfil</g:link>
    </li>
    <li class="${actionName in ['password', 'updatePassword'] ? 'active' : ''}">
        <g:link action="password">Cambiar clave</g:link>
    </li>
    <li class="${actionName == 'classrooms' ? 'active' : ''}">
        <g:link action="classrooms">Administrar aulas</g:link>
    </li>
</ul>
