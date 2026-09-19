# ACME Salary Management — Requirements

## 1. Goal

Build a web-based salary management system that enables ACME's HR Manager to manage employee salary information and quickly understand how the organization pays its employees across countries and departments.

The system will replace spreadsheet-based salary management with a centralized, searchable, auditable, and easy-to-use application.

## 2. User Persona

**Primary user:** HR Manager

The HR Manager needs to:

- Search and view employee salary information.
- Filter employees by country and department.
- Update an employee's salary.
- View salary change history.
- Understand salary distribution across countries and departments.
- Access the information efficiently for an organization of approximately 10,000 employees.

## 3. Scope & Features

### Authentication

- HR Manager login.
- Secure authentication using JWT.
- Role-based authorization for protected APIs.

### Employee Management

- View employees with server-side pagination.
- Search employees by name, employee ID, or email.
- Filter by country and department.
- Sort employee records.
- View detailed employee information.

### Salary Management

- View an employee's current salary and currency.
- Update an employee's salary with an effective date.
- Maintain salary history instead of overwriting historical information.
- Validate salary updates.
- Record who performed a salary change.

### Salary Analytics

Provide the HR Manager with an overview including:

- Total number of employees.
- Employee distribution by country.
- Employee distribution by department.
- Salary statistics by country.
- Salary statistics by department.
- Salary distribution across salary ranges.

Salary analytics will respect currency boundaries. Salaries in different currencies will not be directly aggregated unless an explicit currency-conversion capability is introduced later.

### Auditability

- Record important salary-related changes.
- Store the user responsible for the change, affected employee, previous value, new value, and timestamp.

## 4. Non-Functional Requirements

- Support approximately 10,000 employee records.
- Use server-side pagination, filtering, sorting, and search to avoid loading the complete employee dataset into the browser.
- Use database indexes for frequently queried employee fields.
- Provide appropriate API validation and error handling.
- Protect sensitive salary information through authentication and authorization.
- Maintain a clean separation between controllers, services, repositories, DTOs, and entities.
- Include meaningful automated tests for core business logic and APIs.
- Provide Docker-based local setup and a deployable production build.

## 5. Deliberately Out of Scope

The following features will not be implemented in this version:

- Payroll processing and salary payment.
- Tax, PF, TDS, or benefits calculations.
- Payslip generation.
- Employee self-service portal.
- Employee registration.
- SSO, MFA, or enterprise identity-provider integration.
- Complex multi-level salary approval workflows.
- Mobile applications.
- Automatic foreign-exchange conversion.
- Integration with external HR/payroll systems.

These features are excluded to keep the assessment focused on the core problem: **centralized salary management and salary-related insights for HR**. They can be added later if business requirements justify the additional complexity.

## 6. Assumptions

- The initial application has one primary persona: HR Manager.
- ACME provides employee and salary data.
- Each employee has a current salary and associated currency.
- Salary changes should preserve historical records.
- The initial dataset contains approximately 10,000 employees.
- The application is an internal HR tool and does not expose salary information publicly.

## 7. Success Criteria

The solution will be considered successful when an HR Manager can:

1. Log in securely.
2. Search, filter, sort, and paginate approximately 10,000 employees.
3. View an employee's current salary and details.
4. Update salary information while preserving salary history.
5. See who changed a salary and when.
6. View useful salary analytics by country and department.
7. Use the application reliably through the deployed web interface.

## 8. Technology Direction

- **Frontend:** Angular
- **Backend:** Java + Spring Boot
- **Database:** MySQL
- **API:** REST
- **Authentication:** Spring Security + JWT
- **Database migrations:** Flyway
- **Testing:** JUnit, Mockito, Spring Boot Test, and Angular unit tests
- **Deployment:** Docker-based deployment