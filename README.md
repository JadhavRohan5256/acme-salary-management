# ACME Salary Management

Full-stack employee salary management application built with **Angular, Spring Boot, MySQL, JWT, NgRx, and Docker**.

The application provides employee management, salary updates, salary history, authentication, reference data, and workforce analytics.

---

## Tech Stack

### Frontend

* Angular 17
* TypeScript
* Angular Material
* NgRx Store / Effects
* RxJS
* SCSS
* Jasmine / Karma

### Backend

* Java 21
* Spring Boot
* Spring Data JPA / Hibernate
* Spring Security
* JWT
* MySQL 8.4
* Flyway
* Maven
* JUnit 5 / Mockito

### Infrastructure

* Docker
* Docker Compose

---

## Project Structure

```text
acme-salary-management/
│
├── frontend/
│   ├── src/
│   ├── Dockerfile
│   ├── package.json
│   └── angular.json
│
├── backend/
│   ├── src/
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

# Hosted Application

### Backend

[Hosted Swagger UI](https://acme-salary-management-1-3fwm.onrender.com/swagger-ui/index.html?utm_source=chatgpt.com)

Backend:

```text
https://acme-salary-management-1-3fwm.onrender.com
```

Login:

```text
POST /api/auth/login
```

The Render instance may take a little longer to respond after being idle.

---

# Frontend

The Angular frontend provides:

* Login
* Dashboard
* Employee list
* Search and filtering
* Employee details
* Salary history
* Salary updates
* Country and currency selection
* Country analytics
* Department analytics
* Salary distribution analytics

Frontend routing:

```text
/login
/dashboard
/employees
/employees/:id
/employees/:id/update-salary
/analytics
```

---

# Authentication

Authentication uses JWT.

### Login

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

The API returns an access token:

```json
{
  "accessToken": "...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "username": "hrmanager",
    "role": "HR_MANAGER"
  }
}
```

Protected API requests use:

```http
Authorization: Bearer <accessToken>
```

The Angular application stores the token and automatically adds it to protected API requests through an HTTP interceptor.

---

# API

Base path:

```text
/api
```

### Authentication

```text
POST /api/auth/login
```

### Employees

```text
GET  /api/employees
GET  /api/employees/{id}
GET  /api/employees/{id}/salary-history
PUT  /api/employees/{id}/salary
```

Employee API supports:

* Pagination
* Search
* Country filtering
* Department filtering
* Sorting

Example:

```http
GET /api/employees?page=0&size=20&search=john&countryId=1
```

### Reference Data

```text
GET /api/countries
GET /api/currencies
```

### Analytics

```text
GET /api/analytics/overview
GET /api/analytics/by-country
GET /api/analytics/by-department
GET /api/analytics/salary-distribution
```

Salary analytics are **currency-aware** and do not combine incompatible currencies into a single salary average.

---

# Frontend Architecture

The Angular application uses feature-based architecture with NgRx.

```text
frontend/src/app/

├── core/
│   ├── guards/
│   ├── interceptors/
│   ├── models/
│   └── services/
│
├── shared/
│
├── store/
│   ├── auth/
│   ├── employees/
│   ├── reference-data/
│   └── analytics/
│
└── features/
    ├── auth/
    ├── dashboard/
    ├── employees/
    └── analytics/
```

NgRx is used for:

* Authentication state
* Employee state
* Reference data
* Analytics
* API loading/error states

---

# Salary Update

```http
PUT /api/employees/{employeeId}/salary
```

Request:

```json
{
  "newSalary": 125000,
  "currencyId": 2,
  "effectiveDate": "2026-10-01"
}
```

Salary updates are transactional:

```text
Find employee
     ↓
Validate employee/currency
     ↓
Update current salary
     ↓
Create salary history
     ↓
Create audit log
     ↓
Commit
```

The operation is rolled back if an error occurs.

Salary modification requires:

```text
HR_MANAGER
```

---

# Database

MySQL is used for persistent storage.

### Docker

```text
Host: mysql
Port: 3306
Database: acme_salary_management
```

### Host machine

```text
Host: localhost
Port: 3307
Database: acme_salary_management
```

Flyway manages database migrations.

---

# Environment Variables

Create `.env` in the project root:

```env
MYSQL_DATABASE=acme_salary_management
MYSQL_USERNAME=root
MYSQL_PASSWORD=root
MYSQL_ROOT_PASSWORD=root
MYSQL_PORT=3307
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/acme_salary_management
```

Do not commit production credentials.

---

# Run with Docker Compose

From the project root:

```bash
docker compose --env-file .env -f docker/docker-compose.yml up --build
```

This starts:

```text
Angular Frontend
       ↓
Spring Boot Backend
       ↓
     MySQL
```

### Local URLs

Frontend:

```text
http://localhost:4200
```

Backend:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

Stop containers:

```bash
docker compose --env-file .env -f docker/docker-compose.yml down
```

Remove containers and database volume:

```bash
docker compose --env-file .env -f docker/docker-compose.yml down -v
```

> `down -v` removes the MySQL volume and its stored database data.

---

# Run Backend Without Docker

Requirements:

```text
Java 21
Maven
MySQL 8+
```

From the backend directory:

```bash
cd backend
mvn spring-boot:run
```

Backend:

```text
http://localhost:8080
```

---

# Run Frontend Without Docker

Requirements:

```text
Node.js
npm
Angular CLI 17
```

From the frontend directory:

```bash
cd frontend
npm install
ng serve
```

Frontend:

```text
http://localhost:4200
```

Development API URL:

```text
http://localhost:8080/api
```

---

# Testing

### Backend

From `backend`:

```bash
mvn test
```

The backend includes:

* Controller tests
* Service unit tests
* Repository tests
* Security-related tests

### Frontend

From `frontend`:

```bash
npm test
```

The frontend includes unit tests for:

* Components
* Services
* NgRx reducers
* NgRx selectors
* NgRx effects

---

# Build

### Backend

```bash
cd backend
mvn clean package
```

### Frontend

```bash
cd frontend
npm run build
```

---

# Main Features

* JWT authentication
* Role-based authorization
* Employee management
* Server-side pagination
* Server-side search
* Country and department filtering
* Employee details
* Salary updates
* Salary history
* Audit logging
* Country and currency reference data
* Currency-aware analytics
* Salary distribution
* Angular Material UI
* NgRx state management
* Unit testing
* Dockerized frontend, backend, and MySQL

---

## API-to-UI Mapping

| UI                   | API                                      |
| -------------------- | ---------------------------------------- |
| Login                | `POST /api/auth/login`                   |
| Dashboard            | `GET /api/analytics/overview`            |
| Employee List        | `GET /api/employees`                     |
| Employee Details     | `GET /api/employees/{id}`                |
| Salary History       | `GET /api/employees/{id}/salary-history` |
| Update Salary        | `PUT /api/employees/{id}/salary`         |
| Countries            | `GET /api/countries`                     |
| Currencies           | `GET /api/currencies`                    |
| Country Analytics    | `GET /api/analytics/by-country`          |
| Department Analytics | `GET /api/analytics/by-department`       |
| Salary Distribution  | `GET /api/analytics/salary-distribution` |

---

## Quick Start

```bash
# 1. Clone the repository

# 2. Create .env in the project root

# 3. Start the complete application
docker compose --env-file .env -f docker/docker-compose.yml up --build
```

Open:

```text
Frontend:
http://localhost:4200

Backend:
http://localhost:8080

Swagger:
http://localhost:8080/swagger-ui/index.html
```

Login:

```text
Username: hrmanager
Password: Admin@123
```

---

## License

This project was developed as part of the ACME Salary Management application assessment.
