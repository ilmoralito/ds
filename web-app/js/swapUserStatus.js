{
    document.querySelector('.dropdown-menu').addEventListener('click', getUsers);

    function getUsers(event) {
        const element = event.target;

        if (element.hasAttribute('data-status')) {
            const status = element.dataset.status;
            const response = getData(status);

            removeDropdownMenuActiveState();

            element.parentNode.classList.add('active');

            response.then(json => {
                render(json);
            });
        }
    }

    async function getData(status) {
        const url = getUrl(status);
        const response = await fetch(url);
        const json = await response.json();

        return json;
    }

    function getBody(json) {
        const url = getUserUrl();
        const body = json.map(user => {
            return `
                <tr data-user-id="${user.id}" data-user-fullName="${user.fullName}">
                    <td colspan="2">
                        <a href="${url}/show/${user.id}" class="target">${user.fullName}</a>
                    </td>
                </tr>`;
        }).join('');

        return body;
    }

    function render(json) {
        const body = getBody(json);

        document.querySelector('table#users tbody').innerHTML = body;
    }

    function getUrl(status) {
        const host = getHost();

        return `${host}/${status}/users`;
    }
}
