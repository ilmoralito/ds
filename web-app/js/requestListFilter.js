function getData() {
  const data = Array.from(document.querySelectorAll('.filtrable')).map(item => Object.assign({}, item.dataset));

  return data;
}

const data = getData();

const filter = document.querySelector('#filter');

filter.addEventListener('keyup', applyFilter);

function applyFilter(event) {
  const value = event.target.value;

  const results = data.filter(req => {
    const query = value.toLowerCase();
    const user = req.requestUser.toLowerCase();
    const school = req.requestSchool.toLowerCase();
    const status = req.requestStatus.toLowerCase();
    const classroom = req.requestClassroom.toLowerCase();

    return user.includes(query) || classroom.includes(query) || school.includes(query) || status.includes(query);
  })

  const ordered = results.sort((a, b) => a.requestBlocks[0] > b.requestBlocks[0] ? 1 : -1)

  drawResult(ordered);
}

function drawResult(results) {
  const rows = [];
  const rowLastTitle = null;

  results.forEach((result, index) => {
    const row = createRow({
      id: result.requestId,
      user: result.requestUser,
      classroom: result.requestClassroom,
      school: result.requestSchool,
      status: result.requestStatus
    });

    rows.push(row);
  })

  render(rows.join(''));
}

function render(rows) {
  document.querySelector('tbody').innerHTML = rows;
}

function createRowTitle({block}) {
  return `<tr>
    <td colspan="3">Bloque ${block + 1}</td>
  </tr>
  `
}

function createRow({id, user, school, classroom, status}) {
  return `<tr>
    <td>
      <input type="checkbox" name="requests" value="${id}" form="status" class="requests">
    </td>

    <td>
      <a href="/ds/request/show/${id}">
        ${buildText({user: user, school: school, classroom: classroom})}
      </a>
    </td>

    <td>
      <a href="/ds/request/updateStatus/${id}">
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
  return `Para ${user} de ${school} en ${classroom}`;
}
