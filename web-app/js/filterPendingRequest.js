const FilterModule = (() => {
    const dataset = Array.from(document.querySelectorAll('[data-id]')).map(td => Object.assign({}, td.dataset));

    function loadServerURL() {
        return window.serverURL;
    }

    function applyFilter(event) {
        const results = dataset.filter(data => data.user.toLowerCase().includes(event.target.value));

        render(results);
    }

    function loadTrigger() {
        document.querySelector('#filter').addEventListener('keyup', applyFilter);
    }

    function render(dataset) {
        let date = null;
        const rows = [];

        dataset.forEach((data, index) => {
            if (data.date !== date) {
                rows.push(renderDateRow(data.date, index));
            }

            rows.push(renderDataRow(data));

            date = data.date;
        });

        document.querySelector('table tbody').innerHTML = rows.join('');
    }

    function renderDateRow(date, index) {
        return `<tr>
            <td colspan="2" ${index === 0 ? 'style="border-top: 0"' : ''}>${date}</td>
        </tr>`;
    }

    function renderDataRow(data) {
        const serverURL = loadServerURL();

        return `<tr>
            <td
                data-id="${data.id}"
                data-user="${data.user}"
                data-classroom="${data.classroom}"
                data-date="${data.date}"
                style="vertical-align: middle;">
                <a href="${serverURL}/request/show/${data.id}">${data.user} en ${data.classroom}</a>
            </td>
            <td width="1">
                <form
                    action="${serverURL}/request/delete/${data.id}"
                    method="POST"
                    value="DELETE"
                    onSubmit="if (!confirm('¿Estás seguro?')) return false;"
                    style="margin: 0;">
                    <input type="hidden" name="_method" value="DELETE">
                    <button type="submit" class="btn btn-link btn-small">
                        <i class="icon-trash"></i>
                    </button>
                </form>
            </td>
        </tr>`;
    }

    function init() {
        loadTrigger();
    }

    return {
        init
    };
})();

FilterModule.init();
