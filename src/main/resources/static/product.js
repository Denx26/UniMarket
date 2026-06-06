function togglePretMin() {
    const group = document.getElementById('pretMinGroup');
    const checked = document.getElementById('prodNegociabil').checked;
    group.classList.toggle('max-h-0', !checked);
    group.classList.toggle('opacity-0', !checked);
    group.classList.toggle('max-h-20', checked);  // or max-h-24
    group.classList.toggle('opacity-100', checked);
    if (!checked) document.getElementById('prodPretMin').value = '';
}

function showProductAlert(type, msg) {
    const el = document.getElementById('productAlert');
    const isSuccess = type === 'success';
    el.className = isSuccess
        ? 'bg-emerald-950/40 border border-emerald-500/30 text-emerald-400 px-4 py-3 rounded-xl mb-4 text-sm'
        : 'bg-red-950/40 border border-red-500/30 text-red-400 px-4 py-3 rounded-xl mb-4 text-sm';
    el.textContent = msg;
    el.classList.remove('hidden');
    setTimeout(() => el.classList.add('hidden'), 4000);
}

async function submitProduct() {
    const nume = document.getElementById('prodNume').value.trim();
    const descriere = document.getElementById('prodDescriere').value.trim();
    const pret = parseFloat(document.getElementById('prodPret').value);
    const negociabil = document.getElementById('prodNegociabil').checked;
    const pretMinVal = document.getElementById('prodPretMin').value;
    const pretMin = negociabil && pretMinVal ? parseFloat(pretMinVal) : null;

    if (!nume) return showProductAlert('error', 'Va rog introduceti un nume valid.');
    if (isNaN(pret) || pret < 0) return showProductAlert('error', 'Va rog introduceti un pret valid.');
    if (negociabil && pretMin !== null && pretMin >= pret)
        return showProductAlert('error', 'Pretul minim trebuie sa fie mai mic decat pretul listat.');

    // Pull vanzatorId from wherever you store it after login
    const vanzatorId = parseInt(localStorage.getItem('id'));

    try {
        await axios.post('/api/produse/', { vanzatorId, nume, pret, descriere, negociabil, pretMin });
        showProductAlert('success', 'Produs adaugat cu succes!');
        ['prodNume','prodDescriere','prodPret','prodPretMin'].forEach(id => document.getElementById(id).value = '');
        document.getElementById('prodNegociabil').checked = false;
        togglePretMin();
    } catch (e) {
        showProductAlert('error', 'Eroare. Va rog sa incercati din nou.');
    }
}
document.getElementById('prodNegociabil').addEventListener('change', togglePretMin);
document.getElementById('butonSubmit').addEventListener('click', submitProduct);
