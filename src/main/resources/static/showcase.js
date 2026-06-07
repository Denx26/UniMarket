document.addEventListener('DOMContentLoaded', () => {
    const role = localStorage.getItem('role');
    if (role === 'BUYER') {
        loadAvailableProducts();
    }
});

async function loadAvailableProducts() {
    const grid = document.getElementById('productGrid');
    if (!grid) return;

    try {
        const response = await axios.get('/api/products/available');
        const products = response.data;

        if (!products || products.length === 0) {
            grid.innerHTML = `
                <div class="col-span-full border border-dashed border-white/[0.1] rounded-xl p-12 text-center text-slate-500 text-xs">
                    <i class="fa-solid fa-box-open text-2xl mb-2 text-slate-600 block"></i>
                    Nu exista produse disponibile in acest moment. Revino mai tarziu!
                </div>`;
            return;
        }

        grid.innerHTML = products.map(product => `
            <div class="bg-white/[0.02] border border-white/[0.06] rounded-2xl p-5 flex flex-col justify-between hover:border-indigo-500/30 transition duration-300 shadow-lg relative group" id="card-${product.pid}">
                <div>
                    <div class="flex justify-between items-start gap-2 mb-2">
                        <h4 class="text-base font-bold text-slate-100 tracking-tight">${product.nume}</h4>
                        <span class="text-emerald-400 font-extrabold text-sm">${product.pret.toFixed(2)} RON</span>
                    </div>
                    <p class="text-slate-400 text-xs leading-relaxed mb-6 h-12 overflow-hidden text-ellipsis line-clamp-3">${product.descriere || 'Nu exista descriere pentru acest produs.'}</p>
                </div>

                <div class="flex gap-2 w-full mt-auto">
                    <button onclick="buyProduct(${product.pid})" class="flex-1 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs py-2.5 rounded-xl transition duration-200 flex items-center justify-center gap-1.5 shadow-lg shadow-indigo-600/10">
                        <i class="fa-solid fa-cart-shopping"></i> Cumpara Acum
                    </button>
                </div>
            </div>
        `).join('');

    } catch (error) {
        grid.innerHTML = `
            <div class="col-span-full text-center text-red-400 text-xs p-6 bg-red-950/20 border border-red-500/20 rounded-xl">
                Eroare la conectarea cu catalogul de produse.
            </div>`;
    }
}

async function buyProduct(productId) {
    if (!confirm('Confirmi ca doresti sa cumperi acest produs la pretul afisat?')) return;

    const buyerEmail = localStorage.getItem('email') || "cumparator.anonim@email.com";
    const alertBox = document.getElementById('showcaseAlert');

    try {
        const response = await axios.post(`/api/purchase/${productId}`, { buyerEmail });

        if (alertBox) {
            alertBox.textContent = response.data.message;
            alertBox.className = 'bg-emerald-950/40 border border-emerald-500/30 text-emerald-400 px-4 py-3 rounded-xl mb-4 text-sm block';

            const cardElement = document.getElementById(`card-${productId}`);
            if (cardElement) cardElement.remove();

            setTimeout(() => alertBox.classList.add('hidden'), 4000);
        }

        const remainingCards = document.getElementById('productGrid').children;
        if (remainingCards.length === 0) {
            loadAvailableProducts();
        }

    } catch (error) {
        if (alertBox) {
            alertBox.textContent = error.response?.data?.message || 'Tranzactia nu a putut fi procesata.';
            alertBox.className = 'bg-red-950/40 border border-red-500/30 text-red-400 px-4 py-3 rounded-xl mb-4 text-sm block';
            setTimeout(() => alertBox.classList.add('hidden'), 4000);
        }
    }
}

document.getElementById('menuCumparator')?.addEventListener('click', () => {
    loadAvailableProducts();
});