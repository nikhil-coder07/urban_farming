document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('current-date').innerText = new Date().toLocaleDateString('en-IN', {
        weekday: 'long', year: 'numeric', month: 'short', day: 'numeric'
    });

    loadBalanceSheet();
    loadProfitLoss();
    loadAnalyticZonesDropdown();
});

function switchTab(tabId) {
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));

    event.target.classList.add('active');
    document.getElementById('tab-' + tabId).classList.add('active');
}

async function loadBalanceSheet() {
    try {
        const bs = await ReportsAPI.getBalanceSheet();

        // Assets
        const assetsBody = document.getElementById('bs-assets-body');
        assetsBody.innerHTML = '';
        bs.assets.forEach(a => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${a.name}</td>
                <td class="mono text-right">${formatCurrency(a.balance)}</td>
            `;
            assetsBody.appendChild(tr);
        });
        document.getElementById('bs-total-assets').innerText = formatCurrency(bs.totalAssets);

        // Liabilities
        const liabBody = document.getElementById('bs-liabilities-body');
        liabBody.innerHTML = '';
        bs.liabilities.forEach(l => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${l.name}</td>
                <td class="mono text-right">${formatCurrency(l.balance)}</td>
            `;
            liabBody.appendChild(tr);
        });
        document.getElementById('bs-total-liabilities').innerText = formatCurrency(bs.totalLiabilities);

        // Net Position
        const netEl = document.getElementById('bs-net-position');
        netEl.innerText = formatCurrency(bs.netPosition);
        netEl.style.color = bs.netPosition >= 0 ? 'var(--color-success)' : 'var(--color-danger)';

    } catch (err) {
        showAlert('Failed to load Balance Sheet: ' + err.message);
    }
}

async function loadProfitLoss() {
    try {
        const pl = await ReportsAPI.getProfitLoss();

        // Income
        const incBody = document.getElementById('pl-income-body');
        incBody.innerHTML = '';
        pl.income.forEach(i => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${i.name}</td>
                <td class="mono text-right">${formatCurrency(i.balance)}</td>
            `;
            incBody.appendChild(tr);
        });
        document.getElementById('pl-total-income').innerText = formatCurrency(pl.totalIncome);

        // Expenses
        const expBody = document.getElementById('pl-expenses-body');
        expBody.innerHTML = '';
        pl.expenses.forEach(e => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${e.name}</td>
                <td class="mono text-right">${formatCurrency(e.balance)}</td>
            `;
            expBody.appendChild(tr);
        });
        document.getElementById('pl-total-expenses').innerText = formatCurrency(pl.totalExpenses);

        // Net Profit
        const netEl = document.getElementById('pl-net-profit');
        netEl.innerText = formatCurrency(pl.netProfit);
        netEl.style.color = pl.netProfit >= 0 ? 'var(--color-success)' : 'var(--color-danger)';

    } catch (err) {
        showAlert('Failed to load P&L Report: ' + err.message);
    }
}

async function loadAnalyticZonesDropdown() {
    try {
        const zones = await AnalyticsAPI.getAnalyticAccounts();
        const select = document.getElementById('budget-zone-select');
        select.innerHTML = '<option value="">-- Select Zone --</option>';

        zones.forEach(z => {
            const opt = document.createElement('option');
            opt.value = z.id;
            opt.innerText = z.zoneName;
            select.appendChild(opt);
        });
    } catch (err) {
        showAlert('Failed to load analytic zones: ' + err.message);
    }
}

async function loadBudgetReport() {
    const zoneId = document.getElementById('budget-zone-select').value;
    const tbody = document.getElementById('budget-report-body');

    if (!zoneId) {
        tbody.innerHTML = '<tr><td colspan="6">Select an analytic zone above to view budget utilization.</td></tr>';
        return;
    }

    try {
        const b = await ReportsAPI.getBudgetReport(zoneId);
        tbody.innerHTML = '';

        const tr = document.createElement('tr');
        const pctFormatted = b.percentageUsed ? b.percentageUsed.toFixed(1) : '0.0';

        tr.innerHTML = `
            <td><strong>${b.zoneName}</strong></td>
            <td class="mono">${b.period}</td>
            <td class="mono text-right">${formatCurrency(b.allocatedAmount)}</td>
            <td class="mono text-right">${formatCurrency(b.actualExpense)}</td>
            <td class="mono text-right" style="color: ${b.variance >= 0 ? 'var(--color-success)' : 'var(--color-danger)'}">${formatCurrency(b.variance)}</td>
            <td style="width: 200px;">
                <div style="display: flex; align-items: center; gap: 8px;">
                    <div class="progress-bar-bg">
                        <div class="progress-bar-fill" style="width: ${Math.min(b.percentageUsed || 0, 100)}%; background-color: ${b.percentageUsed > 100 ? 'var(--color-danger)' : 'var(--color-primary)'}"></div>
                    </div>
                    <span class="mono" style="font-size: 11px;">${pctFormatted}%</span>
                </div>
            </td>
        `;
        tbody.appendChild(tr);
    } catch (err) {
        showAlert('Failed to load budget report: ' + err.message);
    }
}
