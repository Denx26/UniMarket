function togglePretMin() {
    const group = document.getElementById('pretMinGroup');
    const checked = document.getElementById('prodNegociabil').checked;
    if (checked) {
        group.style.maxHeight = '80px';
        group.style.opacity = '1';
    } else {
        group.style.maxHeight = '0';
        group.style.opacity = '0';
        document.getElementById('prodPretMin').value = '';
    }
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

    if (!nume) return showProductAlert('error', 'Please enter a product name.');
    if (isNaN(pret) || pret < 0) return showProductAlert('error', 'Please enter a valid price.');
    if (negociabil && pretMin !== null && pretMin >= pret)
        return showProductAlert('error', 'Minimum price must be lower than the listed price.');

    // Pull vanzatorId from wherever you store it after login
    const vanzatorId = parseInt(localStorage.getItem('userId'));

    try {
        await axios.post('/api/produse/', { vanzatorId, nume, pret, descriere, negociabil, pretMin });
        showProductAlert('success', 'Product listed successfully!');
        ['prodNume','prodDescriere','prodPret','prodPretMin'].forEach(id => document.getElementById(id).value = '');
        document.getElementById('prodNegociabil').checked = false;
        togglePretMin();
    } catch (e) {
        showProductAlert('error', 'Failed to add product. Please try again.');
    }
}