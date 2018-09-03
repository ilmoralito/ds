const table = document.querySelector('table');

function toggle(event) {
    if (isBlock(event.target)) {
        toggleCheckedStatus(event.target);
    }
}

function toggleCheckedStatus(element) {
    const inputs = getInputs(element);

    inputs.forEach(input => input.checked = !input.checked);
}

function getInputs(element) {
    const block = getBlock(element);

    return document.querySelectorAll(`[data-request-block="${block}"]`);
}

function getBlock(element) {
    return element.dataset.block;
}

function isBlock(element) {
    return element.hasAttribute('data-block');
}

table.addEventListener('click', toggle, true);
