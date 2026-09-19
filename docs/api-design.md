# ACME Salary Management — API Design

## 1. Overview

The frontend communicates with the Spring Boot backend through REST APIs.

Base API path:

```text
/api
```

Authentication uses JWT.

All protected endpoints require:

```http
Authorization: Bearer <JWT>
```

The API is designed around the following core resources:

```text
Authentication
Employees
Countries
Currencies
Salary History
Analytics
```

---

# 2. API Conventions

## Content Type

Requests and responses use:

```http
Content-Type: application/json
```

## HTTP Status Codes

| Status | Usage                                    |
| ------ | ---------------------------------------- |
| 200    | Successful request                       |
| 201    | Resource created                         |
| 204    | Successful request with no response body |
| 400    | Invalid request                          |
| 401    | Authentication required or invalid       |
| 403    | Insufficient permissions                 |
| 404    | Resource not found                       |
| 409    | Conflict                                 |
| 500    | Unexpected server error                  |

---

# 3. Authentication APIs

## POST `/api/auth/login`

Authenticates an application user.

### Request

```json
{
  "username": "admin",
  "password": "Admin@123"
}
```

### Response

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "username": "admin",
    "role": "HR_MANAGER"
  }
}
```

### Errors

Invalid credentials:

```text
401 Unauthorized
```

---

# 4. Country APIs

Countries are stored in a separate reference table.

The frontend uses this API to populate country dropdowns and filters.

## GET `/api/countries`

Returns the available countries.

### Example Response

```json
[
  {
    "id": 1,
    "name": "India",
    "code": "IN"
  },
  {
    "id": 2,
    "name": "United States",
    "code": "US"
  },
  {
    "id": 3,
    "name": "United Kingdom",
    "code": "GB"
  }
]
```

The country `id` is used when creating or updating employee information.

---

# 5. Currency APIs

Currencies are stored in a separate reference table.

The frontend uses this API to populate currency dropdowns.

## GET `/api/currencies`

Returns the available currencies.

### Example Response

```json
[
  {
    "id": 1,
    "code": "INR",
    "name": "Indian Rupee",
    "symbol": "₹"
  },
  {
    "id": 2,
    "code": "USD",
    "name": "US Dollar",
    "symbol": "$"
  },
  {
    "id": 3,
    "code": "GBP",
    "name": "British Pound",
    "symbol": "£"
  }
]
```

The currency `id` is used when creating or updating employee salary information.

---

# 6. Employee APIs

## GET `/api/employees`

Returns a paginated employee list.

### Query Parameters

| Parameter  | Required | Description                 |
| ---------- | -------- | --------------------------- |
| page       | No       | Zero-based page number      |
| size       | No       | Number of records           |
| search     | No       | Employee name or email      |
| countryId  | No       | Country filter              |
| department | No       | Department filter           |
| sort       | No       | Sorting field and direction |

### Example

```http
GET /api/employees?page=0&size=20
```

### Search

```http
GET /api/employees?page=0&size=20&search=john
```

Search supports:

```text
first name
last name
email
```

### Country Filter

```http
GET /api/employees?page=0&size=20&countryId=1
```

### Department Filter

```http
GET /api/employees?page=0&size=20&department=Engineering
```

### Combined Filters

```http
GET /api/employees?page=0&size=20&countryId=1&department=Engineering
```

### Sorting

```http
GET /api/employees?page=0&size=20&sort=currentSalary,desc
```

### Response

```json
{
  "content": [
    {
      "id": 123,
      "firstName": "John",
      "lastName": "Smith",
      "email": "john.smith@acme.example",
      "country": {
        "id": 2,
        "name": "United States",
        "code": "US"
      },
      "department": "Engineering",
      "designation": "Senior Software Engineer",
      "currentSalary": 120000.00,
      "currency": {
        "id": 2,
        "code": "USD",
        "name": "US Dollar",
        "symbol": "$"
      }
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 10000,
  "totalPages": 500
}
```

---

# 7. Get Employee Details

## GET `/api/employees/{employeeId}`

Returns detailed information about an employee.

### Example

```http
GET /api/employees/123
```

### Response

```json
{
  "id": 123,
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@acme.example",
  "country": {
    "id": 2,
    "name": "United States",
    "code": "US"
  },
  "department": "Engineering",
  "designation": "Senior Software Engineer",
  "currentSalary": 120000.00,
  "currency": {
    "id": 2,
    "code": "USD",
    "name": "US Dollar",
    "symbol": "$"
  },
  "createdAt": "2024-01-10T10:15:30Z",
  "updatedAt": "2026-09-19T09:20:00Z"
}
```

### Errors

Employee does not exist:

```text
404 Not Found
```

---

# 8. Salary History

## GET `/api/employees/{employeeId}/salary-history`

Returns salary history for an employee.

### Example

```http
GET /api/employees/123/salary-history
```

### Response

```json
[
  {
    "id": 10,
    "previousSalary": 105000.00,
    "newSalary": 120000.00,
    "currency": {
      "id": 2,
      "code": "USD",
      "name": "US Dollar",
      "symbol": "$"
    },
    "effectiveDate": "2026-09-19",
    "changedBy": "admin",
    "createdAt": "2026-09-19T09:20:00Z"
  },
  {
    "id": 7,
    "previousSalary": 95000.00,
    "newSalary": 105000.00,
    "currency": {
      "id": 2,
      "code": "USD",
      "name": "US Dollar",
      "symbol": "$"
    },
    "effectiveDate": "2026-01-01",
    "changedBy": "admin",
    "createdAt": "2026-01-01T10:00:00Z"
  }
]
```

History should normally be returned newest first.

---

# 9. Update Employee Salary

## PUT `/api/employees/{employeeId}/salary`

Updates an employee's current salary.

The currency is selected using the currency reference ID.

### Request

```json
{
  "newSalary": 125000.00,
  "currencyId": 2,
  "effectiveDate": "2026-10-01"
}
```

### Processing

The backend performs the following operations within one transaction:

```text
1. Find employee
2. Validate employee
3. Validate currency
4. Capture previous salary
5. Update current salary
6. Update employee currency if required
7. Create salary history
8. Create audit log
9. Commit transaction
```

If any operation fails:

```text
ROLLBACK
```

### Response

```json
{
  "employeeId": 123,
  "previousSalary": 120000.00,
  "newSalary": 125000.00,
  "currency": {
    "id": 2,
    "code": "USD",
    "name": "US Dollar",
    "symbol": "$"
  },
  "effectiveDate": "2026-10-01",
  "updatedBy": "admin",
  "updatedAt": "2026-09-19T10:00:00Z"
}
```

### Validation

The API rejects:

```text
newSalary < 0
```

Missing:

```text
newSalary
currencyId
effectiveDate
```

Invalid currency:

```text
404 Not Found
```

Invalid employee:

```text
404 Not Found
```

---

# 10. Analytics APIs

## GET `/api/analytics/overview`

Returns high-level organization statistics.

### Response

```json
{
  "totalEmployees": 10000,
  "totalCountries": 8,
  "totalDepartments": 7
}
```

Salary totals should be presented with appropriate currency context rather than blindly aggregating different currencies.

---

# 11. Salary by Country

## GET `/api/analytics/by-country`

Returns employee and salary statistics grouped by country.

### Response

```json
[
  {
    "country": {
      "id": 2,
      "name": "United States",
      "code": "US"
    },
    "employeeCount": 2150,
    "currency": {
      "id": 2,
      "code": "USD",
      "name": "US Dollar",
      "symbol": "$"
    },
    "averageSalary": 112500.00,
    "minimumSalary": 45000.00,
    "maximumSalary": 250000.00
  },
  {
    "country": {
      "id": 1,
      "name": "India",
      "code": "IN"
    },
    "employeeCount": 3200,
    "currency": {
      "id": 1,
      "code": "INR",
      "name": "Indian Rupee",
      "symbol": "₹"
    },
    "averageSalary": 2100000.00,
    "minimumSalary": 500000.00,
    "maximumSalary": 6000000.00
  }
]
```

Salary statistics should be calculated within an appropriate currency context.

---

# 12. Salary by Department

## GET `/api/analytics/by-department`

Returns salary statistics grouped by department.

Because employees in the same department may have different currencies, the API should group salary statistics by currency as necessary rather than aggregating incompatible currencies.

### Example Response

```json
[
  {
    "department": "Engineering",
    "currency": {
      "id": 2,
      "code": "USD",
      "name": "US Dollar",
      "symbol": "$"
    },
    "employeeCount": 850,
    "averageSalary": 95000.00
  },
  {
    "department": "Engineering",
    "currency": {
      "id": 1,
      "code": "INR",
      "name": "Indian Rupee",
      "symbol": "₹"
    },
    "employeeCount": 1250,
    "averageSalary": 2100000.00
  }
]
```

The API must not calculate a single raw average across INR, USD, GBP, EUR, or other currencies.

---

# 13. Salary Distribution

## GET `/api/analytics/salary-distribution`

Returns employees grouped into salary ranges.

Because salary ranges are currency-dependent, the API should require a currency context.

### Query Parameters

| Parameter  | Required | Description                           |
| ---------- | -------- | ------------------------------------- |
| currencyId | Yes      | Currency used for salary distribution |
| countryId  | No       | Optional country filter               |
| department | No       | Optional department filter            |

### Example

```http
GET /api/analytics/salary-distribution?currencyId=2
```

### Response

```json
[
  {
    "range": "0-50000",
    "employeeCount": 850
  },
  {
    "range": "50001-100000",
    "employeeCount": 3200
  },
  {
    "range": "100001-150000",
    "employeeCount": 4100
  },
  {
    "range": "150001-200000",
    "employeeCount": 1500
  },
  {
    "range": "200000+",
    "employeeCount": 350
  }
]
```

The ranges are interpreted according to the selected currency.

---

# 14. Error Response Format

All APIs should return a consistent error structure.

### Example

```json
{
  "timestamp": "2026-09-19T10:00:00Z",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Employee not found",
  "path": "/api/employees/123"
}
```

### Validation Error

```json
{
  "timestamp": "2026-09-19T10:00:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "path": "/api/employees/123/salary",
  "fieldErrors": {
    "newSalary": "Salary must be greater than or equal to zero"
  }
}
```

---

# 15. API Security

## Public Endpoint

```text
POST /api/auth/login
```

## Protected Endpoints

```text
/api/employees/**
/api/countries
/api/currencies
/api/analytics/**
```

Salary modification requires:

```text
ROLE_HR_MANAGER
```

Every protected request requires a valid JWT.

---

# 16. Pagination Strategy

Employee data will always be paginated.

Default:

```text
page = 0
size = 20
```

Maximum:

```text
size = 100
```

This prevents clients from accidentally requesting all 10,000 employees in a single request.

Salary history may also be paginated if the history volume becomes large.

---

# 17. Search Strategy

Employee search initially supports:

```text
first name
last name
email
```

Example:

```http
GET /api/employees?search=john
```

Search will be implemented on the backend rather than downloading all employees to Angular.

---

# 18. Filtering Strategy

Supported employee filters:

```text
countryId
department
```

Examples:

```http
GET /api/employees?countryId=1
```

```http
GET /api/employees?department=Engineering
```

Combined:

```http
GET /api/employees?countryId=1&department=Engineering
```

Country filtering uses `countryId` rather than a free-text country value.

This ensures that employee records reference an existing country.

---

# 19. Sorting Strategy

Supported sorting fields will be explicitly whitelisted by the backend.

Examples:

```text
firstName
lastName
country
department
currentSalary
```

Example:

```http
GET /api/employees?sort=currentSalary,desc
```

The API must not directly concatenate arbitrary client-provided SQL fragments.

---

# 20. API-to-UI Mapping

| UI Screen            | APIs                                     |
| -------------------- | ---------------------------------------- |
| Login                | `POST /api/auth/login`                   |
| Dashboard            | `GET /api/analytics/overview`            |
| Employee List        | `GET /api/employees`                     |
| Employee Details     | `GET /api/employees/{id}`                |
| Salary History       | `GET /api/employees/{id}/salary-history` |
| Update Salary        | `PUT /api/employees/{id}/salary`         |
| Country Dropdown     | `GET /api/countries`                     |
| Currency Dropdown    | `GET /api/currencies`                    |
| Country Analytics    | `GET /api/analytics/by-country`          |
| Department Analytics | `GET /api/analytics/by-department`       |
| Salary Distribution  | `GET /api/analytics/salary-distribution` |

---

# 21. Frontend Dropdown Flow

When the employee/salary form is opened, Angular loads the reference data.

```text
Angular
   │
   ├── GET /api/countries
   │
   └── GET /api/currencies
            │
            ▼
       Populate dropdowns
```

Example country dropdown:

```text
Country
┌─────────────────────────────┐
│ Select Country           ▼  │
├─────────────────────────────┤
│ India                       │
│ United States               │
│ United Kingdom              │
│ Germany                     │
│ Canada                      │
└─────────────────────────────┘
```

Example currency dropdown:

```text
Currency
┌─────────────────────────────┐
│ Select Currency          ▼  │
├─────────────────────────────┤
│ INR - Indian Rupee          │
│ USD - US Dollar             │
│ GBP - British Pound         │
│ EUR - Euro                  │
└─────────────────────────────┘
```

The frontend submits the selected IDs rather than the display text.

Example:

```json
{
  "newSalary": 125000.00,
  "currencyId": 2,
  "effectiveDate": "2026-10-01"
}
```

---

# 22. API Design Principles

The API follows these principles:

* RESTful resource naming.
* HTTP status codes represent operation results.
* DTOs are used instead of exposing JPA entities directly.
* Validation occurs at the API boundary and business layer.
* Pagination is mandatory for large employee collections.
* Filtering and sorting are performed server-side.
* Sensitive operations require authentication and authorization.
* Salary updates are transactional.
* Errors use a consistent response format.
* Foreign keys are used for country and currency reference data.
* The frontend uses reference-data APIs for dropdown options.
* Salary analytics do not blindly aggregate incompatible currencies.
* API behavior should be covered by automated tests.

---

# 23. Final API List

```text
Authentication
────────────────────────────────────────
POST   /api/auth/login


Employees
────────────────────────────────────────
GET    /api/employees
GET    /api/employees/{employeeId}
GET    /api/employees/{employeeId}/salary-history
PUT    /api/employees/{employeeId}/salary


Reference Data
────────────────────────────────────────
GET    /api/countries
GET    /api/currencies


Analytics
────────────────────────────────────────
GET    /api/analytics/overview
GET    /api/analytics/by-country
GET    /api/analytics/by-department
GET    /api/analytics/salary-distribution

