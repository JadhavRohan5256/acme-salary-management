# ACME Salary Management — Backend

Spring Boot REST API for managing employees, salaries, salary history, reference data, authentication, and analytics.

The backend is built using **Java, Spring Boot, Spring Data JPA, MySQL, Spring Security, JWT, Flyway, Maven, and Docker**.

---

# 1. Overview

The ACME Salary Management backend provides REST APIs for:

* JWT authentication
* Employee management
* Employee search and filtering
* Country reference data
* Currency reference data
* Salary updates
* Salary history
* Salary analytics
* Centralized error handling
* API validation
* Role-based authorization

Base API path:

```text
/api
```

---

# 2. Hosted Server

The backend application is hosted on a remote server and can be accessed through the hosted Swagger UI.

## Hosted Swagger UI

**Swagger API Documentation:**

https://acme-salary-management-1-3fwm.onrender.com/swagger-ui/index.html

Use the hosted Swagger UI to explore and test the available REST APIs.

### Hosted Backend URL

```text
https://acme-salary-management-1-3fwm.onrender.com
```

### Hosted Login Endpoint

```text
POST https://acme-salary-management-1-3fwm.onrender.com/api/auth/login
```

The hosted Swagger UI provides an interactive interface for testing the backend APIs.

> **Note:** The application is hosted on Render. Depending on the hosting platform's instance lifecycle, the first request may take a little longer if the service needs to wake up.

---

# 3. Technology Stack

| Technology      | Purpose                          |
| --------------- | -------------------------------- |
| Java 21         | Application runtime              |
| Spring Boot     | Backend framework                |
| Spring Web      | REST APIs                        |
| Spring Data JPA | Database access                  |
| Hibernate       | ORM                              |
| Spring Security | Authentication and authorization |
| JWT             | Stateless authentication         |
| MySQL 8.4       | Database                         |
| Flyway          | Database migration               |
| Maven           | Build tool                       |
| JUnit 5         | Testing                          |
| Mockito         | Unit testing                     |
| Docker          | Containerization                 |
| Docker Compose  | Running backend and MySQL        |

---

# 4. Project Structure

```text
acme-salary-management/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/acme/salarymanagement/
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── db/
│   │   │
│   │   └── test/
│   │       └── java/
│   │
│   ├── Dockerfile
│   └── pom.xml
│
├── docker/
│   ├── docker-compose.yml
│   └── init/
│       └── schema.sql
│
├── .env
└── README.md
```

---

# 5. Authentication

The API uses **JWT-based authentication**.

## Login Endpoint

```http
POST /api/auth/login
```

### Request

```json
{
  "username": "hrmanager",
  "password": "Admin@123"
}
```

### Example Response

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "username": "hrmanager",
    "role": "HR_MANAGER"
  }
}
```

The returned `accessToken` must be used when calling protected APIs.

---

## Using the JWT

Add the following HTTP header to protected requests:

```http
Authorization: Bearer <accessToken>
```

Example:

```bash
curl -X GET \
  http://localhost:8080/api/employees \
  -H "Authorization: Bearer <accessToken>"
```

For the hosted server:

```bash
curl -X GET \
  https://acme-salary-management-1-3fwm.onrender.com/api/employees \
  -H "Authorization: Bearer <accessToken>"
```

---

# 6. API Security

## Public Endpoint

The following endpoint does not require authentication:

```text
POST /api/auth/login
```

## Protected Endpoints

All of the following require a valid JWT:

```text
/api/employees/**
/api/countries
/api/currencies
/api/analytics/**
```

Salary modification requires the:

```text
HR_MANAGER
```

role.

---

# 7. API Endpoints

## Authentication

| Method | Endpoint          | Authentication |
| ------ | ----------------- | -------------- |
| POST   | `/api/auth/login` | Public         |

---

# 8. Country APIs

## Get Countries

```http
GET /api/countries
```

Returns all available countries.

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

The country ID is used when referencing a country from an employee record.

---

# 9. Currency APIs

## Get Currencies

```http
GET /api/currencies
```

Returns available currencies.

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

---

# 10. Employee APIs

## Get Employees

```http
GET /api/employees
```

Returns a paginated list of employees.

### Query Parameters

| Parameter    | Required | Description                               |
| ------------ | -------- | ----------------------------------------- |
| `page`       | No       | Zero-based page number                    |
| `size`       | No       | Number of records                         |
| `search`     | No       | Search by first name, last name, or email |
| `countryId`  | No       | Filter by country                         |
| `department` | No       | Filter by department                      |
| `sort`       | No       | Sort field and direction                  |

### Example

```http
GET /api/employees?page=0&size=20
```

### Search

```http
GET /api/employees?page=0&size=20&search=john
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

### Example Response

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

# 11. Employee Details

## Get Employee Details

```http
GET /api/employees/{employeeId}
```

### Example

```http
GET /api/employees/123
```

### Example Response

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

---

# 12. Salary History

## Get Salary History

```http
GET /api/employees/{employeeId}/salary-history
```

### Example

```http
GET /api/employees/123/salary-history
```

### Example Response

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
    "changedBy": "hrmanager",
    "createdAt": "2026-09-19T09:20:00Z"
  }
]
```

Salary history is normally returned with the newest records first.

---

# 13. Update Employee Salary

## Update Salary

```http
PUT /api/employees/{employeeId}/salary
```

Requires:

```text
ROLE_HR_MANAGER
```

### Example

```http
PUT /api/employees/123/salary
```

### Request

```json
{
  "newSalary": 125000.00,
  "currencyId": 2,
  "effectiveDate": "2026-10-01"
}
```

### Processing

The salary update is performed inside a transaction.

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

If an operation fails:

```text
ROLLBACK
```

### Example Response

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
  "updatedBy": "hrmanager",
  "updatedAt": "2026-09-19T10:00:00Z"
}
```

### Validation

The API rejects:

```text
newSalary < 0
```

Required fields:

```text
newSalary
currencyId
effectiveDate
```

Invalid employee:

```text
404 Not Found
```

Invalid currency:

```text
404 Not Found
```

---

# 14. Analytics APIs

## Analytics Overview

```http
GET /api/analytics/overview
```

### Example Response

```json
{
  "totalEmployees": 10000,
  "totalCountries": 8,
  "totalDepartments": 7
}
```

Salary totals are not blindly aggregated across different currencies.

---

## Salary by Country

```http
GET /api/analytics/by-country
```

### Example Response

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
  }
]
```

Salary statistics are calculated within the appropriate currency context.

---

## Salary by Department

```http
GET /api/analytics/by-department
```

Salary statistics are grouped by department and currency where necessary.

### Example

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

The API does not calculate a single average across incompatible currencies.

---

## Salary Distribution

```http
GET /api/analytics/salary-distribution
```

### Query Parameters

| Parameter    | Required | Description                           |
| ------------ | -------- | ------------------------------------- |
| `currencyId` | Yes      | Currency used for salary distribution |
| `countryId`  | No       | Optional country filter               |
| `department` | No       | Optional department filter            |

### Example

```http
GET /api/analytics/salary-distribution?currencyId=2
```

### Example Response

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

---

# 15. Error Handling

The backend uses a consistent error response format.

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

# 16. HTTP Status Codes

| Status | Meaning                                  |
| ------ | ---------------------------------------- |
| `200`  | Successful request                       |
| `201`  | Resource created                         |
| `204`  | Successful request with no response body |
| `400`  | Invalid request                          |
| `401`  | Authentication required or invalid       |
| `403`  | Insufficient permissions                 |
| `404`  | Resource not found                       |
| `409`  | Conflict                                 |
| `500`  | Unexpected server error                  |

---

# 17. Pagination

Employee APIs use server-side pagination.

Default:

```text
page = 0
size = 20
```

Maximum page size:

```text
100
```

Example:

```http
GET /api/employees?page=0&size=20
```

This prevents clients from requesting all employee records at once.

---

# 18. Search

Employee search is performed on the backend.

Supported fields:

```text
firstName
lastName
email
```

Example:

```http
GET /api/employees?search=john
```

The frontend does not download all employees and perform the search locally.

---

# 19. Filtering

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

---

# 20. Sorting

Sorting fields are explicitly controlled by the backend.

Supported fields include:

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

Arbitrary SQL fragments are not accepted from the client.

---

# 21. Database

The application uses MySQL.

Default Docker database configuration:

```text
Database: acme_salary_management
Host: mysql
Port: 3306
```

When connecting from the host machine, the port is configured through:

```env
MYSQL_PORT=3307
```

Inside the Docker network, Spring Boot connects using:

```text
jdbc:mysql://mysql:3306/acme_salary_management
```

---

# 22. Environment Variables

Create a `.env` file in the **`acme-salary-management` project root**.

Example:

```env
MYSQL_DATABASE=acme_salary_management
MYSQL_USERNAME=root
MYSQL_PASSWORD=root
MYSQL_ROOT_PASSWORD=root
MYSQL_PORT=3307
```

Do not commit sensitive production credentials to source control.

---

# 23. Running with Docker

The project includes Docker configuration for running MySQL and Spring Boot together.

Project structure:

```text
acme-salary-management/
│
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│
├── docker/
│   ├── docker-compose.yml
│   └── init/
│       └── schema.sql
│
└── .env
```

### Run from the project root

The Docker Compose command must be executed from the **`acme-salary-management` folder**, where the `.env` file is located.

```bash
cd acme-salary-management
```

Then run:

```bash
docker compose --env-file .env -f docker/docker-compose.yml up --build
```

### Stop the application

Run from the `acme-salary-management` folder:

```bash
docker compose --env-file .env -f docker/docker-compose.yml down
```

### Stop the application and remove Docker volumes

```bash
docker compose --env-file .env -f docker/docker-compose.yml down -v
```

> `down -v` removes the MySQL Docker volume and therefore deletes the database data stored in that volume.

---

# 24. Running Locally Without Docker

Make sure the following are installed:

```text
Java 21
Maven
MySQL 8+
```

Configure the database in:

```text
backend/src/main/resources/application.properties
```

Then start the application from the backend directory:

```bash
cd backend
mvn spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

---

# 25. Build the Application

Run the following commands from the **`backend` directory**.

### Build without running tests

```bash
mvn clean package -DskipTests
```

### Build and run tests

```bash
mvn clean test
```

### Create the production JAR

```bash
mvn clean package
```

The generated JAR will be available under:

```text
target/
```

---

# 26. Testing

The project uses:

* JUnit 5
* Mockito
* Spring Boot Test
* Repository tests
* Service unit tests
* Controller tests

Run all tests from the `backend` directory:

```bash
mvn test
```

---

# 27. Complete API Reference

## Authentication

```text
POST   /api/auth/login
```

## Employees

```text
GET    /api/employees
GET    /api/employees/{employeeId}
GET    /api/employees/{employeeId}/salary-history
PUT    /api/employees/{employeeId}/salary
```

## Reference Data

```text
GET    /api/countries
GET    /api/currencies
```

## Analytics

```text
GET    /api/analytics/overview
GET    /api/analytics/by-country
GET    /api/analytics/by-department
GET    /api/analytics/salary-distribution
```

---

# 28. API-to-UI Mapping

| UI Screen            | API                                      |
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

# 29. Frontend Integration

The Angular application authenticates through:

```text
POST /api/auth/login
```

After successful authentication, Angular stores the JWT and sends it with protected requests:

```http
Authorization: Bearer <JWT>
```

Reference data can be loaded when the employee or salary form is opened:

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

The frontend submits IDs instead of display names.

Example:

```json
{
  "newSalary": 125000.00,
  "currencyId": 2,
  "effectiveDate": "2026-10-01"
}
```

---

# 30. API Design Principles

The backend follows these principles:

* RESTful resource naming
* DTO-based API responses
* JWT-based authentication
* Role-based authorization
* Server-side pagination
* Server-side searching
* Server-side filtering
* Controlled sorting
* Request validation
* Centralized exception handling
* Consistent error responses
* Transactional salary updates
* Foreign-key relationships for country and currency
* Currency-aware salary analytics
* Automated testing
* Docker-based deployment

---

# 31. Quick Start

## Option 1 — Use the Hosted Server

Open the hosted Swagger UI:

```text
https://acme-salary-management-1-3fwm.onrender.com/swagger-ui/index.html
```

Login using:

```http
POST /api/auth/login
```

Request:

```json
{
  "username": "hrmanager",
  "password": "Admin@123"
}
```

Copy the returned JWT and use it to authorize protected API requests.

---

## Option 2 — Run Locally with Docker

### 1. Navigate to the project root

```bash
cd acme-salary-management
```

### 2. Configure `.env`

```env
MYSQL_DATABASE=acme_salary_management
MYSQL_USERNAME=root
MYSQL_PASSWORD=root
MYSQL_PORT=3307
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/acme_salary_management
```

### 3. Start MySQL and Spring Boot

Run this command from the **`acme-salary-management` folder**:

```bash
docker compose --env-file .env -f docker/docker-compose.yml up --build
```

### 4. Open the local API

```text
http://localhost:8080
```

### 5. Open local Swagger UI

```text
http://localhost:8080/swagger-ui/index.html
```

### 6. Login

```http
POST http://localhost:8080/api/auth/login
```

Request:

```json
{
  "username": "hrmanager",
  "password": "Admin@123"
}
```

### 7. Copy the JWT

Use the returned:

```json
{
  "accessToken": "..."
}
```

### 8. Call a protected API

```bash
curl -X GET \
  http://localhost:8080/api/employees?page=0&size=20 \
  -H "Authorization: Bearer <accessToken>"
```

---

# 32. Application URLs

## Hosted Server

```text
Backend API:
https://acme-salary-management-1-3fwm.onrender.com

Swagger UI:
https://acme-salary-management-1-3fwm.onrender.com/swagger-ui/index.html

Login:
https://acme-salary-management-1-3fwm.onrender.com/api/auth/login
```

## Local Docker

```text
Backend API:
http://localhost:8080

Swagger UI:
http://localhost:8080/swagger-ui/index.html

Login:
http://localhost:8080/api/auth/login
```

## MySQL from the host machine

```text
Host: localhost
Port: 3307
Database: acme_salary_management
```

## MySQL from the Spring Boot Docker container

```text
Host: mysql
Port: 3306
Database: acme_salary_management
```

---

# License

This project is developed as part of the ACME Salary Management application assessment.
