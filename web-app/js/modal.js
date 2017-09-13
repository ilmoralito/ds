(() => {
    const triggers = document.querySelectorAll('.show-modal');

    function showModal(e) {
        $('#modal').modal();

        const result = Object.keys(this.dataset).map((key, value) => {
            const displayText = key === 'audio'         ? 'Parlante'    :
                                key === 'screen'        ? 'Pantalla'    :
                                key === 'internet'      ? 'Internet'    :
                                key === 'pointer'       ? 'Puntero'     :
                                key === 'cpu'           ? 'Computador'  :
                                key === 'description'   ? this.dataset.description :
                                                          '';

            return `
                <li>${displayText}</li>
            `;
        }).join('');

        document.querySelector('#requirement').innerHTML = result;
    }

    triggers.forEach(trigger => trigger.addEventListener('click', showModal));
})();