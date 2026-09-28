const BASE_URL = '/api';

async function apiFetch(url, options = {}) {
    try {
        const response = await fetch(`${BASE_URL}${url}`, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });

        if (!response.ok) {
            let errorData;
            try {
                errorData = await response.json();
            } catch (e) {
                errorData = { error: response.statusText || 'Server error' };
            }
            
            let message = errorData.error || errorData.message;
            if (errorData.errors) {
                message = Object.values(errorData.errors).join(', ');
            }
            throw new Error(message || `Request failed with status ${response.status}`);
        }

        if (response.status === 204) {
            return null;
        }

        return await response.json();
    } catch (err) {
        console.error(`API Error on ${url}:`, err);
        throw err;
    }
}

// Contacts API
const ContactsAPI = {
    getAll: () => apiFetch('/contacts'),
    getById: (id) => apiFetch(`/contacts/${id}`),
    create: (data) => apiFetch('/contacts', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => apiFetch(`/contacts/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => apiFetch(`/contacts/${id}`, { method: 'DELETE' })
};

// Products API
const ProductsAPI = {
    getAll: () => apiFetch('/products'),
    getById: (id) => apiFetch(`/products/${id}`),
    create: (data) => apiFetch('/products', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => apiFetch(`/products/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => apiFetch(`/products/${id}`, { method: 'DELETE' })
};

// Accounts API
const AccountsAPI = {
    getAll: () => apiFetch('/accounts'),
    create: (data) => apiFetch('/accounts', { method: 'POST', body: JSON.stringify(data) }),
    update: (id, data) => apiFetch(`/accounts/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    delete: (id) => apiFetch(`/accounts/${id}`, { method: 'DELETE' })
};

// Aeroponics & Telemetry API
const AeroponicsAPI = {
    getTowers: () => apiFetch('/aeroponics/towers'),
    createTower: (data) => apiFetch('/aeroponics/towers', { method: 'POST', body: JSON.stringify(data) }),
    updateTower: (id, data) => apiFetch(`/aeroponics/towers/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    deleteTower: (id) => apiFetch(`/aeroponics/towers/${id}`, { method: 'DELETE' }),
    recordTelemetry: (data) => apiFetch('/aeroponics/telemetry', { method: 'POST', body: JSON.stringify(data) }),
    getLatestTelemetry: () => apiFetch('/aeroponics/telemetry/latest'),
    getTowerTelemetryHistory: (towerId) => apiFetch(`/aeroponics/telemetry/tower/${towerId}`)
};

// Purchases API
const PurchasesAPI = {
    getPOs: () => apiFetch('/purchase-orders'),
    createPO: (data) => apiFetch('/purchase-orders', { method: 'POST', body: JSON.stringify(data) }),
    convertToBill: (id) => apiFetch(`/purchase-orders/${id}/convert-to-bill`, { method: 'POST' }),
    getBills: () => apiFetch('/vendor-bills'),
    payBill: (id, data) => apiFetch(`/vendor-bills/${id}/pay`, { method: 'POST', body: JSON.stringify(data) })
};

// Sales API
const SalesAPI = {
    getSOs: () => apiFetch('/sales-orders'),
    createSO: (data) => apiFetch('/sales-orders', { method: 'POST', body: JSON.stringify(data) }),
    convertToInvoice: (id, data) => apiFetch(`/sales-orders/${id}/convert-to-invoice`, { method: 'POST', body: JSON.stringify(data) }),
    getInvoices: () => apiFetch('/customer-invoices'),
    payInvoice: (id, data) => apiFetch(`/customer-invoices/${id}/pay`, { method: 'POST', body: JSON.stringify(data) })
};

// Analytics & Budgets API
const AnalyticsAPI = {
    getAnalyticAccounts: () => apiFetch('/analytic-accounts'),
    createAnalyticAccount: (data) => apiFetch('/analytic-accounts', { method: 'POST', body: JSON.stringify(data) }),
    getBudgets: () => apiFetch('/budgets'),
    createBudget: (data) => apiFetch('/budgets', { method: 'POST', body: JSON.stringify(data) })
};

// Reports API
const ReportsAPI = {
    getBalanceSheet: () => apiFetch('/reports/balance-sheet'),
    getProfitLoss: () => apiFetch('/reports/profit-loss'),
    getBudgetReport: (analyticAccountId) => apiFetch(`/reports/budget/${analyticAccountId}`)
};

// Format Helpers
function formatCurrency(amount) {
    if (amount === null || amount === undefined) return '₹0.00';
    return '₹' + Number(amount).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function formatDate(dateString) {
    if (!dateString) return '-';
    const d = new Date(dateString);
    return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) return '-';
    const d = new Date(dateTimeString);
    return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' }) + ' ' +
           d.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit' });
}

function showAlert(message, type = 'danger', elementId = 'alert-box') {
    const el = document.getElementById(elementId);
    if (!el) return;
    el.className = `alert alert-${type}`;
    el.innerText = message;
    el.style.display = 'block';
    setTimeout(() => {
        el.style.display = 'none';
    }, 5000);
}
