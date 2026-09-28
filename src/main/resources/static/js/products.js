document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('current-date').innerText = new Date().toLocaleDateString('en-IN', {
        weekday: 'long', year: 'numeric', month: 'short', day: 'numeric'
    });

    loadProducts();

    document.getElementById('product-form').addEventListener('submit', handleSaveProduct);
    document.getElementById('cancel-btn').addEventListener('click', resetForm);
});

async function loadProducts() {
    try {
        const products = await ProductsAPI.getAll();
        const tbody = document.getElementById('products-table-body');
        tbody.innerHTML = '';

        if (products.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6">No products found.</td></tr>';
            return;
        }

        products.forEach(p => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td class="mono">#PRD-${p.id}</td>
                <td><strong>${p.name}</strong></td>
                <td><span class="badge badge-info">${p.type}</span></td>
                <td class="mono text-right">${formatCurrency(p.unitPrice)}</td>
                <td class="mono">${p.uom}</td>
                <td>
                    <button class="btn btn-secondary btn-sm" onclick="editProduct(${p.id}, '${escapeJs(p.name)}', ${p.unitPrice}, '${p.uom}')">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteProduct(${p.id})">Delete</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        showAlert('Failed to load products: ' + err.message);
    }
}

async function handleSaveProduct(e) {
    e.preventDefault();
    const id = document.getElementById('product-id').value;
    const data = {
        name: document.getElementById('product-name').value.trim(),
        type: document.getElementById('product-type').value,
        unitPrice: parseFloat(document.getElementById('product-price').value),
        uom: document.getElementById('product-uom').value
    };

    try {
        if (id) {
            await ProductsAPI.update(id, data);
            showAlert('Product updated successfully', 'success');
        } else {
            await ProductsAPI.create(data);
            showAlert('Product added successfully', 'success');
        }
        resetForm();
        loadProducts();
    } catch (err) {
        showAlert('Failed to save product: ' + err.message);
    }
}

function editProduct(id, name, price, uom) {
    document.getElementById('form-title').innerText = 'Edit Product #' + id;
    document.getElementById('product-id').value = id;
    document.getElementById('product-name').value = name;
    document.getElementById('product-price').value = price;
    document.getElementById('product-uom').value = uom;

    document.getElementById('save-btn').innerText = 'Update Product';
    document.getElementById('cancel-btn').style.display = 'inline-block';
}

function resetForm() {
    document.getElementById('form-title').innerText = 'Add New Product';
    document.getElementById('product-id').value = '';
    document.getElementById('product-form').reset();
    document.getElementById('save-btn').innerText = 'Save Product';
    document.getElementById('cancel-btn').style.display = 'none';
}

async function deleteProduct(id) {
    if (!confirm('Are you sure you want to delete this product?')) return;
    try {
        await ProductsAPI.delete(id);
        showAlert('Product deleted successfully', 'success');
        loadProducts();
    } catch (err) {
        showAlert('Failed to delete product: ' + err.message);
    }
}

function escapeJs(str) {
    return str.replace(/'/g, "\\'").replace(/"/g, '&quot;');
}
