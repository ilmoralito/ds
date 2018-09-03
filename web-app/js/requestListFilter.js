function getData() {
    return Array.from(document.querySelectorAll('.filtrable')).map(item => Object.assign({}, item.dataset));
}

const data = getData();

const filter = document.querySelector('#filter');

function applyFilter(event) {
    const results = filterData(event.target.value);

    updateRequestCount({count: results.length});

    drawResult(results);
}

function filterData(value) {
    const results = data.filter(req => {
        const query = value.toLowerCase();
        const user = req.requestUser.toLowerCase();
        const school = req.requestSchool.toLowerCase();
        const status = req.requestStatus.toLowerCase();
        const classroom = req.requestClassroom.toLowerCase();

        return user.includes(query) || classroom.includes(query) || school.includes(query) || status.includes(query);
    });

    return order(results);
}

function order(results) {
    return results.sort((a, b) => a.requestBlocks[0] > b.requestBlocks[0] ? 1 : -1);
}

function drawResult(results) {
    const rows = [];
    let title = null;

    results.forEach((result, index) => {
        const blocks = JSON.parse(result.requestBlocks);
        const block = parseInt(blocks[0], 10);

        if (index === 0 || title !== block) {
            rows.push(createRowTitle({block: block}));

            title = block;
        }

        const row = createRow({
            id: result.requestId,
            user: result.requestUser,
            classroom: result.requestClassroom,
            school: result.requestSchool,
            status: result.requestStatus,
            blocks: result.requestBlocks,
            block: block,
        });

        rows.push(row);
    });

    render(rows.join(''));
}

function updateRequestCount({count}) {
    document.querySelector('#requestCount').textContent = `${count} resultado${count > 1 ? 's' : ''}`;
}

function render(rows) {
    document.querySelector('#tbody').innerHTML = rows;
}

function createRowTitle({block}) {
    return `<tr>
        <td colspan="3" data-block="${block}" style="cursor: pointer;">Bloque ${block + 1}</td>
    </tr>`;
}

function createRow({id, user, school, classroom, status, blocks, block}) {
    return `<tr
        class="filtrable"
        data-request-id="${id}"
        data-request-user="${user}"
        data-request-school="${school}"
        data-request-classroom="${classroom}"
        data-request-status="${status}"
        data-request-blocks="${blocks}">

        <td>
          <input type="checkbox" name="requests" value="${id}" form="status" data-request-block="${block}" class="requests">
        </td>

        <td>
            <a href="${window.serverURL}/request/show/${id}">
                ${buildText({user: user, school: school, classroom: classroom})}
            </a>
        </td>

        <td>
            <a href="${window.serverURL}/request/updateStatus/${id}">
                ${translateStatus(status)}
            </a>
        </td>
    </tr>`;
}

function translateStatus(status) {
    if (status === 'pending') return 'Pendiente';

    if (status === 'attended') return 'Atendido';

    if (status === 'absent') return 'Sin retirar';

    if (status === 'canceled') return 'Cancelado';
}

function buildText({user, classroom, school}) {
    return `Por ${user} de ${school} en ${classroom}`;
}

filter.addEventListener('keyup', applyFilter);