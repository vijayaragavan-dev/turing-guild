# Software Requirements Specification (SRS)
## Turing Guild Management System (TGMS)

### 1. Introduction

#### 1.1 Purpose
TGMS manages programming events, student participation, evaluation, and leaderboards for the Turing Guild college programming club.

#### 1.2 Scope
Web-based application with role-based access for Students and Admins.

#### 1.3 Definitions
- **Batch Number**: Unique student identifier used as login username
- **Event**: A programming/aptitude/verbal/CS challenge with questions
- **Submission**: A student's answers to an event's questions

---

### 2. Functional Requirements

#### 2.1 Authentication
- FR-1: Students login with Batch Number + admin-assigned default password
- FR-2: Admins login with email + password
- FR-3: First login forces password change
- FR-4: JWT access + refresh token flow
- FR-5: BCrypt (cost ≥ 12) password hashing

#### 2.2 Student Capabilities
- FR-6: Browse published events
- FR-7: Join events
- FR-8: Submit answers within event constraints
- FR-9: View own results
- FR-10: View leaderboard

#### 2.3 Admin Capabilities
- FR-11: Create, edit, publish, close, delete events
- FR-12: Manage students (create, deactivate, reset password)
- FR-13: View and evaluate submissions
- FR-14: Generate and export results
- FR-15: Manage and recompute leaderboard

#### 2.4 Event System
- FR-16: Fixed categories: Programming, Aptitude, Verbal, Core CS
- FR-17: Question types per category as specified in prompt
- FR-18: Extensible via enum + handler (Strategy pattern)

---

### 3. Non-Functional Requirements

| Requirement | Target |
|---|---|
| Security | JWT auth, RBAC, input validation, parameterized queries |
| Performance | < 2s response for list endpoints, no N+1 queries |
| Availability | Graceful error handling, health checks |
| Maintainability | Clean package structure, separation of concerns |
| Testability | Unit + integration tests, H2 for test DB |
| Deployability | Docker support, environment variable config |

---

### 4. Constraints
- Java 21 / Spring Boot backend
- React + Vite + Tailwind frontend
- MySQL database
- Must run locally without external services beyond MySQL
