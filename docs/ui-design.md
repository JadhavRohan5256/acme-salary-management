# ACME Salary Management — UI Design

## 1. Overview

The ACME Salary Management frontend is a web-based application used by HR Managers to manage employees, update salaries, view salary history, and analyze salary information.

The frontend will be implemented using:

* Angular
* TypeScript
* Angular Material
* RxJS
* NgRx
* Reactive Forms
* Angular Router
* Angular HttpClient
* JWT-based authentication
* Charting library for analytics
* Responsive design
* WCAG accessibility practices

The frontend communicates with the Spring Boot backend through REST APIs defined in `api-design.md`.

---

# 2. UI Goals

The application should provide:

1. Secure authentication.
2. Simple employee management.
3. Fast employee search and filtering.
4. Server-side pagination for 10,000+ employees.
5. Clear salary information.
6. Salary update functionality for authorized HR Managers.
7. Salary history visibility.
8. Country and currency selection using backend reference data.
9. Salary analytics through charts.
10. Responsive desktop/tablet UI.
11. Accessibility following WCAG principles.
12. Clear loading, success, error, and empty states.
13. Centralized application state management using NgRx.
14. Automated frontend unit tests for important functionality.

---

# 3. User Role

## 3.1 HR Manager

The primary application user is an HR Manager.

The HR Manager can:

* Login.
* View dashboard.
* View employees.
* Search employees.
* Filter employees.
* Sort employees.
* View employee details.
* View salary history.
* Update employee salary.
* View analytics.
* View country and currency information.

Salary modification requires:

```text
ROLE_HR_MANAGER
```

The backend is responsible for enforcing authorization.

---

# 4. Application Layout

After authentication, the application uses a common layout.

```text
+----------------------------------------------------------+
| ACME Salary Management                     User | Logout |
+----------------------+-----------------------------------+
|                      |                                   |
| Dashboard            |                                   |
| Employees            |        Main Content Area          |
| Analytics            |                                   |
|                      |                                   |
+----------------------+-----------------------------------+
```

## 4.1 Header

The header contains:

* Application name/logo.
* Logged-in username.
* User role.
* Logout button.

Example:

```text
ACME Salary Management                    admin | HR Manager
                                             [Logout]
```

## 4.2 Sidebar

Navigation items:

```text
Dashboard
Employees
Analytics
```

The sidebar should:

* Highlight the active route.
* Support keyboard navigation.
* Provide accessible labels.
* Be responsive.

---

# 5. Route Structure

The Angular application will use the following routes:

```text
/login

/dashboard

/employees
/employees/:id

/analytics
```

## 5.1 Route Protection

Authenticated routes:

```text
/dashboard
/employees
/employees/:id
/analytics
```

should be protected using an Angular authentication guard.

Unauthenticated users attempting to access protected routes should be redirected to:

```text
/login
```

---

# 6. Login Page

Route:

```text
/login
```

## 6.1 UI

```text
+----------------------------------+
|     ACME Salary Management       |
|                                  |
| Username                         |
| [____________________________]   |
|                                  |
| Password                         |
| [____________________________]   |
|                                  |
|           [ Login ]              |
|                                  |
|       Invalid credentials        |
+----------------------------------+
```

## 6.2 Form Fields

### Username

* Required.
* Maximum length should match backend validation.
* Trim unnecessary whitespace.

### Password

* Required.
* Password input type.
* Must not be logged.
* Must not be exposed in error messages.

## 6.3 Login Flow

```text
User enters credentials
        ↓
POST /api/auth/login
        ↓
Successful?
   ┌────┴────┐
  Yes        No
   ↓          ↓
Store auth   Show error
state
   ↓
Navigate to /dashboard
```

## 6.4 Error Handling

Examples:

```text
Invalid username or password.
Unable to connect to server.
Something went wrong. Please try again.
```

The UI should not expose sensitive backend details.

---

# 7. Dashboard

Route:

```text
/dashboard
```

The dashboard provides a high-level overview of the organization.

## 7.1 Summary Cards

Display:

* Total employees.
* Number of countries.
* Number of departments.
* Number of currencies.

Example:

```text
+----------------+ +----------------+
| Total Employees| | Countries      |
|    10,000      | |       8        |
+----------------+ +----------------+

+----------------+ +----------------+
| Departments    | | Currencies     |
|       7        | |       8        |
+----------------+ +----------------+
```

## 7.2 Charts

The dashboard should display:

### Salary by Country

Salary information grouped by country while preserving currency context.

### Salary by Department

Salary information grouped by department while preserving currency context.

### Salary Distribution

Salary distribution by defined salary ranges and currency context.

---

# 8. Employee List

Route:

```text
/employees
```

This is the primary employee management screen.

Because the system contains approximately 10,000 employees, the frontend must use server-side pagination.

The frontend must not load all employees into the browser.

---

# 9. Employee List UI

Example:

```text
Employees

Search:
[ Search by name or email                  ]

Country:
[ All Countries ▼ ]

Department:
[ All Departments ▼ ]

[ Clear Filters ]

--------------------------------------------------------------
ID | Name | Email | Country | Department | Salary | Action
--------------------------------------------------------------
1  | John | ...   | India   | Engineering| ...    | View
2  | Sarah| ...   | USA     | Finance    | ...    | View
3  | Mike | ...   | UK      | Product    | ...    | View
--------------------------------------------------------------

Showing 1-20 of 10,000

[Previous] 1 2 3 4 5 [Next]
```

---

# 10. Employee Search

Search should support:

* First name.
* Last name.
* Email.

API:

```text
GET /api/employees?search=john
```

Search should be handled by the backend.

The frontend should not download all employees and filter them locally.

## 10.1 Search Behavior

To avoid unnecessary API requests while typing, RxJS debounce behavior should be used.

```text
User types:

j
jo
joh
john

        ↓

debounceTime(300-500ms)

        ↓

Dispatch NgRx search/load action

        ↓

GET /api/employees?search=john
```

---

# 11. Employee Filtering

Supported filters:

* Country.
* Department.

## 11.1 Country

Country values are loaded from:

```text
GET /api/countries
```

Example:

```text
Country:
[ All Countries
  India
  USA
  UK
  Germany
  Canada
  Australia
  Singapore
  UAE
]
```

## 11.2 Department

Example departments:

```text
Engineering
Finance
Human Resources
Marketing
Sales
Operations
Product
```

---

# 12. Employee Sorting

The employee table should support sorting where appropriate.

Possible sortable fields:

```text
First Name
Last Name
Country
Department
Current Salary
```

Sorting should be performed by the backend.

Example:

```text
GET /api/employees?sort=firstName,asc
```

The frontend must only send supported sort fields.

---

# 13. Pagination

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

```text
GET /api/employees?page=0&size=20
```

The UI should display:

```text
Showing 1-20 of 10,000
```

and provide:

```text
Previous
Next
Page numbers
Page size
```

The frontend should preserve the current search/filter/sort state when changing pages.

---

# 14. Employee Table

Columns:

| Column         | Description          |
| -------------- | -------------------- |
| ID             | Employee database ID |
| Name           | First + last name    |
| Email          | Employee email       |
| Country        | Country name         |
| Department     | Department           |
| Designation    | Employee designation |
| Current Salary | Salary with currency |
| Action         | View employee        |

The employee `id` is the unique identifier.

There is no separate `employee_code`.

---

# 15. Employee Details

Route:

```text
/employees/:id
```

API:

```text
GET /api/employees/{employeeId}
```

## 15.1 Employee Information

Display:

```text
Employee Details

ID
125

First Name
John

Last Name
Doe

Email
john@example.com

Country
India

Department
Engineering

Designation
Software Engineer
```

## 15.2 Current Salary

Display separately:

```text
Current Salary

₹125,000.00
```

The currency code/symbol should be displayed with the amount.

Example:

```text
INR 125,000.00
```

---

# 16. Salary Update

Salary modification is available to authorized HR Managers.

API:

```text
PUT /api/employees/{employeeId}/salary
```

## 16.1 Salary Update UI

```text
Update Salary

Employee:
John Doe

Current Salary:
INR 100,000.00

New Salary:
[________________]

Effective Date:
[ 2026-10-01 ]

[Cancel]       [Update Salary]
```

## 16.2 Validation

New salary:

* Required.
* Must be numeric.
* Must be greater than or equal to zero.
* Must follow the backend's allowed precision.

Effective date:

* Required.
* Must be a valid date.
* API format:

```text
YYYY-MM-DD
```

## 16.3 Update Flow

```text
User opens employee
        ↓
Clicks Update Salary
        ↓
Enters new salary
        ↓
Selects effective date
        ↓
Frontend validation
        ↓
Confirmation
        ↓
PUT /api/employees/{id}/salary
        ↓
Backend transaction
        ↓
Success
        ↓
Dispatch refresh actions
        ↓
Update employee + salary history state
```

---

# 17. Salary Update Confirmation

Before submitting the salary change, the UI should display a confirmation dialog.

```text
Confirm Salary Update

Employee:
John Doe

Previous Salary:
INR 100,000.00

New Salary:
INR 125,000.00

Effective Date:
01 Oct 2026

Are you sure you want to update the salary?

[Cancel] [Confirm]
```

This reduces accidental salary modifications.

---

# 18. Salary History

Salary history is displayed on the employee details page.

API:

```text
GET /api/employees/{employeeId}/salary-history
```

## 18.1 Salary History Table

```text
Salary History

--------------------------------------------------------------
Previous Salary | New Salary | Currency | Effective Date | By
--------------------------------------------------------------
100,000         | 125,000    | INR      | 2026-10-01      | admin
90,000          | 100,000    | INR      | 2026-01-01      | admin
--------------------------------------------------------------
```

Records should be displayed newest first.

---

# 19. Countries

API:

```text
GET /api/countries
```

Country data is used for:

* Employee filtering.
* Employee forms if applicable.
* Displaying employee country.
* Analytics.

Example:

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
  }
]
```

The frontend should use the database ID when communicating with the backend.

---

# 20. Currencies

API:

```text
GET /api/currencies
```

Currency data is used for:

* Displaying salary currency.
* Employee-related forms where applicable.
* Salary history display.
* Analytics context.

Example:

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
    "name": "United States Dollar",
    "symbol": "$"
  }
]
```

---

# 21. Currency Handling

Employees can have salaries in different currencies.

Example:

```text
India       → INR 125,000
USA         → USD 5,000
UK          → GBP 4,000
Germany     → EUR 4,500
```

The frontend must not imply that raw values from different currencies are directly comparable.

Without an FX conversion mechanism, the UI should preserve currency context for salary analytics.

---

# 22. Analytics Page

Route:

```text
/analytics
```

APIs:

```text
GET /api/analytics/overview

GET /api/analytics/by-country

GET /api/analytics/by-department

GET /api/analytics/salary-distribution
```

---

# 23. Analytics Overview

Display high-level metrics:

```text
Total Employees
Total Countries
Total Departments
Total Currencies
```

Additional salary metrics should only be displayed when their currency context is clearly defined.

---

# 24. Salary by Country

API:

```text
GET /api/analytics/by-country
```

Possible visualization:

```text
Salary by Country

India        █████████████
USA          █████████
UK           ███████
Germany      █████
Canada       ████
```

The chart should clearly identify:

* Country.
* Currency.
* Metric being displayed.

---

# 25. Salary by Department

API:

```text
GET /api/analytics/by-department
```

Example:

```text
Salary by Department

Engineering        █████████████
Finance            ███████
Product            █████████
Sales              █████
Operations         ████
```

If multiple currencies are present, the UI must preserve currency context.

---

# 26. Salary Distribution

API:

```text
GET /api/analytics/salary-distribution
```

The UI may display salary ranges such as:

```text
Salary Distribution

0 - 50K          ███
50K - 100K       ███████
100K - 150K      █████████
150K - 200K      █████
200K+            ██
```

The currency context must be visible.

---

# 27. NgRx State Management

NgRx will be used as the centralized state-management solution for shared application and server state.

NgRx should be organized by feature/domain.

```text
Store
├── auth
├── employees
├── countries
├── currencies
└── analytics
```

Transient component-level UI state should remain local.

Examples:

```text
Global/NgRx State:
- Authentication
- Employee data
- Employee filters
- Pagination state
- Countries
- Currencies
- Analytics data

Local Component State:
- Dialog open/close
- Temporary form values
- UI-only toggles
- Temporary validation state
```

---

# 28. Employee NgRx State

The employee feature state should contain:

```typescript
interface EmployeeState {
  employees: Employee[];
  selectedEmployee: Employee | null;
  salaryHistory: SalaryHistory[];
  totalElements: number;
  page: number;
  size: number;
  search: string;
  countryId: number | null;
  department: string | null;
  sort: string;
  loading: boolean;
  error: string | null;
}
```

The exact interface can be adjusted during implementation while preserving these responsibilities.

---

# 29. NgRx Actions

Examples:

## Employee Actions

```text
loadEmployees
loadEmployeesSuccess
loadEmployeesFailure

loadEmployee
loadEmployeeSuccess
loadEmployeeFailure

loadSalaryHistory
loadSalaryHistorySuccess
loadSalaryHistoryFailure

updateSalary
updateSalarySuccess
updateSalaryFailure

setSearch
setCountryFilter
setDepartmentFilter
setPage
setSort
clearFilters
```

## Authentication Actions

```text
login
loginSuccess
loginFailure
logout
```

## Reference Data Actions

```text
loadCountries
loadCountriesSuccess
loadCountriesFailure

loadCurrencies
loadCurrenciesSuccess
loadCurrenciesFailure
```

## Analytics Actions

```text
loadOverview
loadOverviewSuccess
loadOverviewFailure

loadByCountry
loadByCountrySuccess
loadByCountryFailure

loadByDepartment
loadByDepartmentSuccess
loadByDepartmentFailure

loadSalaryDistribution
loadSalaryDistributionSuccess
loadSalaryDistributionFailure
```

---

# 30. NgRx Effects

Effects handle asynchronous operations and API communication.

Example:

```text
Component
    ↓
dispatch(loadEmployees())
    ↓
Employee Effect
    ↓
EmployeeService
    ↓
HTTP API
    ↓
loadEmployeesSuccess()
    ↓
Employee Reducer
    ↓
Store
    ↓
Selector
    ↓
Component
```

Effects should handle:

* HTTP requests.
* Success actions.
* Failure actions.
* Related refresh operations.

Components should not directly contain API business logic.

---

# 31. NgRx Reducers

Reducers update state based on dispatched actions.

Example:

```text
loadEmployees
    ↓
loading = true

loadEmployeesSuccess
    ↓
employees = response.content
totalElements = response.totalElements
loading = false
error = null

loadEmployeesFailure
    ↓
loading = false
error = message
```

Reducers must remain pure functions.

---

# 32. NgRx Selectors

Selectors provide components with specific pieces of state.

Examples:

```text
selectEmployees
selectSelectedEmployee
selectSalaryHistory
selectEmployeeLoading
selectEmployeeError
selectTotalElements
selectSearch
selectCountryId
selectDepartment
selectSort
```

Analytics selectors:

```text
selectOverview
selectSalaryByCountry
selectSalaryByDepartment
selectSalaryDistribution
selectAnalyticsLoading
selectAnalyticsError
```

Authentication selectors:

```text
selectCurrentUser
selectUserRole
selectIsAuthenticated
selectAccessToken
```

Components should consume state through selectors instead of directly accessing store internals.

---

# 33. Employee Search and NgRx

Search flow:

```text
User enters search
        ↓
Component
        ↓
setSearch action
        ↓
debounce
        ↓
loadEmployees action
        ↓
Employee Effect
        ↓
API
        ↓
Success/Failure
        ↓
Reducer
        ↓
Selector
        ↓
Employee Table
```

The search state should be preserved when navigating through pagination.

---

# 34. Employee Filter and NgRx

Country filter:

```text
User selects country
        ↓
setCountryFilter
        ↓
Reset page to 0
        ↓
loadEmployees
        ↓
API
```

Department filter follows the same pattern.

When filters change:

```text
page = 0
```

to prevent invalid pagination states.

---

# 35. NgRx Salary Update Flow

```text
Salary Form
    ↓
Validation
    ↓
Confirmation
    ↓
dispatch(updateSalary)
    ↓
Salary Effect
    ↓
PUT /api/employees/{id}/salary
    ↓
updateSalarySuccess
    ↓
Refresh employee
    ↓
Refresh salary history
    ↓
Updated Store
    ↓
Updated UI
```

A salary update should not directly mutate state from a component.

---

# 36. Authentication State

Authentication state should be managed through NgRx.

Example:

```typescript
interface AuthState {
  user: User | null;
  accessToken: string | null;
  isAuthenticated: boolean;
  loading: boolean;
  error: string | null;
}
```

The authentication flow:

```text
Login Component
      ↓
login action
      ↓
Auth Effect
      ↓
POST /api/auth/login
      ↓
loginSuccess
      ↓
Auth Reducer
      ↓
Auth Store
      ↓
Dashboard
```

---

# 37. HTTP Interceptor

An Angular HTTP interceptor should attach the JWT to protected API requests.

Example:

```http
Authorization: Bearer <JWT>
```

The interceptor should:

* Read the current authentication token.
* Add the authorization header.
* Avoid attaching tokens to public endpoints when unnecessary.
* Handle authentication-related HTTP responses consistently.

---

# 38. Loading States

Every API-driven feature should have a loading state.

Example:

```text
Loading employees...
```

For tables, skeleton loaders can be used.

The UI should avoid displaying stale information as if it were the latest data while a request is in progress.

---

# 39. Empty States

Employee search:

```text
No employees found.

Try changing your search or filters.
```

Salary history:

```text
No salary history available.
```

Analytics:

```text
No analytics data available.
```

---

# 40. Error Handling

The frontend should handle:

### 400 Bad Request

Display validation/request errors.

### 401 Unauthorized

Clear authentication state and redirect to login when appropriate.

### 403 Forbidden

Display:

```text
You do not have permission to update salaries.
```

### 404 Not Found

Display:

```text
Employee not found.
```

### 409 Conflict

Display the relevant backend conflict message.

### 500 Internal Server Error

Display:

```text
Something went wrong on the server.
Please try again later.
```

The UI should not expose stack traces or internal implementation details.

---

# 41. Global Notification System

Use a consistent notification mechanism.

Success:

```text
Salary updated successfully.
```

Error:

```text
Unable to update salary.
```

Warning:

```text
Please review the entered salary.
```

Angular Material Snackbar can be used for transient notifications.

---

# 42. API Service Structure

API communication should be separated into services.

Suggested services:

```text
AuthService
EmployeeService
SalaryService
CountryService
CurrencyService
AnalyticsService
```

Example:

```typescript
EmployeeService

getEmployees()
getEmployee(id)
getSalaryHistory(id)
```

```typescript
SalaryService

updateSalary(employeeId, request)
```

```typescript
CountryService

getCountries()
```

```typescript
CurrencyService

getCurrencies()
```

```typescript
AnalyticsService

getOverview()
getByCountry()
getByDepartment()
getSalaryDistribution()
```

Services should focus on API communication.

NgRx effects should coordinate asynchronous state changes.

---

# 43. Component Structure

Suggested Angular organization:

```text
app/
│
├── core/
│   ├── guards/
│   ├── interceptors/
│   ├── services/
│   └── models/
│
├── shared/
│   ├── components/
│   ├── pipes/
│   └── directives/
│
├── store/
│   ├── auth/
│   ├── employees/
│   ├── countries/
│   ├── currencies/
│   └── analytics/
│
├── features/
│   ├── auth/
│   │   └── login/
│   │
│   ├── dashboard/
│   │
│   ├── employees/
│   │   ├── employee-list/
│   │   ├── employee-details/
│   │   ├── salary-update/
│   │   └── salary-history/
│   │
│   └── analytics/
│
└── app.routes.ts
```

The exact Angular folder structure should remain consistent with `architecture.md`.

---

# 44. Reactive Forms

Reactive Forms should be used for:

* Login.
* Salary update.
* Any future employee forms.

Example:

```typescript
this.salaryForm = this.fb.group({
  newSalary: [null, [Validators.required, Validators.min(0)]],
  effectiveDate: ['', Validators.required]
});
```

Frontend validation improves user experience.

Backend validation remains authoritative.

---

# 45. Accessibility

Accessibility is a required UI consideration.

The application should follow WCAG principles.

Requirements include:

* Semantic HTML.
* Proper heading hierarchy.
* Labels for form controls.
* Keyboard navigation.
* Visible focus indicators.
* Accessible buttons.
* Accessible dialogs.
* Appropriate ARIA attributes where required.
* Sufficient color contrast.
* Error messages associated with controls.
* No information conveyed through color alone.
* Screen-reader-friendly navigation.
* Accessible tables.
* Accessible charts with supporting textual information.

---

# 46. Responsive Design

The application should support:

* Desktop.
* Laptop.
* Tablet.

Primary target:

```text
Desktop HR management application
```

The employee table should remain usable on smaller screens.

Possible approaches:

* Horizontal scrolling.
* Responsive column handling.
* Stacked employee information on smaller screens.

---

# 47. Performance Considerations

Because the application manages approximately 10,000 employees:

## Employee Data

Do not load all employees into the browser.

Use:

```text
Server-side pagination
Server-side search
Server-side filtering
Server-side sorting
```

## API Requests

Use:

* RxJS operators.
* Search debouncing.
* Request cancellation where appropriate.
* Avoid duplicate reference-data requests.

## Reference Data

Countries and currencies change infrequently.

They can be cached in NgRx state for the duration of the application session.

## Lazy Loading

Feature routes should be lazy-loaded where appropriate.

```text
/dashboard
/employees
/analytics
```

should not unnecessarily load all feature code during initial login.

## NgRx Performance

Selectors should be used to prevent unnecessary component updates.

State should contain only data required by the application.

Large derived datasets should not be unnecessarily duplicated in the store.

---

# 48. UI State Management Principles

NgRx should be used for:

```text
Authentication
Employee server data
Employee filters
Pagination state
Selected employee
Salary history
Countries
Currencies
Analytics
API loading/error states
```

Local component state should be used for:

```text
Dialog visibility
Temporary form values
Temporary UI interactions
Small component-specific state
```

The objective is to avoid both:

* Excessive global state.
* Scattered API state across components.

---

# 49. Navigation Flow

## Login Flow

```text
Login
  ↓
Dashboard
```

## Employee Flow

```text
Dashboard
   ↓
Employees
   ↓
Employee Details
   ↓
Salary History
   ↓
Update Salary
   ↓
Employee Details
```

## Analytics Flow

```text
Dashboard
   ↓
Analytics
   ↓
Salary by Country
Salary by Department
Salary Distribution
```

---

# 50. Employee Detail Back Navigation

From:

```text
/employees/:id
```

the user should be able to return to:

```text
/employees
```

The application should preserve employee-list state where practical:

```text
Search
Country filter
Department filter
Page
Sort
```

This prevents the user from losing their place after viewing an employee.

---

# 51. Confirmation and Business-Critical Actions

Salary updates are business-critical changes.

Therefore:

* Display the previous salary.
* Display the new salary.
* Display effective date.
* Require confirmation.
* Disable submit while the request is in progress.
* Prevent duplicate submissions.
* Display the result after completion.
* Refresh the relevant NgRx state after success.

---

# 52. Data Formatting

## Salary

Display:

```text
INR 125,000.00
USD 5,000.00
GBP 4,000.00
```

Use the appropriate currency formatting mechanism.

## Date

API format:

```text
YYYY-MM-DD
```

UI display can use:

```text
01 Oct 2026
```

while API requests continue using:

```text
2026-10-01
```

---

# 53. Security Considerations

The frontend must:

* Never hard-code passwords.
* Never log JWT tokens.
* Never log sensitive salary information unnecessarily.
* Never trust frontend role checks for authorization.
* Use HTTPS in production.
* Handle expired/invalid tokens.
* Avoid exposing backend stack traces.
* Avoid putting sensitive information into URLs where unnecessary.

The backend remains the authoritative security boundary.

---

# 54. Browser Refresh Behavior

After browser refresh:

```text
Application starts
        ↓
Authentication state checked
        ↓
Valid authentication?
   ┌────┴────┐
  Yes        No
   ↓          ↓
Load app    /login
```

The chosen token-storage mechanism must be documented and implemented consistently with the application's security requirements.

---

# 55. Frontend Testing Strategy

The frontend should have meaningful automated unit tests.

Testing should cover:

```text
Components
Services
NgRx Actions
NgRx Reducers
NgRx Selectors
NgRx Effects
Guards
HTTP Interceptors
Forms
Important user flows
```

Tests should focus on application behavior rather than implementation details.

---

# 56. Testing Tools

Recommended tools:

```text
Angular TestBed
Jasmine
Karma
```

or the testing framework supported by the selected Angular version.

HTTP requests should be mocked during unit tests.

Frontend tests should not depend on the actual production backend.

---

# 57. Authentication Test Cases

## Login Component

Test:

* Form renders correctly.
* Username is required.
* Password is required.
* Invalid form cannot be submitted.
* Login action is dispatched with valid credentials.
* Successful login navigates to dashboard.
* Failed login displays an error.

## Auth Store

Test:

* Initial authentication state.
* Login loading state.
* Login success state.
* Login failure state.
* Logout clears authentication state.

## Auth Guard

Test:

* Authenticated user can access protected routes.
* Unauthenticated user is redirected to `/login`.

---

# 58. HTTP Interceptor Test Cases

Test:

* JWT is added to protected API requests.
* Correct `Authorization` header is generated.
* Public requests are handled correctly.
* 401 responses trigger the expected authentication handling.

Example:

```text
Authorization: Bearer test-token
```

---

# 59. Employee Component Test Cases

Employee list tests:

* Component renders.
* Employees are displayed.
* Loading indicator is displayed while loading.
* Empty state is displayed when no employees exist.
* Error state is displayed when API fails.
* Search action is dispatched.
* Country filter action is dispatched.
* Department filter action is dispatched.
* Pagination action is dispatched.
* Sorting action is dispatched.
* View employee navigation works.

Employee details tests:

* Employee information renders.
* Current salary renders.
* Salary history renders.
* Employee-not-found state is displayed.
* Update Salary button is displayed for authorized users.

---

# 60. Salary Update Test Cases

Test:

* Form renders.
* New salary is required.
* Negative salary is rejected.
* Invalid salary format is rejected.
* Effective date is required.
* Invalid date is rejected.
* Confirmation dialog opens.
* Cancel prevents submission.
* Confirm dispatches update action.
* Submit button is disabled while update is in progress.
* Successful update displays success notification.
* Failed update displays error notification.
* Employee state is refreshed after successful update.
* Salary history state is refreshed after successful update.

---

# 61. NgRx Reducer Test Cases

Reducers should be tested independently.

### Employee Reducer

Test:

```text
Initial state
loadEmployees
loadEmployeesSuccess
loadEmployeesFailure

loadEmployee
loadEmployeeSuccess
loadEmployeeFailure

loadSalaryHistory
loadSalaryHistorySuccess
loadSalaryHistoryFailure

updateSalary
updateSalarySuccess
updateSalaryFailure

setSearch
setCountryFilter
setDepartmentFilter
setPage
setSort
clearFilters
```

Verify:

* Correct state changes.
* Loading state.
* Error state.
* Employee data.
* Pagination state.
* Filter state.

Reducers must remain pure.

---

# 62. NgRx Selector Test Cases

Test selectors for:

```text
selectEmployees
selectSelectedEmployee
selectSalaryHistory
selectTotalElements
selectSearch
selectCountryId
selectDepartment
selectSort
selectEmployeeLoading
selectEmployeeError
```

Authentication:

```text
selectCurrentUser
selectUserRole
selectIsAuthenticated
```

Analytics:

```text
selectOverview
selectSalaryByCountry
selectSalaryByDepartment
selectSalaryDistribution
selectAnalyticsLoading
selectAnalyticsError
```

Selectors should return the expected derived data from known state.

---

# 63. NgRx Effect Test Cases

Effects should be tested with mocked services.

### Employee Effects

Test:

```text
loadEmployees
    ↓
EmployeeService.getEmployees()
    ↓
loadEmployeesSuccess
```

and:

```text
EmployeeService error
    ↓
loadEmployeesFailure
```

Similarly test:

```text
loadEmployee
loadSalaryHistory
updateSalary
```

### Reference Data Effects

Test:

```text
loadCountries
loadCurrencies
```

### Analytics Effects

Test:

```text
loadOverview
loadByCountry
loadByDepartment
loadSalaryDistribution
```

Each effect should dispatch the correct success or failure action.

---

# 64. Service Test Cases

Services should be tested independently.

## EmployeeService

Test:

* Correct employee-list URL.
* Query parameters.
* Employee detail URL.
* Salary history URL.

## SalaryService

Test:

* Correct salary update URL.
* Correct HTTP method.
* Correct request body.

## CountryService

Test:

* Correct countries endpoint.

## CurrencyService

Test:

* Correct currencies endpoint.

## AnalyticsService

Test:

* Correct analytics endpoints.

---

# 65. Reactive Form Test Cases

Test:

* Initial form state.
* Required validators.
* Minimum salary validation.
* Date validation.
* Form validity.
* Form submission.
* Invalid form prevents API/action submission.

---

# 66. Dashboard Test Cases

Test:

* Dashboard loads.
* Overview API/effect is triggered.
* Summary cards display correct values.
* Country analytics display correctly.
* Department analytics display correctly.
* Salary distribution displays correctly.
* Loading state works.
* Error state works.

---

# 67. Analytics Test Cases

Test:

* Analytics page loads.
* Overview data is displayed.
* Country analytics are displayed.
* Department analytics are displayed.
* Salary distribution is displayed.
* Loading states are displayed.
* Empty states are displayed.
* API errors are handled correctly.
* Currency context is visible.

---

# 68. Testing Coverage Target

The goal is meaningful coverage of application behavior rather than achieving a specific percentage at the expense of useful tests.

Priority should be given to:

```text
Authentication
Authorization-related UI behavior
Employee search/filter/pagination
Employee details
Salary updates
NgRx reducers
NgRx effects
NgRx selectors
HTTP interceptor
Route guard
Important forms
Error handling
```

---

# 69. UI API Mapping

| UI Feature           | API                                      |
| -------------------- | ---------------------------------------- |
| Login                | `POST /api/auth/login`                   |
| Dashboard overview   | `GET /api/analytics/overview`            |
| Employee list        | `GET /api/employees`                     |
| Employee details     | `GET /api/employees/{id}`                |
| Salary history       | `GET /api/employees/{id}/salary-history` |
| Update salary        | `PUT /api/employees/{id}/salary`         |
| Countries            | `GET /api/countries`                     |
| Currencies           | `GET /api/currencies`                    |
| Country analytics    | `GET /api/analytics/by-country`          |
| Department analytics | `GET /api/analytics/by-department`       |
| Salary distribution  | `GET /api/analytics/salary-distribution` |

---

# 70. UI-to-Backend Data Flow

General flow:

```text
Angular Component
       ↓
NgRx Store
       ↓
Action
       ↓
Effect
       ↓
Angular Service
       ↓
HTTP Client
       ↓
JWT Interceptor
       ↓
Spring Boot REST API
       ↓
Service Layer
       ↓
Repository
       ↓
MySQL
```

Response:

```text
MySQL
  ↓
Spring Boot
  ↓
JSON Response
  ↓
Angular Service
  ↓
NgRx Effect
  ↓
Success Action
  ↓
Reducer
  ↓
NgRx Store
  ↓
Selector
  ↓
Component
  ↓
UI
```

---

# 71. Main User Journey

```text
                    ┌──────────┐
                    │  Login   │
                    └────┬─────┘
                         ↓
                  ┌──────────────┐
                  │  Dashboard   │
                  └──────┬───────┘
                         │
              ┌──────────┴───────────┐
              ↓                      ↓
       ┌─────────────┐        ┌────────────┐
       │  Employees  │        │ Analytics  │
       └──────┬──────┘        └────────────┘
              ↓
      ┌─────────────────┐
      │ Employee Details│
      └────────┬────────┘
               ↓
      ┌─────────────────┐
      │ Salary History  │
      └────────┬────────┘
               ↓
      ┌─────────────────┐
      │ Update Salary   │
      └─────────────────┘
```

---

# 72. UI Acceptance Criteria

## Authentication

* User can login using valid credentials.
* Invalid credentials show an appropriate error.
* Protected routes cannot be accessed without authentication.
* User can logout.
* JWT is attached to protected API requests.

## Dashboard

* Dashboard loads successfully.
* Employee count is displayed.
* Country/department information is displayed.
* Salary analytics are visualized with currency context.
* Loading and error states are handled.

## Employees

* Employee list loads using server-side pagination.
* User can search by name/email.
* User can filter by country.
* User can filter by department.
* User can sort supported columns.
* User can navigate between pages.
* User can open employee details.
* Employee state is managed through NgRx.

## Employee Details

* Employee information is displayed.
* Current salary is displayed with currency.
* Salary history is displayed.
* Salary update action is available to authorized users.

## Salary Update

* User can enter a new salary.
* User can select an effective date.
* Invalid input is rejected.
* User must confirm the update.
* Successful updates refresh current salary and salary history.
* Failed updates display an appropriate error.
* Duplicate submissions are prevented.

## Analytics

* Overview analytics load successfully.
* Country analytics are displayed.
* Department analytics are displayed.
* Salary distribution is displayed.
* Currency context is preserved.

## Accessibility

* Main functionality is keyboard accessible.
* Form controls have accessible labels.
* Errors are accessible to assistive technologies.
* Focus is managed appropriately for dialogs and navigation.

## Performance

* Employee list does not load all 10,000 employees.
* Search is debounced.
* Pagination is server-side.
* Routes/features are lazy-loaded where appropriate.
* Unnecessary API requests are avoided.
* NgRx selectors are used appropriately.

## Testing

* Important components have unit tests.
* NgRx reducers are tested.
* NgRx effects are tested.
* NgRx selectors are tested.
* Services are tested.
* Guards are tested.
* HTTP interceptor is tested.
* Important form validation is tested.
* Salary update flow is tested.
* Error and loading states are tested.

---

# 73. Final UI Screen List

The minimum required screens are:

```text
1. Login
2. Dashboard
3. Employee List
4. Employee Details
5. Salary Update Dialog/Form
6. Analytics
```

Supporting UI components:

```text
- Header
- Sidebar
- Search bar
- Country filter
- Department filter
- Employee table
- Pagination
- Salary history table
- Confirmation dialog
- Loading indicators
- Error states
- Empty states
- Snackbar notifications
- Analytics charts
```

---

# 74. UI Design Principles

The implementation should follow these principles:

1. **Simple** — HR users should be able to complete common tasks quickly.
2. **Consistent** — Common components and interaction patterns should be reused.
3. **Accessible** — The application should be usable with keyboard and assistive technologies.
4. **Responsive** — Core functionality should remain usable across desktop and tablet sizes.
5. **Performant** — Large employee datasets must be handled through server-side operations.
6. **Secure** — Authentication and authorization must be enforced by the backend.
7. **Maintainable** — UI functionality should be organized by feature.
8. **Testable** — Important business flows should have automated tests.
9. **Centralized** — Shared application state should be managed through NgRx.
10. **Currency-aware** — Salary data from different currencies must not be presented as directly comparable raw values.
11. **Traceable** — Salary changes should clearly expose their history and effective dates.

---

# 75. Source of Truth

This document defines the expected frontend behavior, state management, testing strategy, and UI structure.

The following documents should remain consistent with this document:

```text
requirement.md
architecture.md
database-design.md
api-design.md
ui-design.md
```

Any change to the API, database model, authentication flow, NgRx state model, or major UI behavior should be reflected in the relevant documentation before or alongside implementation.
