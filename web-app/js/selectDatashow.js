const triggers = document.querySelectorAll('.trigger');

function updateDatashowValue() {
    document.querySelector('#datashow').value = this.dataset.datashow;
}

triggers.forEach(trigger => trigger.addEventListener('click', updateDatashowValue));
