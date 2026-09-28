let allAccounts = [];
let currentFilter = 'ALL';

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('current-date').innerText = new Date().toLocaleDateString('en-IN', {
        weekday: 'long', year: 'numeric', month: 'short', day: 'numeric'
    });

    loadAccounts();

    document.getElementById('account-form').addEventListener('submit', handleSaveAccount);
    document.getElementById('cancel-btn').addEventListener('click', resetForm);
});

async function loadAccounts() {
    try {
        allAccounts = await AccountsAPI.getAll();
        renderAccountsTable();
    } catch (err) {
        showAlert('Failed to load chart of accounts: ' + err.message);
    }
}

function filterType(type) {
    currentFilter = type;
    document.querySelectorAll('.filter-btn').forEach(btn => {
        if (btn.innerText === type) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    });
    renderAccountsTable();
}

function renderAccountsTable() {
    const tbody = document.getElementById('accounts-table-body');
    tbody.innerHTML = '';

    const filtered = currentFilter === 'ALL'
        ? allAccounts
        : allAccounts.filter(a => a.type === currentFilter);

    if (filtered.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4">No accounts found for category: ${currentFilter}</td></tr>`;
        return;
    }

    filtered.forEach(a => {
        let badgeClass = 'badge-info';
        if (a.type === 'ASSET') badgeClass = 'badge-success';
        if (a.type === 'LIABILITY') badgeClass = 'badge-warning';
        if (a.type === 'INCOME') badgeClass = 'badge-info';
        if (a.type === 'EXPENSE') badgeClass = 'badge-danger';

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="mono">#ACC-${a.id}</td>
            <td><strong>${a.name}</strong></td>
            <td><span class="badge ${badgeClass}">${a.type}</span></td>
            <td>
                <button class="btn btn-secondary btn-sm" onclick="editAccount(${a.id}, '${escapeJs(a.name)}', '${a.type}')">Edit</button>
                <button class="btn btn-danger btn-sm" onclick="deleteAccount(${a.id})">Delete</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

async function handleSaveAccount(e) {
    e.preventDefault();
    const id = document.getElementById('account-id').value;
    const data = {
        name: document.getElementById('account-name').value.trim(),
        type: document.getElementById('account-type').value
    };

    try {
        if (id) {
            await AccountsAPI.update(id, data);
            showAlert('Account updated successfully', 'success');
        } else {
            await AccountsAPI.create(data);
            showAlert('Account added successfully', 'success');
        }
        resetForm();
        loadAccounts();
    } catch (err) {
        showAlert('Failed to save account: ' + err.message);
    }
}

function editAccount(id, name, type) {
    document.getElementById('form-title').innerText = 'Edit Account #' + id;
    document.getElementById('account-id').value = id;
    document.getElementById('account-name').value = name;
    document.getElementById('account-type').value = type;

    document.getElementById('save-btn').innerText = 'Update Account';
    document.getElementById('cancel-btn').style.display = 'inline-block';
}

function resetForm() {
    document.getElementById('form-title').innerText = 'Add Account to Ledger';
    document.getElementById('account-id').value = '';
    document.getElementById('account-form').reset();
    document.getElementById('save-btn').innerText = 'Save Account';
    document.getElementById('cancel-btn').style.display = 'none';
}

async function deleteAccount(id) {
    if (!confirm('Are you sure you want to delete this account?')) return;
    try {
        await AccountsAPI.delete(id);
        showAlert('Account deleted successfully', 'success');
        loadAccounts();
    } catch (err) {
        showAlert('Failed to delete account: ' + err.message);
    }
}

function escapeJs(str) {
    return str.replace(/'/g, "\\'").replace(/"/g, '&quot;');
}
