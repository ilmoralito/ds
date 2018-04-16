const classroom = document.querySelector('#classroom');

function toggleInternetInput(e) {
    const trigger = getTrigger(e);
    const option = getSelectedOption(trigger);
    const internetInput = document.querySelector('#internet');

    if (hasWIFI(option)) {
        internetInput.parentNode.classList.remove('hidden');
    } else {
        internetInput.parentNode.classList.add('hidden');
    }
}

function getTrigger(element) {
    return element.nodeType ? element : element.target;
}

function hasWIFI(option) {
    return (option.dataset.wifi == 'true');
}

function getSelectedOption(classroom) {
    return classroom.options[classroom.selectedIndex];
}

classroom.addEventListener('change', toggleInternetInput);

toggleInternetInput(classroom);
