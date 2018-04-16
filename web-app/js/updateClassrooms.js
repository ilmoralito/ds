const user = document.querySelector('#user');

function updateClassrooms() {
    const option = getSelectedOption(this);
    const dataset = Object.assign({}, option.dataset);

    renderClassrooms(JSON.parse(dataset.classrooms));
}

function renderClassrooms(dataset) {
    const rows = [];

    dataset.forEach(data => {
        const options = data.classrooms.map(classroom => {

            return `
                <option
                    value="${classroom.code}"
                    data-wifi="${classroom.wifi ? true : false }">
                    ${classroom.name ? classroom.name : classroom.code}
                </option>`;
        }).join('');

        const optgroup = `<optgroup label="${data.code}">${options}</optgroup>`;

        rows.push(optgroup);
    });

    document.querySelector('#classroom').innerHTML = rows.join('');
}

function getSelectedOption(select) {
    return select.options[select.selectedIndex];
}

user.addEventListener('change', updateClassrooms);
