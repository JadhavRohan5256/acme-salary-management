# ACME Salary Management — Database Design

## 1. Overview

The ACME Salary Management system uses a relational database to store employee information, salary information, salary history, authentication data, audit records, countries, and currencies.

The database is designed to support approximately 10,000 employees while keeping employee search, filtering, salary updates, and analytics efficient and maintainable.

**Database:** MySQL

---

# 2. Database Entities

The initial database contains six core entities:

```text
users
  │
  │ performs
  ▼
audit_logs

countries
  │
  │ has many
  ▼
employees
  │
  │ has many
  ▼
salary_history

currencies
  │
  ├──────────────► employees
  │
  └──────────────► salary_history
```

### Tables

1. `users` — application users and their roles.
2. `employees` — current employee information and current salary.
3. `salary_history` — historical salary changes.
4. `audit_logs` — audit trail for important system operations.
5. `countries` — supported countries for employee records.
6. `currencies` — supported currencies for salary records.

---

# 3. Users Table

Stores users who can access the salary management application.

### `users`

| Column        | Type         | Constraints        | Description           |
| ------------- | ------------ | ------------------ | --------------------- |
| id            | BIGINT       | PK, AUTO_INCREMENT | Unique user ID        |
| username      | VARCHAR(100) | UNIQUE, NOT NULL   | Login username        |
| password_hash | VARCHAR(255) | NOT NULL           | BCrypt password hash  |
| role          | VARCHAR(50)  | NOT NULL           | Application role      |
| created_at    | TIMESTAMP    | NOT NULL           | Creation timestamp    |
| updated_at    | TIMESTAMP    | NOT NULL           | Last update timestamp |

### Roles

Initial role:

```text
HR_MANAGER
```

The design allows additional roles to be introduced later without redesigning the employee data model.

---

# 4. Countries Table

Stores the countries supported by the application.

Using a separate country table avoids storing country names repeatedly in the `employees` table and provides a controlled list of supported countries.

### `countries`

| Column     | Type         | Constraints        | Description             |
| ---------- | ------------ | ------------------ | ----------------------- |
| id         | BIGINT       | PK, AUTO_INCREMENT | Unique country ID       |
| code       | CHAR(2)      | UNIQUE, NOT NULL   | ISO 3166-1 alpha-2 code |
| name       | VARCHAR(100) | UNIQUE, NOT NULL   | Country name            |
| created_at | TIMESTAMP    | NOT NULL           | Creation timestamp      |

### Example

```text
id    code    name
1     IN      India
2     US      United States
3     GB      United Kingdom
4     DE      Germany
5     CA      Canada
6     AU      Australia
7     SG      Singapore
8     AE      United Arab Emirates
```

The application can use `countries.id` as the foreign-key reference from `employees`.

---

# 5. Currencies Table

Stores the currencies supported by the application.

A separate currency table provides a controlled list of supported currencies and avoids storing arbitrary currency codes in employee and salary-history records.

### `currencies`

| Column     | Type         | Constraints        | Description            |
| ---------- | ------------ | ------------------ | ---------------------- |
| id         | BIGINT       | PK, AUTO_INCREMENT | Unique currency ID     |
| code       | CHAR(3)      | UNIQUE, NOT NULL   | ISO 4217 currency code |
| name       | VARCHAR(100) | UNIQUE, NOT NULL   | Currency name          |
| symbol     | VARCHAR(10)  | NULL               | Currency symbol        |
| created_at | TIMESTAMP    | NOT NULL           | Creation timestamp     |

### Example

```text
id    code    name                  symbol
1     INR     Indian Rupee          ₹
2     USD     United States Dollar  $
3     GBP     Pound Sterling        £
4     EUR     Euro                  €
5     CAD     Canadian Dollar       $
6     AUD     Australian Dollar     $
7     SGD     Singapore Dollar      $
8     AED     UAE Dirham            د.إ
```

The application can use `currencies.id` as the foreign-key reference from `employees` and `salary_history`.

---

# 6. Employees Table

Stores the main employee information and current salary.

The database primary key `id` is used as the unique employee identifier. A separate business `employee_code` is not required for the initial version.

Country and currency are represented using foreign keys to the `countries` and `currencies` tables.

### `employees`

| Column         | Type          | Constraints        | Description           |
| -------------- | ------------- | ------------------ | --------------------- |
| id             | BIGINT        | PK, AUTO_INCREMENT | Unique employee ID    |
| first_name     | VARCHAR(100)  | NOT NULL           | First name            |
| last_name      | VARCHAR(100)  | NOT NULL           | Last name             |
| email          | VARCHAR(255)  | UNIQUE, NOT NULL   | Employee email        |
| country_id     | BIGINT        | FK, NOT NULL       | Employee country      |
| department     | VARCHAR(100)  | NOT NULL           | Department            |
| designation    | VARCHAR(150)  | NOT NULL           | Job designation       |
| current_salary | DECIMAL(19,2) | NOT NULL           | Current salary        |
| currency_id    | BIGINT        | FK, NOT NULL       | Salary currency       |
| created_at     | TIMESTAMP     | NOT NULL           | Creation timestamp    |
| updated_at     | TIMESTAMP     | NOT NULL           | Last update timestamp |

### Relationships

```text
countries.id
      │
      │ 1:N
      ▼
employees.country_id
```

```text
currencies.id
      │
      │ 1:N
      ▼
employees.currency_id
```

### Example

```text
id:              1
first_name:      John
last_name:       Smith
email:           john.smith@acme.example
country_id:      2
department:      Engineering
designation:     Senior Software Engineer
current_salary:  120000.00
currency_id:     2
```

The employee ID `1` uniquely identifies this employee within the application.

The application can resolve:

```text
country_id = 2
→ United States

currency_id = 2
→ USD
```

---

# 7. Salary History Table

Stores every salary change for an employee.

### `salary_history`

| Column          | Type          | Constraints        | Description                   |
| --------------- | ------------- | ------------------ | ----------------------------- |
| id              | BIGINT        | PK, AUTO_INCREMENT | Salary history ID             |
| employee_id     | BIGINT        | FK, NOT NULL       | Employee reference            |
| previous_salary | DECIMAL(19,2) | NULL               | Salary before change          |
| new_salary      | DECIMAL(19,2) | NOT NULL           | Salary after change           |
| currency_id     | BIGINT        | FK, NOT NULL       | Currency reference            |
| effective_date  | DATE          | NOT NULL           | Date salary becomes effective |
| changed_by      | BIGINT        | FK, NOT NULL       | User who changed salary       |
| created_at      | TIMESTAMP     | NOT NULL           | Record creation timestamp     |

### Relationships

```text
employees.id
      │
      │ 1:N
      ▼
salary_history.employee_id
```

```text
users.id
      │
      │ 1:N
      ▼
salary_history.changed_by
```

```text
currencies.id
      │
      │ 1:N
      ▼
salary_history.currency_id
```

Every salary update creates a new history record.

Example:

```text
Employee ID: 123

2024-01-01    85000    USD
2025-01-01    95000    USD
2026-01-01   105000    USD
2026-09-19   120000    USD
```

The historical records must never be overwritten.

---

# 8. Audit Logs Table

Stores important actions performed in the application.

### `audit_logs`

| Column      | Type         | Constraints        | Description            |
| ----------- | ------------ | ------------------ | ---------------------- |
| id          | BIGINT       | PK, AUTO_INCREMENT | Audit record ID        |
| user_id     | BIGINT       | FK, NOT NULL       | User performing action |
| action      | VARCHAR(100) | NOT NULL           | Action performed       |
| entity_type | VARCHAR(100) | NOT NULL           | Entity affected        |
| entity_id   | BIGINT       | NULL               | ID of affected entity  |
| details     | JSON         | NULL               | Additional information |
| created_at  | TIMESTAMP    | NOT NULL           | Action timestamp       |

### Example

```json
{
  "previousSalary": 105000,
  "newSalary": 120000,
  "currency": "USD",
  "effectiveDate": "2026-09-19"
}
```

For a salary update, the audit record provides information about what was changed, which employee was affected, and when the operation occurred.

---

# 9. Entity Relationship Diagram

```text
                         ┌──────────────────┐
                         │      users       │
                         ├──────────────────┤
                         │ id PK            │
                         │ username         │
                         │ password_hash    │
                         │ role             │
                         │ created_at       │
                         │ updated_at       │
                         └────────┬─────────┘
                                  │
                     ┌────────────┴─────────────┐
                     │                          │
                     │                          │
                     ▼                          ▼
          ┌──────────────────┐       ┌──────────────────┐
          │  salary_history  │       │   audit_logs     │
          ├──────────────────┤       ├──────────────────┤
          │ id PK            │       │ id PK            │
          │ employee_id FK   │       │ user_id FK       │
          │ previous_salary  │       │ action           │
          │ new_salary       │       │ entity_type      │
          │ currency_id FK   │       │ entity_id        │
          │ effective_date   │       │ details          │
          │ changed_by FK    │       │ created_at       │
          │ created_at       │       └──────────────────┘
          └────────┬─────────┘
                   │
                   │ N:1
                   ▼
          ┌─────────────────────┐
          │      employees      │
          ├─────────────────────┤
          │ id PK               │
          │ first_name          │
          │ last_name           │
          │ email UNIQUE        │
          │ country_id FK       │
          │ department          │
          │ designation         │
          │ current_salary      │
          │ currency_id FK      │
          │ created_at          │
          │ updated_at          │
          └──────┬────────┬─────┘
                 │        │
              N:1│        │N:1
                 ▼        ▼
       ┌──────────────┐  ┌────────────────┐
       │  countries   │  │   currencies   │
       ├──────────────┤  ├────────────────┤
       │ id PK        │  │ id PK          │
       │ code UNIQUE  │  │ code UNIQUE    │
       │ name UNIQUE  │  │ name UNIQUE    │
       │ created_at   │  │ name UNIQUE    │
       └──────────────┘  │ symbol         │
                         │ created_at     │
                         └────────────────┘
```

### Relationships

```text
users 1 ───────── N salary_history

users 1 ───────── N audit_logs

employees 1 ───── N salary_history

countries 1 ───── N employees

currencies 1 ──── N employees

currencies 1 ──── N salary_history
```

---

# 10. Indexing Strategy

The system needs efficient employee search, filtering, salary-history retrieval, and auditing.

### Employee indexes

```sql
CREATE UNIQUE INDEX idx_employee_email
ON employees(email);

CREATE INDEX idx_employee_country
ON employees(country_id);

CREATE INDEX idx_employee_department
ON employees(department);

CREATE INDEX idx_employee_country_department
ON employees(country_id, department);

CREATE INDEX idx_employee_currency
ON employees(currency_id);
```

The primary key index on `employees.id` is automatically created by MySQL.

### Country indexes

```sql
CREATE UNIQUE INDEX idx_country_code
ON countries(code);

CREATE UNIQUE INDEX idx_country_name
ON countries(name);
```

### Currency indexes

```sql
CREATE UNIQUE INDEX idx_currency_code
ON currencies(code);

CREATE UNIQUE INDEX idx_currency_name
ON currencies(name);
```

### Salary history indexes

```sql
CREATE INDEX idx_salary_history_employee
ON salary_history(employee_id);

CREATE INDEX idx_salary_history_employee_effective_date
ON salary_history(employee_id, effective_date);

CREATE INDEX idx_salary_history_currency
ON salary_history(currency_id);
```

The composite index supports queries such as retrieving an employee's salary history ordered or filtered by effective date.

### Audit indexes

```sql
CREATE INDEX idx_audit_user
ON audit_logs(user_id);

CREATE INDEX idx_audit_entity
ON audit_logs(entity_type, entity_id);
```

Indexes are intentionally limited to fields used by the application's expected queries. Additional indexes can be introduced after observing real query patterns.

---

# 11. Salary Update Transaction

Salary updates must be atomic.

When the HR Manager changes a salary:

```text
BEGIN TRANSACTION

1. Load employee

2. Read current salary and current currency

3. Validate new salary and currency

4. Update employees.current_salary
   and employees.currency_id

5. Create salary_history record

6. Create audit_logs record

COMMIT
```

If any operation fails:

```text
ROLLBACK
```

This prevents inconsistent states such as:

```text
Employee salary updated

but

salary history not created
```

The Spring service will use:

```java
@Transactional
```

for this operation.

The transaction ensures that the current salary, salary history, and audit record are committed together.

---

# 12. Data Integrity Rules

## Employee

* `id` is the unique employee identifier.
* `email` must be unique.
* Salary must be greater than or equal to zero.
* `country_id` must reference an existing country.
* `currency_id` must reference an existing currency.
* Required employee fields cannot be null.

## Country

* Country code must be unique.
* Country name must be unique.
* Country code must use the defined ISO country-code format.
* Employees may only reference countries that exist in the `countries` table.

## Currency

* Currency code must be unique.
* Currency code must use the three-letter ISO 4217 format.
* Currency name must be unique.
* Employees and salary-history records may only reference currencies that exist in the `currencies` table.

## Salary

* `new_salary` must be greater than or equal to zero.
* `effective_date` is required.
* Salary history must reference an existing employee.
* Salary history must reference an existing currency.
* Salary changes must be recorded in the history table.
* Historical salary records must not be modified as part of normal salary updates.

## Authentication

* Passwords must never be stored as plain text.
* Passwords will be stored using BCrypt.
* Application roles will control access to protected operations.

---

# 13. Seed Data

The application must support approximately 10,000 employees.

The seed data should contain realistic variations across the following dimensions.

### Countries

The initial country reference data will contain:

```text
IN - India
US - United States
GB - United Kingdom
DE - Germany
CA - Canada
AU - Australia
SG - Singapore
AE - United Arab Emirates
```

### Currencies

The initial currency reference data will contain:

```text
INR - Indian Rupee
USD - United States Dollar
GBP - Pound Sterling
EUR - Euro
CAD - Canadian Dollar
AUD - Australian Dollar
SGD - Singapore Dollar
AED - UAE Dirham
```

### Departments

```text
Engineering
Finance
Human Resources
Marketing
Sales
Operations
Product
```

Employee IDs will be automatically generated by MySQL using `AUTO_INCREMENT`.

Example:

```text
1
2
3
...
9999
10000
```

No separate employee-code generation is required.

Seed data should be deterministic so that tests and local development are reproducible.

Reference data for `countries` and `currencies` should also be deterministic and inserted before employee seed data so that foreign-key relationships are valid.

---

# 14. Currency Design Decision

Salary values are stored together with a reference to the currency in which the salary is denominated.

For example:

```text
2,400,000 INR
120,000 USD
75,000 GBP
```

The first version will **not automatically convert currencies**.

This avoids presenting misleading cross-country salary comparisons without an explicit exchange-rate source and conversion policy.

Analytics should therefore avoid aggregating raw salary amounts across different currencies.

For example, the system should not calculate:

```text
Average salary =
(INR salaries + USD salaries + GBP salaries) / employee count
```

unless a currency conversion mechanism is introduced.

Instead, salary analytics should be grouped or filtered by currency where required.

The `currencies` table provides a controlled list of supported currencies while leaving currency conversion outside the initial database scope.

If currency conversion becomes a future requirement, it can be added as a separate capability with an explicit exchange-rate source and conversion policy.

---

# 15. Design Decisions

## Why MySQL?

A relational database is appropriate because employee, salary history, user, audit, country, and currency records have well-defined relationships and transactional requirements.

MySQL also provides indexing, foreign-key constraints, transactions, JSON support, and mature Spring Boot/JPA integration.

## Why use `employees.id` as the employee identifier?

The database primary key already provides a unique identifier for every employee.

A separate `employee_code` would add another identifier that the application would need to generate, validate, store, and maintain.

For the scope of this assessment, the database-generated employee ID is sufficient.

If a future requirement introduces a business-facing employee number, a separate identifier can be added later.

## Why use a separate countries table?

Countries are reference data rather than employee-specific data.

Using a `countries` table provides:

* A controlled list of supported countries.
* Consistent country values.
* Foreign-key validation.
* Easier country filtering.
* The ability to add country metadata later.
* Avoidance of repeated country names in the `employees` table.

For example:

```text
employees.country_id = 1
        ↓
countries.id = 1
        ↓
IN - India
```

This also makes it easier to extend country information later without modifying the employee table.

## Why use a separate currencies table?

Currencies are reference data and should be controlled rather than stored as arbitrary strings.

Using a `currencies` table provides:

* A controlled list of supported currencies.
* Validation of currency references.
* Consistent ISO 4217 currency codes.
* Currency names and symbols in one location.
* Easier future currency management.
* Support for future currency-related metadata.

For example:

```text
employees.currency_id = 2
        ↓
currencies.id = 2
        ↓
USD - United States Dollar
```

## Why keep `current_salary` on employees?

The employee list and dashboard frequently need current salary information.

Keeping the current value on the employee record avoids repeatedly calculating the latest salary history record for common queries.

Salary history remains responsible for historical salary changes.

## Why separate salary history?

Salary information is time-sensitive.

Overwriting the current salary would destroy information needed to understand how compensation changed over time.

The `salary_history` table preserves each salary change.

## Why use audit logs?

Salary changes are sensitive business operations.

An audit record provides traceability regarding:

* who performed the operation
* what entity was affected
* what action was performed
* when the operation occurred
* additional relevant details

## Why not create separate department tables initially?

The current requirements only need department filtering and analytics.

A normalized department reference table can be introduced later if departments require additional metadata, administration, ownership, or organizational relationships.

---

# 16. Future Extensions

The schema can later support:

* Salary approval workflows
* Multiple compensation components
* Bonuses
* Benefits
* Payroll integration
* Currency conversion
* Exchange-rate history
* Employee self-service
* More granular permissions
* Organizational hierarchy
* Business employee codes
* External HR/payroll integrations
* Country-specific employee metadata
* Currency-specific configuration

These are intentionally excluded from the initial implementation.

---

# 17. Final Database Structure

The initial database will therefore contain **six tables**:

```text
┌───────────────┐
│     users     │
└───────┬───────┘
        │
        ├──────────────────┐
        │                  │
        ▼                  ▼
┌────────────────┐  ┌────────────────┐
│salary_history  │  │  audit_logs    │
└───────┬────────┘  └────────────────┘
        │
        │
        ▼
┌────────────────┐
│   employees    │
└──────┬─────┬───┘
       │     │
       │     │
       ▼     ▼
┌──────────┐ ┌──────────────┐
│ countries│ │  currencies  │
└──────────┘ └──────────────┘
```

### Core principles

```text
employees
    ↓
Current employee state

salary_history
    ↓
Historical salary changes

users
    ↓
Authentication and authorization

audit_logs
    ↓
Operational traceability

countries
    ↓
Controlled country reference data

currencies
    ↓
Controlled currency reference data
```

The schema is intentionally normalized enough to maintain data consistency while remaining simple enough to implement and maintain for approximately 10,000 employees.

The `countries` and `currencies` tables are reference/master-data tables. They prevent invalid values from being stored directly in employee and salary records and provide a clean foundation for future country- and currency-specific functionality.
