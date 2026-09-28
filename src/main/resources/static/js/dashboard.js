document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('current-date').innerText = new Date().toLocaleDateString('en-IN', {
        weekday: 'long', year: 'numeric', month: 'short', day: 'numeric'
    });

    loadDashboardData();
});

async function loadDashboardData() {
    try {
        const [plReport, invoices, bills, towers, latestTelemetry] = await Promise.all([
            ReportsAPI.getProfitLoss().catch(() => ({ totalIncome: 0, totalExpenses: 0 })),
            SalesAPI.getInvoices().catch(() => []),
            PurchasesAPI.getBills().catch(() => []),
            AeroponicsAPI.getTowers().catch(() => []),
            AeroponicsAPI.getLatestTelemetry().catch(() => [])
        ]);

        // Revenue & Expenses — mute color when no transactions exist yet
        const revenueEl = document.getElementById('total-revenue');
        const expensesEl = document.getElementById('total-expenses');
        revenueEl.innerText = formatCurrency(plReport.totalIncome);
        expensesEl.innerText = formatCurrency(plReport.totalExpenses);
        revenueEl.classList.toggle('metric-value--zero', !plReport.totalIncome || Number(plReport.totalIncome) === 0);
        expensesEl.classList.toggle('metric-value--zero', !plReport.totalExpenses || Number(plReport.totalExpenses) === 0);

        // Open Invoices
        const unpaidInvoices = invoices.filter(i => i.paidStatus === 'UNPAID');
        const unpaidInvoiceSum = unpaidInvoices.reduce((sum, i) => sum + (Number(i.amount) || 0), 0);
        const openInvoicesEl = document.getElementById('open-invoices');
        openInvoicesEl.innerText = `${unpaidInvoices.length} (${formatCurrency(unpaidInvoiceSum)})`;
        openInvoicesEl.classList.toggle('metric-value--zero', unpaidInvoices.length === 0);

        // Open Bills
        const unpaidBills = bills.filter(b => b.paidStatus === 'UNPAID');
        const unpaidBillSum = unpaidBills.reduce((sum, b) => sum + (Number(b.amount) || 0), 0);
        const openBillsEl = document.getElementById('open-bills');
        openBillsEl.innerText = `${unpaidBills.length} (${formatCurrency(unpaidBillSum)})`;
        openBillsEl.classList.toggle('metric-value--zero', unpaidBills.length === 0);

        // Active Towers
        const towersEl = document.getElementById('active-towers');
        towersEl.innerText = towers.length;
        towersEl.classList.toggle('metric-value--zero', towers.length === 0);

        // Telemetry table
        const tbody = document.getElementById('telemetry-table-body');
        tbody.innerHTML = '';

        if (latestTelemetry.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="color:var(--color-text-muted);font-style:italic;padding:16px 12px;">No telemetry readings recorded yet.</td></tr>';
            return;
        }

        latestTelemetry.forEach(t => {
            const tr = document.createElement('tr');
            const isMist = (t.mistTriggered === true || t.mistCycleTriggered === true);
            const mistBadge = isMist
                ? '<span class="badge badge-warning">AUTO-MIST TRIGGERED</span>'
                : '<span class="badge badge-success">NORMAL</span>';

            tr.innerHTML = `
                <td class="mono">#TWR-${t.tower ? t.tower.id : '-'}</td>
                <td>${t.tower ? t.tower.zoneName : 'N/A'}</td>
                <td class="mono col-num">${t.humidity ? t.humidity.toFixed(1) : '0.0'}%</td>
                <td class="mono col-num">${t.nutrientPpm ? t.nutrientPpm.toFixed(0) : '0'} PPM</td>
                <td class="mono">${formatDateTime(t.recordedAt || t.timestamp)}</td>
                <td>${mistBadge}</td>
            `;
            tbody.appendChild(tr);
        });

    } catch (err) {
        showAlert('Failed to load dashboard metrics: ' + err.message);
    }
}
