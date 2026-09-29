# LeaveTrack: Employee Leave Management System

A role-based REST API where employees apply for leave and managers approve or reject requests. Built with Java, Spring Boot, Spring Data JPA, Spring Security and MySQL.

## Features
- Employees apply for leave, view their balance and request history
- Managers view pending requests and approve or reject them
- Leave balance is deducted **only on approval**, never on apply or reject
- Requests exceeding the available balance are rejected with a clear error
- Role-based access with Spring Security (HTTP Basic, BCrypt-hashed passwords)
- Input validation and consistent JSON error responses

## Tech Stack
Java 21, Spring Boot, Spring Web, Spring Data JPA (Hibernate), Spring Security, MySQL, Maven, JUnit and Mockito

## Architecture
Layered: `controller` (HTTP) -> `service` (business rules) -> `repository` (database) -> MySQL.
Entities: `Employee`, `LeaveType`, `LeaveRequest` (many-to-one relationships).

## API Endpoints

| Method | URL | Access |
|---|---|---|
| POST | `/api/leave/apply` | Any logged-in user |
| GET | `/api/leave/balance/{employeeId}` | Any logged-in user |
| GET | `/api/leave/employee/{employeeId}` | Any logged-in user |
| GET | `/api/leave/pending` | MANAGER |
| PUT | `/api/leave/{id}/approve` | MANAGER |
| PUT | `/api/leave/{id}/reject` | MANAGER |

## Run Locally
1. Install JDK 21 and MySQL, then create the database: `CREATE DATABASE leavetrack;`
2. Set your database password as an environment variable:
   - PowerShell: `$env:DB_PASSWORD="your_password"`
3. Start the app: `./mvnw spring-boot:run`
4. Demo users (created on first start, password `temp123`):
   - `manager@leavetrack.com` (MANAGER)
   - `employee@leavetrack.com` (EMPLOYEE)

## Example Request
```json
POST /api/leave/apply
{
  "employeeId": 2,
  "leaveTypeId": 1,
  "startDate": "2026-10-12",
  "endDate": "2026-10-14",
  "reason": "Family function"
}
```

## Known Limitations and Next Steps
- Any logged-in user can view another employee's data by changing the id (fix: read the user from the security context)
- No audit log of who approved what and when
- No yearly balance reset or accrual
- Balance is a single total, not tracked per leave type