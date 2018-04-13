const toggleTriggers = document.querySelectorAll('.toggleTrigger');

function toggle() {
    const index = getIndex(this);
    const tds = getTdsInIndex(index);
    const checkboxes = getEnabledCheckboxes(tds);

    checkboxes.forEach(checkbox => checkbox.checked = this.checked);
}

function getIndex(elem){
    return Array.from(elem.parentNode.parentNode.children).indexOf(elem.parentNode);
}

function getTdsInIndex(index) {
    return document.querySelectorAll(`tbody td:nth-of-type(${index + 1})`);
}

function getEnabledCheckboxes(tds) {
    return Array.from(tds).map(td => td.querySelector('[type=checkbox]')).filter(checkbox => !checkbox.disabled);
}

toggleTriggers.forEach(trigger => trigger.addEventListener('click', toggle));
