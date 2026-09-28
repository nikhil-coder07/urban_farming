document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('current-date').innerText = new Date().toLocaleDateString('en-IN', {
        weekday: 'long', year: 'numeric', month: 'short', day: 'numeric'
    });

    loadContacts();

    document.getElementById('contact-form').addEventListener('submit', handleSaveContact);
    document.getElementById('cancel-btn').addEventListener('click', resetForm);
});

async function loadContacts() {
    try {
        const contacts = await ContactsAPI.getAll();
        const tbody = document.getElementById('contacts-table-body');
        tbody.innerHTML = '';

        if (contacts.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6">No contacts found.</td></tr>';
            return;
        }

        contacts.forEach(c => {
            const tr = document.createElement('tr');
            const typeBadge = c.type === 'CUSTOMER'
                ? '<span class="badge badge-info">CUSTOMER</span>'
                : '<span class="badge badge-warning">VENDOR</span>';

            tr.innerHTML = `
                <td class="mono">#CNT-${c.id}</td>
                <td><strong>${c.name}</strong></td>
                <td>${typeBadge}</td>
                <td class="mono">${c.phone || '-'}</td>
                <td>${c.address || '-'}</td>
                <td>
                    <button class="btn btn-secondary btn-sm" onclick="editContact(${c.id}, '${escapeJs(c.name)}', '${c.type}', '${escapeJs(c.phone || '')}', '${escapeJs(c.address || '')}')">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteContact(${c.id})">Delete</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        showAlert('Failed to load contacts: ' + err.message);
    }
}

async function handleSaveContact(e) {
    e.preventDefault();
    const id = document.getElementById('contact-id').value;
    const data = {
        name: document.getElementById('contact-name').value.trim(),
        type: document.getElementById('contact-type').value,
        phone: document.getElementById('contact-phone').value.trim(),
        address: document.getElementById('contact-address').value.trim()
    };

    try {
        if (id) {
            await ContactsAPI.update(id, data);
            showAlert('Contact updated successfully', 'success');
        } else {
            await ContactsAPI.create(data);
            showAlert('Contact added successfully', 'success');
        }
        resetForm();
        loadContacts();
    } catch (err) {
        showAlert('Failed to save contact: ' + err.message);
    }
}

function editContact(id, name, type, phone, address) {
    document.getElementById('form-title').innerText = 'Edit Contact #' + id;
    document.getElementById('contact-id').value = id;
    document.getElementById('contact-name').value = name;
    document.getElementById('contact-type').value = type;
    document.getElementById('contact-phone').value = phone;
    document.getElementById('contact-address').value = address;

    document.getElementById('save-btn').innerText = 'Update Contact';
    document.getElementById('cancel-btn').style.display = 'inline-block';
}

function resetForm() {
    document.getElementById('form-title').innerText = 'Add New Contact';
    document.getElementById('contact-id').value = '';
    document.getElementById('contact-form').reset();
    document.getElementById('save-btn').innerText = 'Save Contact';
    document.getElementById('cancel-btn').style.display = 'none';
}

async function deleteContact(id) {
    if (!confirm('Are you sure you want to delete this contact?')) return;
    try {
        await ContactsAPI.delete(id);
        showAlert('Contact deleted successfully', 'success');
        loadContacts();
    } catch (err) {
        showAlert('Failed to delete contact: ' + err.message);
    }
}

function escapeJs(str) {
    return str.replace(/'/g, "\\'").replace(/"/g, '&quot;');
}
