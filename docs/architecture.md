# ACME Salary Management

 ## Product & Feature Design

 The requirements are converted into a concrete application by defining the main screens and user journeys.

 ### 2.1 Main Screens

 The first version of the application will contain the following screens:

```
ACME Salary Management
│
├── Login
│
└── Application
    │
    ├── Dashboard
    │
    ├── Employees
    │   ├── Employee List
    │   └── Employee Details
    │
    ├── Salary
    │   ├── Update Salary
    │   └── Salary History
    │
    └── Analytics
        ├── By Country
        ├── By Department
        └── Salary Distribution
```

 ### 2.2 Main User Journey

 The primary user journey is for an **HR Manager** managing an employee's salary.

```
HR Manager
    ↓
Login
    ↓
Dashboard
    ↓
Employees
    ↓
Search Employee
    ↓
Employee Details
    ↓
View Current Salary
    ↓
Update Salary
    ↓
Salary History Created
    ↓
Audit Log Created
```

 #### Primary Journey Description

 1. The HR Manager logs into the application.
2. The HR Manager is redirected to the Dashboard.
3. The HR Manager navigates to **Employees**.
4. The HR Manager searches for an employee.
5. The HR Manager opens the employee's details.
6. The current salary is displayed.
7. The HR Manager updates the employee's salary.
8. A new entry is created in the **Salary History**.
9. An **Audit Log** entry is created to record the salary change.

 ### Secondary User Journey — Analytics

 The second major journey focuses on salary analytics.

```
HR Manager
    ↓
Dashboard / Analytics
    ↓
Salary by Country
    ↓
Salary by Department
    ↓
Salary Distribution
```

 The analytics section allows the HR Manager to analyze salary information across:

 - Countries
- Departments
- Salary ranges/distributions

---

 # Step 3 — Architecture Design

 After defining the product features and user journeys, the next step is to determine how the application will be built.

 ## 3.1 High-Level Architecture

 The first version will use a three-tier architecture consisting of:

 - **Angular** — Frontend/UI
- **Spring Boot** — Backend REST API
- **MySQL** — Relational database

```
                 ┌─────────────────┐
                 │   Angular UI    │
                 └────────┬────────┘
                          │
                         REST
                          │
                          ▼
                 ┌─────────────────┐
                 │   Spring Boot   │
                 │                 │
                 │ Auth            │
                 │ Employee        │
                 │ Salary          │
                 │ Analytics       │
                 │ Audit           │
                 └────────┬────────┘
                          │
                         JPA
                          │
                          ▼
                 ┌─────────────────┐
                 │      MySQL      │
                 └─────────────────┘
```

 ## 3.2 Architecture Components

 ### Angular UI

 The Angular application will provide the user interface for the HR Manager.

 Primary responsibilities include:

 - Login screen
- Dashboard
- Employee list and search
- Employee details
- Salary updates
- Salary history
- Analytics dashboards
- Communication with the backend through REST APIs

 ### Spring Boot

 Spring Boot will provide the backend application and REST APIs.

 The backend will contain the following major modules:

 | Module | Responsibility |
| --- | --- |
| **Auth** | User authentication and authorization |
| **Employee** | Employee management and employee details |
| **Salary** | Salary updates and salary history |
| **Analytics** | Salary analysis by country, department, and distribution |
| **Audit** | Tracking important system actions such as salary changes |

### MySQL

 MySQL will be used as the primary relational database.

 It will store data such as:

 - Users
- Employees
- Departments
- Countries
- Salaries
- Salary history
- Audit logs

 ### JPA

 Spring Data JPA/Hibernate will be used by the Spring Boot backend to communicate with MySQL.

 The basic data flow will be:

```
Angular
   │
   │ HTTP / REST
   ▼
Spring Boot
   │
   │ JPA / Hibernate
   ▼
MySQL
```

 ## 3.3 Overall Application Flow

```
┌──────────────┐
│ HR Manager   │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  Angular UI  │
└──────┬───────┘
       │ REST API
       ▼
┌──────────────────────────────┐
│         Spring Boot          │
│                              │
│  ┌───────┐  ┌────────────┐  │
│  │ Auth  │  │ Employees  │  │
│  └───────┘  └────────────┘  │
│                              │
│  ┌────────┐ ┌─────────────┐ │
│  │ Salary │ │ Analytics   │ │
│  └────────┘ └─────────────┘ │
│                              │
│  ┌────────┐                  │
│  │ Audit  │                  │
│  └────────┘                  │
└──────────────┬───────────────┘
               │ JPA
               ▼
        ┌─────────────┐
        │    MySQL    │
        └─────────────┘
```

 ## 3.4 Key Design Principle

 The application separates responsibilities across three layers:

```
Presentation Layer
        ↓
Angular UI

Business/API Layer
        ↓
Spring Boot

Data Layer
        ↓
MySQL + JPA
```
