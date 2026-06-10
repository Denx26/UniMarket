document.addEventListener('DOMContentLoaded', () => {
    const role = localStorage.getItem('role');
    if (role !== 'ADMIN') {
        return;
    }
    loadSellers();
});

async function loadSellers() {
    const tableBody = document.getElementById('sellersTableBody');
    if (!tableBody) return;

    try {
        const response = await axios.get('/api/admin/sellers');
        const sellers = response.data;

        if (!sellers || sellers.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="4" class="px-4 py-8 text-center text-slate-500 text-xs">No sellers registered yet.</td></tr>`;
            return;
        }

        tableBody.innerHTML = sellers.map(seller => buildRow(seller)).join('');
    } catch (error) {
        tableBody.innerHTML = `<tr><td colspan="4" class="px-4 py-8 text-center text-red-400 text-xs">Error loading sellers.</td></tr>`;
    }
}

function buildRow(seller) {
    let statusBadge;
    let actions;

    switch (seller.accountStatus) {
        case 'PENDING':
            statusBadge = badge('Pending', 'amber');
            actions = `
                <button onclick="approveSeller(${seller.id})" class="${btn('emerald')} mr-2">Approve</button>
                <button onclick="rejectSeller(${seller.id})" class="${btn('orange')}">Reject</button>`;
            break;
        case 'ACTIVE':
            statusBadge = badge('Active', 'emerald');
            actions = `<button onclick="cancelSeller(${seller.id})" class="${btn('red')}">Cancel account</button>`;
            break;
        case 'CANCELLED':
            statusBadge = badge('Cancelled', 'red');
            actions = `<span class="text-slate-600 text-xs">Account blocked</span>`;
            break;
        case 'REJECTED':
            statusBadge = badge('Rejected', 'slate');
            actions = `<span class="text-slate-600 text-xs">Request rejected</span>`;
            break;
        default:
            statusBadge = badge(seller.accountStatus || '?', 'slate');
            actions = '';
    }

    return `
        <tr class="hover:bg-white/[0.02] transition">
            <td class="px-4 py-3 text-slate-500">#${seller.id}</td>
            <td class="px-4 py-3 text-slate-200 font-medium">${seller.email}</td>
            <td class="px-4 py-3">${statusBadge}</td>
            <td class="px-4 py-3 text-right">${actions}</td>
        </tr>`;
}

function badge(text, color) {
    return `<span class="px-2.5 py-1 rounded-full text-xs font-semibold bg-${color}-500/15 text-${color}-400">${text}</span>`;
}

function btn(color) {
    return `bg-${color}-600/80 hover:bg-${color}-600 text-white text-xs font-semibold px-3 py-1.5 rounded-lg transition`;
}


async function approveSeller(id) {
    await sendAction(`/api/admin/approve/${id}`);
}

async function rejectSeller(id) {
    if (!confirm('Are you sure you want to reject this seller request?')) return;
    await sendAction(`/api/admin/reject/${id}`);
}

async function cancelSeller(id) {
    if (!confirm('Are you sure you want to cancel this account? The seller will no longer be able to log in.')) return;
    await sendAction(`/api/admin/cancel/${id}`);
}

async function sendAction(url) {
    try {
        const response = await axios.put(url);
        showAlert(response.data.message);
        loadSellers();
    } catch (error) {
        showAlert(error.response?.data?.message || 'An error occurred.', true);
    }
}


function showAlert(message, isError = false) {
    const alert = document.getElementById('adminAlert');
    if (!alert) return;
    alert.classList.remove('hidden');
    alert.innerText = message;
    if (isError) {
        alert.className = 'bg-red-950/40 border border-red-500/30 text-red-400 px-4 py-3 rounded-xl mb-4 text-sm';
    } else {
        alert.className = 'bg-emerald-950/40 border border-emerald-500/30 text-emerald-400 px-4 py-3 rounded-xl mb-4 text-sm';
    }
}