# ACME Salary Management — AI Usage

## 1. Purpose

AI tools are intentionally used during the development of the ACME Salary Management system to improve productivity while keeping engineering decisions, validation, testing, and final implementation under developer control.

AI is treated as an engineering assistant rather than the sole source of implementation decisions.

---

## 2. AI Usage Principles

The following principles are followed:

1. Understand the generated solution before using it.
2. Validate generated code against project requirements.
3. Review security-sensitive code manually.
4. Test generated code.
5. Avoid blindly copying generated implementations.
6. Keep architecture and final technical decisions developer-owned.
7. Document important AI-assisted decisions.
8. Do not expose secrets, passwords, tokens, or private credentials to AI tools.

---

## 3. Areas Where AI Is Used

AI assistance is used or may be used throughout the project for:

### Requirements

* Clarifying ambiguous requirements
* Identifying missing acceptance criteria
* Converting requirements into implementation tasks

### Architecture

* Comparing architectural alternatives
* Reviewing module boundaries
* Identifying scalability concerns

### Database

* Reviewing entity relationships
* Suggesting indexes
* Reviewing SQL queries
* Identifying normalization opportunities

### Backend

* Spring Boot boilerplate
* DTO structures
* Controller/service/repository patterns
* Validation
* Exception handling
* Security configuration
* Test case generation
* Query optimization suggestions

### Frontend

* Angular component structure
* Reactive form implementation
* Angular Material usage
* NgRx actions, reducers, effects, and selectors
* HTTP services
* Guards and interceptors
* Test-case suggestions
* Accessibility review

### Testing

* Identifying edge cases
* Generating test scenarios
* Reviewing test coverage
* Suggesting negative test cases

### DevOps

* Dockerfile suggestions
* Docker Compose configuration
* Environment configuration
* Deployment troubleshooting

### Documentation

* Architecture documentation
* API documentation
* Tradeoff documentation
* Test documentation
* AI usage documentation

---

## 4. AI Prompt Examples

The following prompts represent the type of prompts used during development.

### 4.1 Requirements Analysis

```text
I need to build an internal salary management application for approximately
10,000 employees.

The system should allow an HR Manager to:
- authenticate
- view dashboard analytics
- search employees
- filter employees by country and department
- view employee details
- update employee salary
- view salary history
- view salary analytics

Identify missing functional requirements, non-functional requirements,
validation rules, and edge cases before implementation.
```

### 4.2 Architecture Review

```text
Design a production-oriented architecture for an employee salary management
system with approximately 10,000 employees.

Frontend:
Angular

Backend:
Java Spring Boot

Database:
MySQL

Requirements:
- JWT authentication
- role-based authorization
- employee search/filter/sorting
- salary updates
- salary history
- audit logs
- analytics

Compare a modular monolith against microservices and explain the tradeoffs.
```

### 4.3 Database Design

```text
Review this database design for a salary management application.

Tables:
users
employees
countries
currencies
salary_history
audit_logs

Employees have a current salary and currency.
Salary changes must be historically tracked.
Approximately 10,000 employees will be stored.

Identify:
- missing constraints
- useful indexes
- normalization problems
- transaction concerns
- query performance concerns
```

### 4.4 API Design

```text
Review the REST API design for an Angular + Spring Boot salary management
application.

Endpoints include:

POST /api/auth/login
GET /api/employees
GET /api/employees/{employeeId}
GET /api/employees/{employeeId}/salary-history
PUT /api/employees/{employeeId}/salary
GET /api/analytics/overview
GET /api/analytics/by-country
GET /api/analytics/by-department

Review the API hierarchy, HTTP methods, status codes, validation,
authorization, pagination, sorting, and error handling.
```

### 4.5 Spring Boot Implementation

```text
Create a Spring Boot implementation for updating an employee salary.

Requirements:
1. Find employee by ID.
2. Validate the new salary.
3. Capture the previous salary.
4. Update the current salary.
5. Insert salary history.
6. Insert an audit log.
7. Execute the complete operation inside one transaction.
8. Roll back if any operation fails.
9. Restrict the endpoint to HR_MANAGER.
10. Return a meaningful API response.
```

Generated code must be reviewed and adapted to the actual project structure before being committed.

### 4.6 NgRx Design

```text
Design NgRx state management for an Angular salary management application.

State domains:
- auth
- employees
- countries
- currencies
- analytics

Employees require:
- server-side pagination
- search
- country filtering
- department filtering
- sorting
- selected employee
- salary history
- loading state
- error state

Provide actions, reducers, selectors, and effects while keeping temporary
dialog/form state local to components.
```

### 4.7 Performance Review

```text
Review the performance strategy for an Angular + Spring Boot application
containing approximately 10,000 employees.

The employee table supports:
- search
- country filter
- department filter
- sorting
- pagination

Identify frontend and backend performance risks and recommend improvements
without introducing unnecessary infrastructure.
```

### 4.8 Accessibility Review

```text
Review this Angular employee management UI for accessibility.

Check:
- semantic HTML
- form labels
- keyboard navigation
- focus management
- ARIA usage
- table accessibility
- error messages
- screen-reader behavior
- color contrast

Provide concrete improvements based on WCAG-oriented practices.
```

### 4.9 Test Generation

```text
Generate meaningful unit and integration test scenarios for the salary
update functionality.

Include:
- employee not found
- invalid salary
- unauthorized user
- authorized HR Manager
- successful salary update
- salary history creation
- audit log creation
- transaction rollback
- duplicate request handling
- validation failures
```

---

## 5. AI-Assisted Code Review Process

AI-assisted implementation follows this process:

```text
Requirement
    ↓
AI assistance
    ↓
Developer review
    ↓
Project-specific modification
    ↓
Compile/build
    ↓
Unit tests
    ↓
Integration testing
    ↓
Manual verification
    ↓
Commit
```

AI-generated code is not considered complete merely because it compiles.

---

## 6. Security Review of AI-Assisted Code

Security-sensitive code receives additional manual review.

Particular attention is given to:

### Authentication

* Password hashing
* JWT validation
* Token expiration
* Authentication failures

### Authorization

* Role checks
* Backend authorization
* Protected endpoints

### Database

* SQL injection prevention
* Parameterized queries
* Safe sorting and filtering

### Input Validation

* Salary validation
* Employee ID validation
* Request body validation

### Secrets

The following must never be hardcoded:

```text
database passwords
JWT secrets
API keys
cloud credentials
access tokens
```

Environment variables or deployment secret management should be used.

---

## 7. Human Verification

The developer remains responsible for:

* Architecture
* Requirements interpretation
* Security decisions
* Database design
* API contracts
* Code review
* Test validation
* Performance validation
* Deployment
* Final acceptance

AI output is considered a suggestion until it has been reviewed and tested.

---

## 8. AI and Testing

AI may help generate test cases, but tests are reviewed to ensure that they verify actual business behavior.

For example, a salary update test should verify more than HTTP `200`.

It should verify that:

```text
employee.current_salary
        ↓
updated

salary_history
        ↓
created

audit_logs
        ↓
created
```

and that these operations occur transactionally.

---

## 9. AI and Performance

AI suggestions related to performance are validated against the actual application.

Examples include:

* Database query execution
* Pagination behavior
* API response size
* Angular rendering
* Network requests
* NgRx state updates
* Lighthouse metrics where applicable

No optimization is introduced solely because an AI tool recommends it.

---

## 10. AI and Documentation

AI may assist in drafting documentation, but the final documentation must reflect the actual implementation.

Documentation should not claim that a feature exists if it has not been implemented and tested.

The project documentation consists of:

```text
docs/
├── requirement.md
├── architecture.md
├── database-design.md
├── api-design.md
├── ui-design.md
├── tradeoffs.md
└── ai-usage.md
```

These documents act as the project's source of truth.

---

## 11. Intentional Use of Agentic AI

AI tools may be used not only for generating code but also for structured engineering workflows.

Examples include:

* Breaking requirements into implementation tasks
* Reviewing architecture
* Reviewing generated code
* Identifying edge cases
* Generating test scenarios
* Reviewing API contracts
* Investigating build errors
* Reviewing accessibility
* Reviewing performance
* Improving documentation

The developer decides when AI assistance is appropriate and validates the result before integration.

---

## 12. Example Debugging Workflow

When an implementation error occurs:

```text
Error
 ↓
Understand the error
 ↓
Collect relevant logs/code
 ↓
Ask AI for possible causes
 ↓
Evaluate suggestions
 ↓
Test the likely cause
 ↓
Implement the fix
 ↓
Run tests
 ↓
Verify no regression
```

AI should assist with investigation rather than replace debugging and verification.

---

## 13. What AI Should Not Decide Automatically

The following decisions remain developer-controlled:

* Final architecture
* Security model
* Authorization rules
* Database relationships
* Business rules
* Salary calculation rules
* Production infrastructure
* Secret management
* Acceptance criteria

AI suggestions must be evaluated against the assessment requirements and actual project constraints.

---

## 14. AI-Assisted Development Record

This section records significant AI-assisted engineering activities performed during the development of the ACME Salary Management system.

The record is updated throughout the project. Only meaningful AI-assisted decisions, reviews, or implementation activities are recorded.

| Date       | Area         | Prompt / Task                                                                  | AI Contribution                                                                                              | Developer Decision                                  |
| ---------- | ------------ | ------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ | --------------------------------------------------- |
| 2026-09-19 | Requirements | Review salary management requirements for approximately 10,000 employees       | Identified functional requirements, non-functional requirements, validation needs, and edge cases            | Requirements were refined and documented            |
| 2026-09-19 | Architecture | Compare modular monolith and microservices for the application                 | Identified scalability, deployment, transaction, and operational tradeoffs                                   | Selected modular monolith                           |
| 2026-09-19 | Database     | Review employee, salary history, country, currency, user, and audit-log schema | Identified relationships, constraints, and indexing considerations                                           | Adopted the relational MySQL design                 |
| 2026-09-19 | API          | Review REST resource hierarchy for employee salary operations                  | Compared salary endpoint structures                                                                          | Selected `/api/employees/{employeeId}/salary`       |
| 2026-09-19 | Frontend     | Design Angular state management                                                | Identified shared state domains and NgRx patterns                                                            | Selected NgRx for shared application state          |
| 2026-09-19 | UI Design    | Review employee management UI architecture                                     | Suggested server-side pagination, filtering, sorting, loading/error states, and accessibility considerations | Incorporated these requirements into `ui-design.md` |

### Record Maintenance

New entries should be added when AI is used for significant development activities such as:

* Architecture decisions
* Database/query design
* API design
* Security review
* Backend implementation
* Frontend implementation
* NgRx design
* Performance optimization
* Accessibility review
* Test design
* Debugging
* Docker/deployment
* Refactoring

Minor AI interactions or simple syntax assistance do not need to be recorded.

The record should describe the **actual AI contribution and the developer's final decision**, rather than presenting AI-generated suggestions as automatically accepted solutions.


---

## 15. Final AI Usage Statement

AI is used as a productivity and engineering-support tool throughout the project.

The final implementation is reviewed, modified, tested, and validated by the developer. Architectural, security, business, and production decisions remain under developer control.

The objective is to demonstrate **intentional and responsible AI-assisted engineering**, not simply AI-generated code.
