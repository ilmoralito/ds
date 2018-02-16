(() => {
    document.body.addEventListener('click', (event) => {
        if (event.target.classList.contains('show-modal')) {
            showModal.call(event.target);
        }
    });

    function showModal() {
        $('#modal').modal();

        const result = Object.keys(this.dataset).map(key => {
            const displayText = key === 'audio'         ? 'Parlante'    :
                                key === 'screen'        ? 'Pantalla'    :
                                key === 'internet'      ? 'Internet'    :
                                key === 'pointer'       ? 'Puntero'     :
                                key === 'cpu'           ? 'Computador'  :
                                key === 'description'   ? this.dataset.description :
                                                          '';

            return `<li>${displayText}</li>`;
        }).join('');

        document.querySelector('#requirement').innerHTML = result;
    }
})();
