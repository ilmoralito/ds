{
    document.querySelector('.dropdown-menu').addEventListener('click', getUsers);

    function getUsers(event) {
        const element = event.target;

        if (element.hasAttribute('data-role')) {
            const role = element.dataset.role;
            const response = getData(role);

            removeDropdownMenuActiveState();

            highlightElement(element.parentNode);

            response.then(json => {
                render(json);
            });
        }
    }

    async function getData(role) {
        const url = getUrl(role);
        const response = await fetch(url);
        const json = await response.json();

        return json;
    }

    function getBody(json) {
        const body = json.map(user => {
            return `
                <tr data-user-id="${user.id}" data-user-fullName="${user.fullName}">
                    <td colspan="2">
                        <a id="${user.id}" class="target">${user.fullName}</a>
                    </td>
                </tr>`;
        }).join('');

        return body;
    }

    function render(json) {
        const body = getBody(json);

        document.querySelector('table#users tbody').innerHTML = body;
    }

    function getUrl(role) {
        const host = getHost();

        return `${host}/${role}/users`;
    }
}