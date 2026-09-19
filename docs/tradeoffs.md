# ACME Salary Management — Technical Tradeoffs

## 1. Purpose

This document records the major technical and architectural decisions made for the ACME Salary Management system, including alternatives considered, reasons for the selected approach, and known tradeoffs.

The goal is to demonstrate intentional engineering decisions rather than choosing technologies or patterns without justification.

---

## 2. Monolithic Application vs Microservices

### Decision

Use a **modular monolithic architecture** with Angular as the frontend and Spring Boot as the backend.

### Alternatives Considered

* Microservices
* Modular monolith

### Why Modular Monolith

The application is an internal salary management system with approximately 10,000 employees. The expected scale does not justify the operational complexity of multiple independently deployed services.

The modular monolith provides:

* Simpler development
* Easier local setup
* Easier deployment
* Transactional consistency
* Lower infrastructure overhead
* Clear separation between application modules

The backend will still be organized into logical modules:

```text
auth
employees
salary
analytics
reference-data
audit
```

### Tradeoff

A monolith provides less independent scalability than microservices. However, the current requirements do not require independent service scaling.

If the system grows significantly, modules can later be extracted into services.

---

## 3. Angular vs React

### Decision

Use **Angular** for the frontend.

### Reason

The assessment allows React/Next.js or Angular for a Java-oriented role. Angular was selected because it provides an opinionated enterprise application structure and integrates naturally with:

* TypeScript
* Angular Material
* Reactive Forms
* Angular Router
* HttpClient
* RxJS
* NgRx

This is suitable for an administrative application containing forms, tables, authentication, filters, analytics, and state management.

### Tradeoff

Angular has a larger framework footprint and steeper learning curve than a minimal React application.

The benefit is stronger application structure and consistency for a larger enterprise-style application.

---

## 4. NgRx vs Local Component State

### Decision

Use **NgRx** for shared application state and local component state for temporary UI state.

### NgRx is Used For

* Authentication state
* Employee collection
* Selected employee
* Salary history
* Countries
* Currencies
* Analytics
* Loading/error states associated with shared data

### Local Component State is Used For

* Dialog visibility
* Temporary form values
* UI-only toggles
* Temporary interaction state

### Reason

The application contains multiple screens that use related data. Centralized state makes data flow predictable and avoids unnecessary duplicated API calls.

### Tradeoff

NgRx introduces additional:

* Actions
* Reducers
* Effects
* Selectors
* Boilerplate

For a small application, this could be excessive. However, it demonstrates scalable state-management practices appropriate for an enterprise application.

---

## 5. MySQL vs NoSQL

### Decision

Use **MySQL** as the primary database.

### Reason

Salary management requires strongly structured and relational data:

```text
employees
countries
currencies
users
salary_history
audit_logs
```

The system requires:

* Foreign keys
* Transactions
* Consistency
* Structured relationships
* Filtering and sorting
* Aggregation queries

A relational database is therefore appropriate.

### Tradeoff

Schema changes require migrations and careful database design.

This is acceptable because data consistency is more important than schema flexibility for this system.

---

## 6. JPA/Hibernate vs JDBC

### Decision

Use **Spring Data JPA with Hibernate** for standard CRUD operations.

### Reason

The application contains several related entities and standard persistence operations. JPA provides:

* Entity mapping
* Repository abstraction
* Transaction management
* Pagination
* Sorting
* Query support

For analytics or performance-sensitive aggregation queries, optimized JPQL/native SQL can be introduced where appropriate.

### Tradeoff

JPA can generate inefficient queries if relationships and fetching strategies are not designed carefully.

To avoid this:

* Avoid unnecessary eager relationships
* Use pagination
* Select only required fields for analytics
* Monitor generated SQL
* Avoid loading large object graphs

---

## 7. Flyway vs Hibernate Schema Generation

### Decision

Use **Flyway** for database migrations.

### Reason

Database structure should be version-controlled and reproducible across environments.

Example:

```text
V1__create_users.sql
V2__create_reference_tables.sql
V3__create_employees.sql
V4__create_salary_history.sql
V5__create_audit_logs.sql
V6__seed_reference_data.sql
```

Hibernate should not be responsible for production schema evolution.

### Tradeoff

Developers need to maintain migration scripts.

The benefit is predictable database versioning and safer deployments.

---

## 8. Employee Identifier Design

### Decision

Use the database-generated employee `id` as the employee identifier.

No separate `employee_code` is required.

Example:

```text
employees
---------
id
first_name
last_name
email
...
```

### Reason

The assessment does not require a business-specific employee code.

Removing it avoids:

* Redundant identifiers
* Additional uniqueness rules
* Extra generation logic
* Unnecessary database indexes

### Tradeoff

If ACME later requires a human-readable employee identifier, a separate business identifier can be introduced without changing the database primary key.

---

## 9. Country and Currency Reference Tables

### Decision

Use separate `countries` and `currencies` tables.

Employees reference these tables using foreign keys.

### Reason

Country and currency are standardized reference data.

This prevents inconsistent values such as:

```text
India
india
INDIA
Ind
```

or:

```text
INR
Indian Rupee
Rs
₹
```

The frontend retrieves these values from:

```text
GET /api/countries
GET /api/currencies
```

### Tradeoff

Reference data introduces additional joins and tables.

The consistency benefits outweigh the small complexity increase.

---

## 10. Department as VARCHAR

### Decision

Store department as a `VARCHAR` instead of creating a department table.

### Reason

Departments are organization-specific and may change frequently.

For the current scope, values such as:

```text
Engineering
Finance
Human Resources
Marketing
Sales
Operations
Product
```

are sufficient.

### Tradeoff

There is less referential integrity compared with a dedicated department table.

If departments later require:

* Department IDs
* Managers
* Hierarchies
* Budgets
* Department metadata

a dedicated table can be introduced.

---

## 11. Salary Currency Handling

### Decision

Store the salary amount together with its currency.

Example:

```text
current_salary = 75000.00
currency = INR
```

or:

```text
current_salary = 5000.00
currency = USD
```

### Important Rule

Salary amounts in different currencies must **not** be directly aggregated.

For example, this is invalid:

```text
INR 75,000 + USD 5,000
```

without currency conversion.

### Version 1 Approach

Analytics will remain currency-aware by grouping or filtering salary information by currency.

Automatic foreign exchange conversion is not included in Version 1.

### Tradeoff

The dashboard cannot provide a single meaningful global salary total across all countries/currencies.

Adding live FX conversion would introduce:

* External API dependency
* Exchange-rate freshness concerns
* Failure scenarios
* Additional business rules

This can be added in a future version.

---

## 12. Salary Update API Design

### Decision

Use:

```text
PUT /api/employees/{employeeId}/salary
```

instead of:

```text
PUT /api/employees/salary/{employeeId}
```

### Reason

Salary is modeled as a sub-resource of an employee.

The resource hierarchy is clearer:

```text
/api/employees/{employeeId}
/api/employees/{employeeId}/salary
/api/employees/{employeeId}/salary-history
```

This keeps employee-related resources grouped together.

---

## 13. Salary History

### Decision

Maintain a separate `salary_history` table.

Every salary change creates a history record.

Example:

```text
Previous Salary | New Salary | Effective Date | Changed By
-----------------------------------------------------------
50000           | 60000       | 2026-09-01     | HR Manager
60000           | 70000       | 2027-01-01     | HR Manager
```

### Reason

Salary changes are business-critical and should be auditable.

The current employee record represents the current state while salary history represents historical state changes.

### Tradeoff

Every salary update requires an additional database insert.

This is acceptable because auditability is more important than minimizing a single write operation.

---

## 14. Transactional Salary Updates

Salary updates will execute inside a database transaction.

The operation is conceptually:

```text
1. Load employee
2. Validate request
3. Capture previous salary
4. Update current salary
5. Insert salary history
6. Insert audit log
7. Commit
```

If any operation fails:

```text
ROLLBACK
```

This prevents inconsistent states such as:

```text
Employee salary updated
but
Salary history missing
```

Spring's `@Transactional` will be used to enforce this behavior.

---

## 15. Server-Side Pagination

### Decision

Employee lists use server-side pagination.

Default:

```text
page = 0
size = 20
```

Maximum:

```text
size = 100
```

### Reason

The system contains approximately 10,000 employees.

Loading all employees into the browser would:

* Increase network payload
* Increase browser memory usage
* Increase rendering cost
* Slow down tables

Instead:

```text
Browser
   ↓
GET /api/employees?page=0&size=20
   ↓
Database
   ↓
20 records
```

### Tradeoff

Changing pages requires API requests.

This is acceptable because the application prioritizes scalability and responsiveness.

---

## 16. Server-Side Search, Filtering and Sorting

Employee search and filtering will be performed by the backend.

Supported examples:

```text
search
countryId
department
sort
```

### Reason

Filtering 10,000 records in the browser is unnecessary and increases client-side complexity.

Database indexes can be used to improve common queries.

### Tradeoff

Each filter change may trigger an API request.

Search input will therefore use a debounce of approximately 300–500 ms.

---

## 17. Sorting Security

Sorting parameters will not be directly concatenated into SQL.

Instead, the backend will maintain an allowlist such as:

```text
firstName
lastName
department
currentSalary
```

Only supported fields can be used for sorting.

### Reason

This prevents unsafe dynamic query construction and keeps API behavior predictable.

---

## 18. JWT vs Session-Based Authentication

### Decision

Use JWT-based authentication.

### Reason

The Angular frontend communicates with the Spring Boot REST API independently.

JWT allows the API to authenticate requests using:

```text
Authorization: Bearer <token>
```

### Tradeoff

JWT introduces token lifecycle concerns such as:

* Expiration
* Token storage
* Logout handling
* Refresh strategy

For the assessment scope, short-lived access tokens are sufficient.

---

## 19. Role-Based Authorization

### Decision

Use Spring Security role-based authorization.

Example:

```text
HR_MANAGER
```

Salary modifications require the HR Manager role.

Read operations can be available to authenticated users according to the application's authorization rules.

### Reason

Authentication determines who the user is.

Authorization determines what that user can do.

The backend remains the final authority even if the Angular UI hides restricted controls.

---

## 20. Audit Logging

### Decision

Maintain a dedicated `audit_logs` table.

Important operations such as salary updates will record:

```text
user
action
entity type
entity ID
details
timestamp
```

### Reason

Salary modifications are sensitive business operations and should be traceable.

### Tradeoff

Audit logging adds database writes and storage requirements.

For an internal salary management system, traceability is valuable.

---

## 21. REST API vs GraphQL

### Decision

Use REST.

### Reason

The application's requirements map naturally to resource-based APIs:

```text
/employees
/employees/{id}
/employees/{id}/salary
/employees/{id}/salary-history
/analytics/overview
```

REST is also straightforward to test and document with OpenAPI/Swagger.

### Tradeoff

Some screens may require multiple API calls.

This is acceptable for the current scope.

---

## 22. Angular Material vs Custom UI Components

### Decision

Use Angular Material as the primary UI component library.

### Reason

It provides accessible and reusable components for:

* Tables
* Forms
* Inputs
* Dialogs
* Selects
* Buttons
* Pagination
* Navigation

This reduces development time while maintaining consistent UI behavior.

### Tradeoff

The application may have less visual uniqueness than a fully custom design system.

---

## 23. Accessibility

### Decision

Accessibility is treated as a first-class requirement.

The UI will follow WCAG-oriented practices including:

* Semantic HTML
* Proper labels
* Keyboard navigation
* Focus management
* Accessible forms
* Appropriate ARIA usage
* Screen-reader compatibility
* Sufficient contrast

### Reason

The application should be usable by employees with different accessibility needs.

---

## 24. Testing Strategy

### Backend

Use:

* JUnit 5
* Mockito
* Spring Boot Test
* MockMvc where appropriate

Test:

* Services
* Controllers
* Validation
* Security
* Salary transactions
* Error handling
* Repository/query behavior where useful

### Frontend

Use Angular testing tools to cover:

* Components
* Services
* Guards
* Interceptors
* NgRx reducers
* NgRx selectors
* NgRx effects
* Forms
* Important user flows

### Tradeoff

Writing tests increases development time.

The benefit is confidence in critical functionality, especially salary modification and authentication.

---

## 25. Performance Strategy

The system is designed for approximately 10,000 employees.

Primary strategies:

```text
Server-side pagination
Server-side filtering
Server-side sorting
Database indexes
Debounced search
Lazy-loaded Angular features
NgRx selectors
Reference-data caching
Efficient analytics queries
```

The application should avoid transferring or rendering all 10,000 employee records in the browser.

---

## 26. Seed Data Strategy

### Decision

Generate approximately 10,000 deterministic employee records.

### Reason

The assessment specifically requires realistic data volume.

Deterministic seed data provides:

* Repeatable development environments
* Predictable testing
* Performance testing
* Demonstrable pagination/filtering behavior

---

## 27. Docker

### Decision

Containerize the application using Docker.

The local environment can contain:

```text
Angular Application
       ↓
Spring Boot API
       ↓
MySQL
```

Docker Compose can be used to simplify local startup.

### Tradeoff

Docker adds configuration complexity.

The benefit is reproducible environments and easier deployment.

---

## 28. Deployment

The final application should be deployed so the evaluator can access the system without building it locally.

Deployment should include:

* Frontend
* Backend
* Database
* Environment configuration
* Production API URL
* Authentication configuration

Secrets must not be committed to Git.

---

## 29. Incremental Git Commits

Development should be committed in meaningful increments rather than one final commit.

Example:

```text
chore: initialize project structure
docs: add system requirements
docs: add architecture design
docs: add database design
docs: add API design
docs: add UI design
feat: add database migrations
feat: add employee management APIs
feat: add salary management
feat: add JWT authentication
feat: add analytics APIs
test: add salary service tests
feat: add Angular employee management
feat: add NgRx state management
test: add frontend tests
chore: add Docker configuration
docs: add deployment instructions
```

This provides a clear development history.

---

## 30. Final Decision Summary

| Area                | Decision                        |
| ------------------- | ------------------------------- |
| Architecture        | Modular Monolith                |
| Backend             | Java + Spring Boot              |
| Frontend            | Angular                         |
| UI                  | Angular Material                |
| State Management    | NgRx                            |
| Database            | MySQL                           |
| ORM                 | Spring Data JPA / Hibernate     |
| Migration           | Flyway                          |
| Authentication      | JWT                             |
| Authorization       | Spring Security                 |
| API                 | REST                            |
| Documentation       | OpenAPI / Swagger               |
| Testing             | JUnit + Angular testing tools   |
| Containerization    | Docker                          |
| Local orchestration | Docker Compose                  |
| Employee ID         | Database-generated `id`         |
| Country             | Reference table                 |
| Currency            | Reference table                 |
| Department          | VARCHAR                         |
| Salary History      | Separate table                  |
| Audit               | Separate audit table            |
| Pagination          | Server-side                     |
| Search              | Server-side                     |
| Sorting             | Server-side allowlist           |
| Analytics           | Currency-aware                  |
| FX conversion       | Not included in V1              |
| Seed Data           | ~10,000 deterministic employees |

---

## 31. Future Improvements

Potential future enhancements include:

* Refresh tokens
* Multi-level roles and permissions
* Department master table
* Employee creation/editing
* Bulk salary updates
* CSV/Excel import/export
* Salary approval workflow
* Currency conversion using an FX provider
* Advanced reporting
* Notification system
* Full audit-log viewer
* Caching layer
* Redis
* Read replicas
* Service decomposition if scale requires it
