const checkboxes = document.querySelectorAll('input[type=checkbox]');
let timeoutId;

function post(url, dataset) {
    let params = new URLSearchParams();

    params.append('userId', dataset.userId);
    params.append('classroom', dataset.classroomCode);

    return axios.post(url, params)
        .then(response => response)
        .catch(error => error);
}

function toggleClassroom(event) {
    const isChecked = this.checked === true;
    const path = isChecked ? '/add/classroom' : '/delete/classroom';
    const url = window.serverURL + '' +path;

    post(url, Object.assign({}, this.dataset))
        .then(data => showMessage(isChecked ? 'Aula agregada' : 'Aula eliminada'))
        .catch(error => showMessage('A ocurrido un error'));
}

function showMessage(msg) {
    const message = document.querySelector('#message');

    message.textContent = msg;

    window.clearTimeout(timeoutId);

    timeoutId = setTimeout(() => message.textContent = '', 3000);
}

checkboxes.forEach(checkbox => checkbox.addEventListener('change', toggleClassroom));
