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
async function loadMyProducts() {
	const vanzatorId = parseInt(localStorage.getItem('id'));
	const tbody = document.getElementById('myProductsTableBody');

	try {
		const res = await axios.get(`/api/produse/vanzator/${vanzatorId}`);
		const products = res.data;

		if (products.length === 0) {
			tbody.innerHTML = `<tr><td colspan="6" class="px-4 py-8 text-center text-slate-500 text-xs">Nu aveti niciun produs.</td></tr>`;
			return;
		}

		tbody.innerHTML = products.map(p => `
            <tr class="hover:bg-white/[0.02] transition" id="row-${p.pid}">
                <td class="px-4 py-3 font-medium text-slate-200">${p.nume}</td>
                <td class="px-4 py-3 text-slate-400 max-w-xs truncate">${p.descriere ?? '—'}</td>
                <td class="px-4 py-3 text-slate-200">${p.pret.toFixed(2)} RON</td>
                <td class="px-4 py-3">
                    ${p.negociabil
				? '<span class="bg-emerald-950/40 text-emerald-400 border border-emerald-500/30 text-xs px-2 py-1 rounded-lg">Yes</span>'
				: '<span class="bg-white/[0.04] text-slate-500 border border-white/[0.08] text-xs px-2 py-1 rounded-lg">No</span>'
			}
                </td>
                <td class="px-4 py-3 text-slate-400">${p.pretMin != null ? p.pretMin.toFixed(2) + ' RON' : '—'}</td>
                <td class="px-4 py-3 text-right space-x-2">
                    <button onclick="deleteProduct(${p.pid})"
                        class="text-xs bg-red-950/40 hover:bg-red-600/80 text-red-400 hover:text-white border border-red-500/30 px-3 py-1.5 rounded-lg transition">
                        Delete
                    </button>
                </td>
            </tr>
        `).join('');
	} catch (e) {
		tbody.innerHTML = `<tr><td colspan="6" class="px-4 py-8 text-center text-red-400 text-xs">Failed to load products.</td></tr>`;
	}
}

async function deleteProduct(pid) {
	if (!confirm('Esti sigur ca vrei sa stergi acest produs?')) return;
	try {
		await axios.delete(`/api/produse/${pid}`);
		document.getElementById(`row-${pid}`).remove();
		// if table is now empty
		const tbody = document.getElementById('myProductsTableBody');
		if (!tbody.hasChildNodes()) {
			tbody.innerHTML = `<tr><td colspan="6" class="px-4 py-8 text-center text-slate-500 text-xs">No products listed yet.</td></tr>`;
		}
	} catch (e) {
		alert('Produsul nu a putut fi sters.');
	}
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
		['prodNume', 'prodDescriere', 'prodPret', 'prodPretMin'].forEach(id => document.getElementById(id).value = '');
		document.getElementById('prodNegociabil').checked = false;
		togglePretMin();
		await loadMyProducts();
	} catch (e) {
		showProductAlert('error', 'Eroare. Va rog sa incercati din nou.');
	}
}
document.getElementById('prodNegociabil').addEventListener('change', togglePretMin);
document.getElementById('butonSubmit').addEventListener('click', submitProduct);
document.getElementById('menuVanzator').addEventListener('click', () => {
	const categorieAdaugareProdus = document.getElementById("categorieAdaugareProdus");
	const categorieVeziToateProdusele = document.getElementById("categorieVeziToateProdusele");
	if (categorieAdaugareProdus) {
		categorieAdaugareProdus.classList.add('hidden');
	}
	if (categorieVeziToateProdusele) {
		categorieVeziToateProdusele.classList.remove('hidden');
	}
	loadMyProducts();
});
document.getElementById('menuAdaugareProdus').addEventListener('click', () => {
	const categorieAdaugareProdus = document.getElementById("categorieAdaugareProdus");
	const categorieVeziToateProdusele = document.getElementById("categorieVeziToateProdusele");
	if (categorieAdaugareProdus) {
		categorieAdaugareProdus.classList.remove('hidden');
	} else {
		console.log("AAA");
	}
	if (categorieVeziToateProdusele) {
		categorieVeziToateProdusele.classList.add('hidden');
	}
	loadMyProducts();
});
document.addEventListener('DOMContentLoaded', () => {
	const role = localStorage.getItem('role');
	if (role === "SELLER")
		loadMyProducts();
})
