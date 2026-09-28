document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('current-date').innerText = new Date().toLocaleDateString('en-IN', {
        weekday: 'long', year: 'numeric', month: 'short', day: 'numeric'
    });

    loadPurchaseData();

    document.getElementById('po-form').addEventListener('submit', handleCreatePO);
});

async function loadPurchaseData() {
    try {
        const [contacts, products, pos, bills] = await Promise.all([
            ContactsAPI.getAll(),
            ProductsAPI.getAll(),
            PurchasesAPI.getPOs(),
            PurchasesAPI.getBills()
        ]);

        populateDropdowns(contacts, products);
        renderPOTable(pos);
        renderBillsTable(bills);
    } catch (err) {
        showAlert('Failed to load purchase data: ' + err.message);
    }
}

function populateDropdowns(contacts, products) {
    const vendorSelect = document.getElementById('po-vendor-select');
    vendorSelect.innerHTML = '<option value="">-- Select Vendor --</option>';
    contacts.filter(c => c.type === 'VENDOR').forEach(v => {
        const opt = document.createElement('option');
        opt.value = v.id;
        opt.innerText = v.name;
        vendorSelect.appendChild(opt);
    });

    const productSelect = document.getElementById('po-product-select');
    productSelect.innerHTML = '<option value="">-- Select Product --</option>';
    products.forEach(p => {
        const opt = document.createElement('option');
        opt.value = p.id;
        opt.innerText = `${p.name} (${formatCurrency(p.unitPrice)}/${p.uom})`;
        productSelect.appendChild(opt);
    });
}

function renderPOTable(pos) {
    const tbody = document.getElementById('po-table-body');
    tbody.innerHTML = '';

    if (pos.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">No purchase orders found.</td></tr>';
        return;
    }

    pos.forEach(po => {
        const tr = document.createElement('tr');
        const statusBadge = po.status === 'BILLED'
            ? '<span class="badge badge-success">BILLED</span>'
            : '<span class="badge badge-warning">PENDING</span>';

        const actionBtn = po.status === 'PENDING'
            ? `<button class="btn btn-primary btn-sm" onclick="convertToBill(${po.id})">Convert to Bill</button>`
            : '<span class="mono" style="color: green;">Bill Generated</span>';

        tr.innerHTML = `
            <td class="mono">#PO-${po.id}</td>
            <td><strong>${po.vendor ? po.vendor.name : 'N/A'}</strong></td>
            <td>${po.product ? po.product.name : 'N/A'}</td>
            <td class="mono text-right">${po.qty}</td>
            <td>${statusBadge}</td>
            <td>${actionBtn}</td>
        `;
        tbody.appendChild(tr);
    });
}

function renderBillsTable(bills) {
    const tbody = document.getElementById('bills-table-body');
    tbody.innerHTML = '';

    if (bills.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">No vendor bills recorded.</td></tr>';
        return;
    }

    bills.forEach(b => {
        const tr = document.createElement('tr');
        const statusBadge = b.paidStatus === 'PAID'
            ? '<span class="badge badge-success">PAID</span>'
            : '<span class="badge badge-danger">UNPAID</span>';

        let actionCell = '<span class="mono" style="color: green;">Payment Settled</span>';
        if (b.paidStatus === 'UNPAID') {
            actionCell = `
                <div style="display: flex; gap: 6px; align-items: center;">
                    <select id="pay-mode-${b.id}" class="form-control" style="padding: 3px 6px; font-size: 11px;">
                        <option value="BANK">BANK</option>
                        <option value="CASH">CASH</option>
                    </select>
                    <button class="btn btn-primary btn-sm" onclick="payBill(${b.id})">Mark Paid</button>
                </div>
            `;
        }

        tr.innerHTML = `
            <td class="mono">#BILL-${b.id}</td>
            <td class="mono">#PO-${b.purchaseOrder ? b.purchaseOrder.id : '-'}</td>
            <td>${b.purchaseOrder && b.purchaseOrder.vendor ? b.purchaseOrder.vendor.name : 'N/A'}</td>
            <td class="mono text-right">${formatCurrency(b.amount)}</td>
            <td>${statusBadge}</td>
            <td>${actionCell}</td>
        `;
        tbody.appendChild(tr);
    });
}

async function handleCreatePO(e) {
    e.preventDefault();
    const vendorId = parseInt(document.getElementById('po-vendor-select').value);
    const productId = parseInt(document.getElementById('po-product-select').value);
    const qty = parseFloat(document.getElementById('po-qty').value);

    try {
        await PurchasesAPI.createPO({ vendorId, productId, qty });
        showAlert('Purchase order created successfully', 'success');
        document.getElementById('po-form').reset();
        loadPurchaseData();
    } catch (err) {
        showAlert('Failed to create PO: ' + err.message);
    }
}

async function convertToBill(poId) {
    try {
        await PurchasesAPI.convertToBill(poId);
        showAlert(`PO #${poId} converted to Vendor Bill & Journal Entries Posted!`, 'success');
        loadPurchaseData();
    } catch (err) {
        showAlert('Failed to convert PO to Bill: ' + err.message);
    }
}

async function payBill(billId) {
    const mode = document.getElementById(`pay-mode-${billId}`).value;
    try {
        await PurchasesAPI.payBill(billId, { mode });
        showAlert(`Bill #${billId} paid via ${mode} & Payment Entry Posted!`, 'success');
        loadPurchaseData();
    } catch (err) {
        showAlert('Failed to pay bill: ' + err.message);
    }
}
