document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('current-date').innerText = new Date().toLocaleDateString('en-IN', {
        weekday: 'long', year: 'numeric', month: 'short', day: 'numeric'
    });

    loadSalesData();

    document.getElementById('so-form').addEventListener('submit', handleCreateSO);
});

async function loadSalesData() {
    try {
        const [contacts, products, sos, invoices] = await Promise.all([
            ContactsAPI.getAll(),
            ProductsAPI.getAll(),
            SalesAPI.getSOs(),
            SalesAPI.getInvoices()
        ]);

        populateDropdowns(contacts, products);
        renderSOTable(sos);
        renderInvoicesTable(invoices);
    } catch (err) {
        showAlert('Failed to load sales data: ' + err.message);
    }
}

function populateDropdowns(contacts, products) {
    const customerSelect = document.getElementById('so-customer-select');
    customerSelect.innerHTML = '<option value="">-- Select Customer --</option>';
    contacts.filter(c => c.type === 'CUSTOMER').forEach(cust => {
        const opt = document.createElement('option');
        opt.value = cust.id;
        opt.innerText = cust.name;
        customerSelect.appendChild(opt);
    });

    const productSelect = document.getElementById('so-product-select');
    productSelect.innerHTML = '<option value="">-- Select Product --</option>';
    products.forEach(p => {
        const opt = document.createElement('option');
        opt.value = p.id;
        opt.innerText = `${p.name} (${formatCurrency(p.unitPrice)}/${p.uom})`;
        productSelect.appendChild(opt);
    });
}

function renderSOTable(sos) {
    const tbody = document.getElementById('so-table-body');
    tbody.innerHTML = '';

    if (sos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">No sales orders recorded.</td></tr>';
        return;
    }

    sos.forEach(so => {
        const tr = document.createElement('tr');
        const statusBadge = so.status === 'INVOICED'
            ? '<span class="badge badge-success">INVOICED</span>'
            : '<span class="badge badge-warning">PENDING</span>';

        const actionBtn = so.status === 'PENDING'
            ? `<button class="btn btn-primary btn-sm" onclick="convertToInvoice(${so.id}, ${so.qty})">Convert to Invoice</button>`
            : '<span class="mono" style="color: green;">Invoice Generated</span>';

        tr.innerHTML = `
            <td class="mono">#SO-${so.id}</td>
            <td><strong>${so.customer ? so.customer.name : 'N/A'}</strong></td>
            <td>${so.product ? so.product.name : 'N/A'}</td>
            <td class="mono text-right">${so.qty}</td>
            <td>${statusBadge}</td>
            <td>${actionBtn}</td>
        `;
        tbody.appendChild(tr);
    });
}

function renderInvoicesTable(invoices) {
    const tbody = document.getElementById('invoices-table-body');
    tbody.innerHTML = '';

    if (invoices.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7">No customer invoices recorded.</td></tr>';
        return;
    }

    invoices.forEach(inv => {
        const tr = document.createElement('tr');
        const statusBadge = inv.paidStatus === 'PAID'
            ? '<span class="badge badge-success">PAID</span>'
            : '<span class="badge badge-danger">UNPAID</span>';

        let actionCell = '<span class="mono" style="color: green;">Settled</span>';
        if (inv.paidStatus === 'UNPAID') {
            actionCell = `
                <div style="display: flex; gap: 6px; align-items: center;">
                    <select id="pay-mode-${inv.id}" class="form-control" style="padding: 3px 6px; font-size: 11px;">
                        <option value="BANK">BANK</option>
                        <option value="CASH">CASH</option>
                    </select>
                    <button class="btn btn-primary btn-sm" onclick="payInvoice(${inv.id})">Mark Paid</button>
                </div>
            `;
        }

        tr.innerHTML = `
            <td class="mono">#INV-${inv.id}</td>
            <td class="mono">#SO-${inv.salesOrder ? inv.salesOrder.id : '-'}</td>
            <td>${inv.salesOrder && inv.salesOrder.customer ? inv.salesOrder.customer.name : 'N/A'}</td>
            <td class="mono text-right">${inv.deliveredKg ? inv.deliveredKg.toFixed(2) + ' KG' : '-'}</td>
            <td class="mono text-right">${formatCurrency(inv.amount)}</td>
            <td>${statusBadge}</td>
            <td>${actionCell}</td>
        `;
        tbody.appendChild(tr);
    });
}

async function handleCreateSO(e) {
    e.preventDefault();
    const customerId = parseInt(document.getElementById('so-customer-select').value);
    const productId = parseInt(document.getElementById('so-product-select').value);
    const qty = parseFloat(document.getElementById('so-qty').value);

    try {
        await SalesAPI.createSO({ customerId, productId, qty });
        showAlert('Sales order created successfully', 'success');
        document.getElementById('so-form').reset();
        loadSalesData();
    } catch (err) {
        showAlert('Failed to create SO: ' + err.message);
    }
}

async function convertToInvoice(soId, defaultQty) {
    const deliveredKgStr = prompt('Enter Actual Delivered Quantity (in KG):', defaultQty);
    if (!deliveredKgStr) return;

    const deliveredKg = parseFloat(deliveredKgStr);
    if (isNaN(deliveredKg) || deliveredKg <= 0) {
        showAlert('Invalid delivered quantity entered.');
        return;
    }

    try {
        await SalesAPI.convertToInvoice(soId, { deliveredKg });
        showAlert(`SO #${soId} converted to Customer Invoice & Revenue Entry Posted!`, 'success');
        loadSalesData();
    } catch (err) {
        showAlert('Failed to convert SO to Invoice: ' + err.message);
    }
}

async function payInvoice(invoiceId) {
    const mode = document.getElementById(`pay-mode-${invoiceId}`).value;
    try {
        await SalesAPI.payInvoice(invoiceId, { mode });
        showAlert(`Invoice #${invoiceId} paid via ${mode} & Cash Entry Posted!`, 'success');
        loadSalesData();
    } catch (err) {
        showAlert('Failed to pay invoice: ' + err.message);
    }
}
