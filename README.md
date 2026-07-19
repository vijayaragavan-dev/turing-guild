# Turing Guild Management System (TGMS)

A full-stack web application for managing programming events, student participation, evaluation, and leaderboards for the Turing Guild college programming club.

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.3, Spring Security, JPA/Hibernate, MySQL, Flyway
- **Frontend:** React 18, TypeScript, Vite, Tailwind CSS, React Router
- **Testing:** JUnit 5, Mockito, H2 Database (test)

## Prerequisites

- Java 21+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0+

## Setup

### 1. Database

```sql
CREATE DATABASE turing_guild;
```

### 2. Backend

```bash
cd backend
cp ../.env.example .env
# Edit .env with your MySQL credentials
mvn clean install
mvn spring-boot:run
```

Backend runs at http://localhost:8081

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at http://localhost:5173

## Default Admin Credentials

- **Email:** admin@turingguild.com
- **Password:** admin123

## API Documentation

Swagger UI available at http://localhost:8081/swagger-ui.html

## Features

### Student
- Login with Batch Number
- Browse and join published events
- Submit answers to questions
- View results and leaderboard

### Admin
- Manage students (CRUD)
- Create events with multiple question types
- Publish/close/delete events
- Review and evaluate submissions
- View and recompute leaderboards

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| DB_USERNAME | MySQL username | root |
| DB_PASSWORD | MySQL password | root |
| JWT_SECRET | JWT signing key (32+ chars) | - |
| VITE_API_URL | Backend API URL | http://localhost:8080 |

## Testing

```bash
# Backend tests
cd backend
mvn test

# Frontend build check
cd frontend
npm run build
```

## Known Gaps

- No email verification for student registration
- No file upload for coding submissions
- No WebSocket for real-time updates
- No PDF/Excel export for results

## Future Scalability

- Add Redis for rate limiting and caching
- Implement file upload for coding submissions
- Add WebSocket for real-time leaderboard updates
- Add pagination for all list endpoints
- Implement email notifications
