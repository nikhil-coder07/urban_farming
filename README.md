# 🌱 Aeroponic Urban Farm ERP

A comprehensive Enterprise Resource Planning (ERP) and operational management system built with **Java 21**, **Spring Boot 4**, **Spring Data JPA (Hibernate)**, **MySQL**, and **Thymeleaf**.

---

## 🚀 Key Features

### 1. Operations & IoT Telemetry
- **Tower Management:** Zone tracking, plant hole capacity, LED spectrum configuration.
- **Sensor Telemetry:** Automated real-time tracking for humidity (%) and nutrient concentration (PPM).
- **Auto-Mist Logic:** Automated alert triggering when environmental conditions drop below optimal thresholds.

### 2. Dual Architecture: Thymeleaf SSR & REST APIs
- **Server-Side UI:** Full Thymeleaf template integration with custom dark-green aeroponic UI, real-time KPI metrics, responsive sidebar, and data tables.
- **RESTful API:** 12 `@RestController` services mapping `/api/*` for programmatic integration and mobile/SPA clients.

### 3. Double-Entry Accounting & Financial Reporting
- **Chart of Accounts:** ASSET, LIABILITY, INCOME, EXPENSE account classifications.
- **Double-Entry General Ledger:** Balanced debit/credit journal postings for every bill, invoice, and payment with `@Transactional` ACID guarantees.
- **Financial Statements:** Real-time Balance Sheet, Profit & Loss (P&L) Statement, and Analytic Zone Budget vs. Actual Variance analysis.

### 4. Supply Chain & Sales Workflow
- **Purchases:** Purchase Order creation $\rightarrow$ 1-click Conversion to Vendor Bill $\rightarrow$ Cash/Bank settlement.
- **Sales:** Sales Order intake $\rightarrow$ Produce Delivery & Customer Invoicing $\rightarrow$ Receivable collection.

---

## 🛠️ Tech Stack

- **Backend:** Java 21 LTS, Spring Boot 4.0.0
- **Data Persistence:** Spring Data JPA, Hibernate, MySQL 8.x
- **Frontend / Templating:** Thymeleaf, HTML5, CSS3, JavaScript (Fetch API)
- **Validation:** Jakarta Bean Validation (Hibernate Validator)
- **Build Tool:** Apache Maven (Wrapper included)

---

## 💻 Getting Started

### 1. Prerequisites
- Java 21+ JDK
- MySQL Server 8.x running on `localhost:3306`

### 2. Database Setup
```sql
CREATE DATABASE IF NOT EXISTS aeroponic_farm_erp;
```

### 3. Configuration
Update database credentials if needed in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/aeroponic_farm_erp
spring.datasource.username=root
spring.datasource.password=your_password
```

### 4. Build & Run
```bash
# Clone the repository
git clone https://github.com/nikhil-coder07/urban_farming.git
cd urban_farming

# Run the Spring Boot application
./mvnw spring-boot:run
```
*(On Windows PowerShell: `.\mvnw.cmd spring-boot:run`)*

### 5. Access the Web Application
Open your browser at:
- **Dashboard:** [http://localhost:8080/](http://localhost:8080/)
- **Contacts:** [http://localhost:8080/contacts](http://localhost:8080/contacts)
- **Products:** [http://localhost:8080/products](http://localhost:8080/products)
- **Accounts:** [http://localhost:8080/accounts](http://localhost:8080/accounts)
- **Towers & Telemetry:** [http://localhost:8080/towers](http://localhost:8080/towers)
- **Purchases:** [http://localhost:8080/purchases](http://localhost:8080/purchases)
- **Sales:** [http://localhost:8080/sales](http://localhost:8080/sales)
- **Reports:** [http://localhost:8080/reports](http://localhost:8080/reports)
- **REST APIs:** [http://localhost:8080/api/*](http://localhost:8080/api/products)
