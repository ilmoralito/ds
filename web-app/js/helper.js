function getHost() {
    const origin = window.location.origin;

    if (origin === 'http://localhost:8080') {
        return `${origin}/ds`;
    }

    return `${origin}`;
}

function getUserUrl() {
    const host = getHost();

    return `${host}/user`;
}

function highlightElement(element) {
    element.classList.add('active');
}

function removeDropdownMenuActiveState() {
    document.querySelectorAll('ul.dropdown-menu li').forEach(li => li.classList.remove('active'));
}
