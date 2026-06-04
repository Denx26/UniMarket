const role = localStorage.getItem('role');
const email = localStorage.getItem('email');

if (!role || !email) {
    window.location.href = '/index.html';
}

document.getElementById('userDisplayEmail').innerText = email;

if (role === 'ADMIN') {
    document.getElementById('menuAdmin').classList.remove('hidden');
    document.getElementById('sectionAdmin').classList.remove('hidden');
} else if (role === 'VANZATOR') {
    document.getElementById('menuVanzator').classList.remove('hidden');
    document.getElementById('sectionVanzator').classList.remove('hidden');
} else {
    document.getElementById('menuCumparator').classList.remove('hidden');
    document.getElementById('sectionCumparator').classList.remove('hidden');
}

function logout() {
    localStorage.clear();
    window.location.href = './index.html';
}