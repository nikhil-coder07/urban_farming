-- Seed data for contacts
INSERT IGNORE INTO contacts (id, name, type, address, phone) VALUES 
(1, 'FreshMart Retail Chain', 'CUSTOMER', '101 Market Rd, Metro City', '9876543210'),
(2, 'GreenLeaf Supermarkets', 'CUSTOMER', '45 Bio Way, North Suburb', '9876543211'),
(3, 'NutriGrow Chemical Supplies', 'VENDOR', '789 Industrial Estate, West Hub', '9876543212');

-- Seed data for products
INSERT IGNORE INTO products (id, name, type, unit_price, uom) VALUES 
(1, 'Organic Aeroponic Lettuce', 'GOODS', 120.00, 'KG'),
(2, 'Concentrated Mineral Solution', 'GOODS', 850.00, 'UNIT'),
(3, 'Misting Nozzle Assembly', 'GOODS', 450.00, 'UNIT');

-- Seed data for chart_of_accounts
INSERT IGNORE INTO chart_of_accounts (id, name, type) VALUES 
(1, 'Aeroponic Tower Infrastructure', 'ASSET'),
(2, 'LED Lighting Assets', 'ASSET'),
(3, 'Cash/Bank', 'ASSET'),
(4, 'Accounts Receivable', 'ASSET'),
(5, 'Mineral Supplier Payables', 'LIABILITY'),
(6, 'Power Utility Creditors', 'LIABILITY'),
(7, 'Produce Sales Revenue', 'INCOME'),
(8, 'Farm Tour Revenue', 'INCOME'),
(9, 'LED Electricity Expenses', 'EXPENSE'),
(10, 'Nutrient Chemical Costs', 'EXPENSE');

-- Seed data for journals
INSERT IGNORE INTO journals (id, name, type) VALUES 
(1, 'Sales Journal', 'SALES'),
(2, 'Purchase Journal', 'PURCHASE'),
(3, 'Cash Journal', 'CASH'),
(4, 'Bank Journal', 'BANK');

-- Seed data for towers
INSERT IGNORE INTO towers (id, zone_name, capacity, led_spectrum) VALUES 
(1, 'Aeroponic Module 1', 150, 'Red/Blue 3:1'),
(2, 'Aeroponic Module 2', 200, 'Full Spectrum White'),
(3, 'Aeroponic Module 3', 180, 'Full Spectrum White');

-- Seed data for analytic_accounts
INSERT IGNORE INTO analytic_accounts (id, zone_name) VALUES 
(1, 'Aeroponic Module 1'),
(2, 'Aeroponic Module 2');

-- Seed data for budgets
INSERT IGNORE INTO budgets (id, analytic_account_id, allocated_amount, period) VALUES 
(1, 1, 5000.00, '2026-Q3'),
(2, 2, 7500.00, '2026-Q3');

-- Seed data for telemetry
INSERT IGNORE INTO telemetry (id, tower_id, humidity, nutrient_ppm, recorded_at, mist_triggered) VALUES 
(1, 1, 65.5, 950.0, NOW(), FALSE),
(2, 2, 54.0, 750.0, NOW(), TRUE),
(3, 3, 72.0, 1050.0, NOW(), FALSE);
