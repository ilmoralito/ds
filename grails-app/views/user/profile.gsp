<g:applyLayout name="twoColumns">
    <head>
        <title>Perfil</title>
        <r:require modules="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, updateProfile"/>
    </head>

    <content tag="main">
        <g:render template="navbar"/>

        <table class="table table-hover">
            <col width="25%" />
            <col width="70%" />
            <col width="5%" />

            <tbody>
                <tr>
                    <td style="border-top: 0;">Nombre y apellido</td>
                    <td style="border-top: 0;" id="full-name" data-user-id="${user.id}">${user.fullName}</td>
                    <td style="border-top: 0;">
                        <button id="action-button" class="btn btn-small">Editar</button>
                    </td>
                </tr>
                <tr>
                    <td>Correo</td>
                    <td colspan="2">${user.email}</td>
                </tr>
                <tr>
                    <td>Coordinaciones</td>
                    <td colspan="2">${schools?.join(', ')}</td>
                </tr>
            </tbody>
        </table>
    </content>
</g:applyLayout>
