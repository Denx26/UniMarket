document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const email = localStorage.getItem('email');

    if (!token) {
        window.location.href = './index.html';
        return;
    }

    const userDisplayEmail = document.getElementById('userDisplayEmail');
    if (userDisplayEmail && email) {
        userDisplayEmail.innerText = email;
    }

    hideAllSections();

    if (role === 'BUYER') {
        const menuCumparator = document.getElementById('menuCumparator');
        if (menuCumparator) {
            menuCumparator.classList.remove('hidden');
            menuCumparator.classList.remove('text-slate-300');
            menuCumparator.classList.add('bg-indigo-600/20', 'text-indigo-400', 'font-semibold');
        }

        document.getElementById('sectionCumparator')?.classList.remove('hidden');

    } else if (role === 'SELLER') {
        const menuVanzator = document.getElementById('menuVanzator');
        const menuAdaugareProdus = document.getElementById('menuAdaugareProdus');
        if (menuVanzator) {
            menuVanzator.classList.remove('hidden');
            menuVanzator.classList.remove('text-slate-300');
            menuVanzator.classList.add('bg-amber-600/20', 'text-amber-400', 'font-semibold');
        }

        if (menuAdaugareProdus) {
            menuAdaugareProdus.classList.remove('hidden');
            menuAdaugareProdus.classList.remove('text-slate-300');
            menuAdaugareProdus.classList.add('bg-amber-600/20', 'text-amber-400', 'font-semibold');
        }
        document.getElementById('sectionVanzator')?.classList.remove('hidden');

    } else if (role === 'ADMIN') {
        const menuAdmin = document.getElementById('menuAdmin');
        if (menuAdmin) {
            menuAdmin.classList.remove('hidden');
            menuAdmin.classList.remove('text-slate-300');
            menuAdmin.classList.add('bg-red-600/20', 'text-red-400', 'font-semibold');
        }

        document.getElementById('sectionAdmin')?.classList.remove('hidden');
    }
});

function hideAllSections() {
    const elements = [
        'menuAdmin', 'menuVanzator', 'menuCumparator', 'menuAdaugareProdus',
        'sectionAdmin', 'sectionVanzator', 'sectionCumparator'
    ];
    elements.forEach(id => {
        document.getElementById(id)?.classList.add('hidden');
    });
}

function logout() {
    localStorage.clear();
    window.location.href = './index.html';
}