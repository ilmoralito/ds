function dsAlert(message) {
    const exist = existBox();

    if (!exist) {
        appendMessage(message);
        removeMessage();
    }
}

function appendMessage(message) {
    const box = createBox(message);
    const span = getSpan();

    document.querySelector(`.${span}`).insertAdjacentHTML('beforeend', box);
}

function removeMessage() {
    const id = setTimeout(() => {
        document.querySelector('.alert').remove();

        clearTimeout(id);
    }, 3000);
}

function createBox(message) {
    return `<div class="alert alert-info">${message}</div>`;
}

function getSpan() {
    const children = getRowChildrenLength();

    return children === 2 ? 'span10' : 'span2';
}

function existBox() {
    const box = document.querySelector('.alert');

    return box ? true : false;
}

function getRowChildrenLength() {
    return document.querySelector('.container .row').children.length;
}
