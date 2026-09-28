let towersList = [];

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('current-date').innerText = new Date().toLocaleDateString('en-IN', {
        weekday: 'long', year: 'numeric', month: 'short', day: 'numeric'
    });

    loadTowersAndTelemetry();

    document.getElementById('tower-form').addEventListener('submit', handleSaveTower);
    document.getElementById('cancel-tower-btn').addEventListener('click', resetTowerForm);
    document.getElementById('telemetry-form').addEventListener('submit', handleSaveTelemetry);
});

async function loadTowersAndTelemetry() {
    try {
        const [towers, latestTelemetry] = await Promise.all([
            AeroponicsAPI.getTowers(),
            AeroponicsAPI.getLatestTelemetry()
        ]);

        towersList = towers;
        renderTowersTable(towers);
        populateTowerSelect(towers);
        renderTelemetryHistory(latestTelemetry);
    } catch (err) {
        showAlert('Failed to load towers/telemetry: ' + err.message);
    }
}

function renderTowersTable(towers) {
    const tbody = document.getElementById('towers-table-body');
    tbody.innerHTML = '';

    if (towers.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5">No aeroponic towers configured yet.</td></tr>';
        return;
    }

    towers.forEach(t => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="mono">#TWR-${t.id}</td>
            <td><strong>${t.zoneName}</strong></td>
            <td class="mono">${t.capacity} Pods</td>
            <td>${t.ledSpectrum}</td>
            <td>
                <button class="btn btn-secondary btn-sm" onclick="editTower(${t.id}, '${escapeJs(t.zoneName)}', ${t.capacity}, '${escapeJs(t.ledSpectrum)}')">Edit</button>
                <button class="btn btn-danger btn-sm" onclick="deleteTower(${t.id})">Delete</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function populateTowerSelect(towers) {
    const select = document.getElementById('telemetry-tower-select');
    select.innerHTML = '<option value="">-- Select Tower --</option>';
    towers.forEach(t => {
        const opt = document.createElement('option');
        opt.value = t.id;
        opt.innerText = `#TWR-${t.id} (${t.zoneName})`;
        select.appendChild(opt);
    });
}

function renderTelemetryHistory(telemetryList) {
    const tbody = document.getElementById('telemetry-history-body');
    tbody.innerHTML = '';

    if (telemetryList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7">No telemetry records logged yet.</td></tr>';
        return;
    }

    telemetryList.forEach(t => {
        const tr = document.createElement('tr');
        const isMist = (t.mistTriggered === true || t.mistCycleTriggered === true);
        const mistBadge = isMist
            ? '<span class="badge badge-warning">MIST CYCLE TRIGGERED</span>'
            : '<span class="badge badge-success">OPTIMAL</span>';

        tr.innerHTML = `
            <td class="mono">#TEL-${t.id}</td>
            <td class="mono">#TWR-${t.tower ? t.tower.id : '-'}</td>
            <td>${t.tower ? t.tower.zoneName : 'N/A'}</td>
            <td class="mono">${t.humidity ? t.humidity.toFixed(1) : '0.0'}%</td>
            <td class="mono">${t.nutrientPpm ? t.nutrientPpm.toFixed(0) : '0'} PPM</td>
            <td class="mono">${formatDateTime(t.recordedAt || t.timestamp)}</td>
            <td>${mistBadge}</td>
        `;
        tbody.appendChild(tr);
    });
}

async function handleSaveTower(e) {
    e.preventDefault();
    const id = document.getElementById('tower-id').value;
    const data = {
        zoneName: document.getElementById('tower-zone').value.trim(),
        capacity: parseInt(document.getElementById('tower-capacity').value),
        ledSpectrum: document.getElementById('tower-led').value.trim()
    };

    try {
        if (id) {
            await AeroponicsAPI.updateTower(id, data);
            showAlert('Tower updated successfully', 'success');
        } else {
            await AeroponicsAPI.createTower(data);
            showAlert('Tower created successfully', 'success');
        }
        resetTowerForm();
        loadTowersAndTelemetry();
    } catch (err) {
        showAlert('Failed to save tower: ' + err.message);
    }
}

function editTower(id, zoneName, capacity, ledSpectrum) {
    document.getElementById('tower-form-title').innerText = 'Edit Tower #' + id;
    document.getElementById('tower-id').value = id;
    document.getElementById('tower-zone').value = zoneName;
    document.getElementById('tower-capacity').value = capacity;
    document.getElementById('tower-led').value = ledSpectrum;

    document.getElementById('save-tower-btn').innerText = 'Update Tower';
    document.getElementById('cancel-tower-btn').style.display = 'inline-block';
}

function resetTowerForm() {
    document.getElementById('tower-form-title').innerText = 'Configure Vertical Tower';
    document.getElementById('tower-id').value = '';
    document.getElementById('tower-form').reset();
    document.getElementById('save-tower-btn').innerText = 'Save Tower';
    document.getElementById('cancel-tower-btn').style.display = 'none';
}

async function deleteTower(id) {
    if (!confirm('Are you sure you want to delete this tower? Associated telemetry records will be deleted.')) return;
    try {
        await AeroponicsAPI.deleteTower(id);
        showAlert('Tower deleted successfully', 'success');
        loadTowersAndTelemetry();
    } catch (err) {
        showAlert('Failed to delete tower: ' + err.message);
    }
}

async function handleSaveTelemetry(e) {
    e.preventDefault();
    const towerId = parseInt(document.getElementById('telemetry-tower-select').value);
    const humidity = parseFloat(document.getElementById('telemetry-humidity').value);
    const nutrientPpm = parseFloat(document.getElementById('telemetry-ppm').value);

    const dto = { towerId, humidity, nutrientPpm };

    try {
        const result = await AeroponicsAPI.recordTelemetry(dto);

        const banner = document.getElementById('mist-status-banner');
        banner.style.display = 'block';

        const isMist = (result.mistTriggered === true || result.mistCycleTriggered === true);
        if (isMist) {
            banner.className = 'alert alert-warning';
            banner.innerHTML = `<strong>ALERT:</strong> Telemetry reading logged (Humidity: ${humidity}%, PPM: ${nutrientPpm}). <strong>Mist Cycle & Nutrient Injection Auto-Triggered!</strong>`;
        } else {
            banner.className = 'alert alert-success';
            banner.innerHTML = `<strong>OPTIMAL:</strong> Telemetry reading logged (Humidity: ${humidity}%, PPM: ${nutrientPpm}). Parameters within normal range.`;
        }

        document.getElementById('telemetry-form').reset();
        loadTowersAndTelemetry();
    } catch (err) {
        showAlert('Failed to log telemetry: ' + err.message);
    }
}

function escapeJs(str) {
    return str.replace(/'/g, "\\'").replace(/"/g, '&quot;');
}
