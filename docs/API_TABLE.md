# REST API Table

## Authentication

| Method | Path | Auth | Request | Response | Status |
|--------|------|------|---------|----------|--------|
| POST | /api/auth/login | None | `{ batchNumber/email, password }` | `{ accessToken, refreshToken, user }` | 200 |
| POST | /api/auth/refresh | Refresh Token | `{ refreshToken }` | `{ accessToken }` | 200 |
| POST | /api/auth/change-password | Any | `{ oldPassword, newPassword }` | `{ message }` | 200 |

## Students (Admin)

| Method | Path | Auth | Request | Response | Status |
|--------|------|------|---------|----------|--------|
| GET | /api/admin/students | Admin | - | `[{ id, batchNumber, email, fullName, isActive, createdAt }]` | 200 |
| POST | /api/admin/students | Admin | `{ batchNumber, email, fullName, password }` | `{ id, batchNumber, ... }` | 201 |
| PUT | /api/admin/students/{id} | Admin | `{ email, fullName, isActive }` | `{ id, ... }` | 200 |
| POST | /api/admin/students/{id}/reset-password | Admin | - | `{ message }` | 200 |
| DELETE | /api/admin/students/{id} | Admin | - | `{ message }` | 200 |

## Events (Admin)

| Method | Path | Auth | Request | Response | Status |
|--------|------|------|---------|----------|--------|
| GET | /api/admin/events | Admin | - | `[{ id, title, category, status, ... }]` | 200 |
| POST | /api/admin/events | Admin | `{ title, description, category, ... }` | `{ id, ... }` | 201 |
| PUT | /api/admin/events/{id} | Admin | `{ title, description, ... }` | `{ id, ... }` | 200 |
| PUT | /api/admin/events/{id}/publish | Admin | - | `{ status: "PUBLISHED" }` | 200 |
| PUT | /api/admin/events/{id}/close | Admin | - | `{ status: "CLOSED" }` | 200 |
| DELETE | /api/admin/events/{id} | Admin | - | `{ message }` | 200 |
| GET | /api/admin/events/{id}/questions | Admin | - | `[{ id, questionType, ... }]` | 200 |
| POST | /api/admin/events/{id}/questions | Admin | `{ questionType, questionText, ... }` | `{ id, ... }` | 201 |
| PUT | /api/admin/questions/{id} | Admin | `{ questionText, marks, ... }` | `{ id, ... }` | 200 |
| DELETE | /api/admin/questions/{id} | Admin | - | `{ message }` | 200 |

## Events (Student)

| Method | Path | Auth | Request | Response | Status |
|--------|------|------|---------|----------|--------|
| GET | /api/events | Student | - | `[{ id, title, category, startTime, ... }]` | 200 |
| GET | /api/events/{id} | Student | - | `{ id, title, questions: [...] }` | 200 |
| POST | /api/events/{id}/join | Student | - | `{ submissionId }` | 201 |

## Submissions

| Method | Path | Auth | Request | Response | Status |
|--------|------|------|---------|----------|--------|
| POST | /api/submissions/{id}/submit | Student | `{ answers: [{ questionId, answerText }] }` | `{ totalScore, ... }` | 200 |
| GET | /api/submissions/{id} | Student | - | `{ id, answers: [...] }` | 200 |
| GET | /api/admin/submissions | Admin | `?eventId=` | `[{ id, user, event, status, ... }]` | 200 |
| GET | /api/admin/submissions/{id} | Admin | - | `{ id, answers: [...] }` | 200 |
| PUT | /api/admin/submissions/{id}/evaluate | Admin | `{ answers: [{ id, scoreAwarded, feedback }] }` | `{ totalScore }` | 200 |

## Results & Leaderboard

| Method | Path | Auth | Request | Response | Status |
|--------|------|------|---------|----------|--------|
| GET | /api/results/me | Student | - | `[{ event, totalScore, rank }]` | 200 |
| GET | /api/leaderboard/{eventId} | Any | - | `[{ rank, user, totalScore }]` | 200 |
| GET | /api/admin/leaderboard/{eventId} | Admin | - | `[{ rank, user, totalScore }]` | 200 |
| POST | /api/admin/leaderboard/{eventId}/recompute | Admin | - | `{ message }` | 200 |
| GET | /api/admin/results/{eventId}/export | Admin | - | CSV/Excel file | 200 |
