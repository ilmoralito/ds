const trigger = document.querySelector('#action-button');
const target = document.querySelector('#full-name');

function handleClick() {
    toggleInput();

    toggleTriggerText();
}

function toggleInput() {
    const triggerText = getTriggerText();

    if (triggerText === 'Editar') {
        const input = createInput();

        target.innerHTML = input;
        return;
    }

    const isValid = validate();

    if (!isValid) {
        alert('El dato es requerido');

        throw Error('No valid data');
    }

    post();

    target.innerHTML = getNewFullName();
}

function post() {
    const params = getParams();
    const url = getUrl();

    axios.post(url, params)
        .then(() => alert('Dato actualizado'))
        .catch(error => {
            alert(`A ocurrido el error: ${error.message}`);
        });
}

function getParams() {
    let params = new URLSearchParams();
    const fullName = getNewFullName();

    params.append('fullName', fullName);
    params.append('id', target.dataset.userId);

    return params;
}

function validate() {
    const fullName = getNewFullName();

    if (fullName === '') {
        target.focus();

        return false;
    }

    return true;
}

function toggleTriggerText() {
    const currentText = getTriggerText();

    trigger.innerHTML = currentText === 'Editar' ? 'Confirmar' : 'Editar';
}

function getNewFullName() {
    return document.querySelector('#new-fullname').value;
}

function getTriggerText() {
    return trigger.innerHTML;
}

function createInput() {
    return `<input id="new-fullname" value="${target.textContent}">`;
}

function getUrl() {
    const host = getHost();

    return `${host}/update/user/fullname`;
}

trigger.addEventListener('click', handleClick);
target.addEventListener('keyup', event => {
    if (event.keyCode === 13) {
        handleClick();
    }
});
