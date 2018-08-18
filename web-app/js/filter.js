{
    function getData() {
        const nodeList = document.querySelectorAll('table#users tbody tr');
        const dataset = Array.from(nodeList).map(node => Object.assign({}, node.dataset));

        return dataset;
    }

    const data = getData();
    const filter = document.querySelector('#filter');

    filter.addEventListener('keyup', applyFilter);

    function applyFilter(event) {
        removeDropdownMenuActiveState();

        const value = event.target.value;
        const results = data.filter(info => {
            const fullName = info.userFullname.toLowerCase();

            return fullName.includes(value);
        });

        render(results);
    }

    function getView(results) {
        const url = getUserUrl();
        const view = results.map(result => {
            return `
                <tr data-user-id="${result.userId}" data-user-fullName="${result.userFullname}">
                    <td colspan="2">
                        <a href="${url}/show/${result.userId}" class="target">${result.userFullname}</a>
                    </td>
                </tr>`;
        });

        return view.join('');
    }

    function render(results) {
        const view = getView(results);

        document.querySelector('#users tbody').innerHTML = view;
    }
}
