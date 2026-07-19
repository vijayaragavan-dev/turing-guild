# 🚀 Turing Guild Management System (TGMS)

> A modern, secure, and scalable **Full-Stack Event Management Platform** developed for the **Turing Guild Programming Club** to efficiently manage programming events, student participation, evaluations, and leaderboards.

---

## 📖 Overview

The **Turing Guild Management System (TGMS)** is designed to simplify the organization and execution of technical events within a college programming club.

The platform provides two dedicated portals:

- **Admin Portal** – Manage students, administrators, events, questions, submissions, and leaderboards.
- **Student Portal** – Secure login, participate in events, submit answers, and view rankings.

The application follows a production-style architecture with secure authentication, role-based authorization, responsive UI, and scalable backend design.

---

# ✨ Key Features

## 👨‍🎓 Student Portal

- Secure Batch Number Login
- Participate in Published Events
- Attempt Multiple Question Types
- Submit Event Responses
- View Scores & Results
- View Event Leaderboards
- Responsive Dashboard

---

## 👨‍💼 Admin Portal

- Secure Administrator Login
- Student Management
- Administrator Management
- Event Creation & Management
- Question Management
- Submission Evaluation
- Leaderboard Generation
- Performance Monitoring
- Role-Based Access Control

---

# 🛠 Technology Stack

## Backend

- Java 21
- Spring Boot 3.3
- Spring Security
- Spring Data JPA (Hibernate)
- JWT Authentication
- MySQL 8
- Flyway Database Migration
- Maven

---

## Frontend

- React 18
- TypeScript
- Vite
- Tailwind CSS
- React Router
- Axios
- Framer Motion
- Lucide Icons

---

## Database

- MySQL 8

---

## Testing

- JUnit 5
- Mockito
- H2 Database (Testing)

---

# 🏗 System Architecture

```
               React + TypeScript
                       │
                       │ REST API
                       ▼
         Spring Boot Application
 ┌──────────────────────────────────┐
 │ Controllers                      │
 │ Services                         │
 │ Spring Security                  │
 │ JWT Authentication               │
 │ JPA / Hibernate                  │
 └──────────────────────────────────┘
                       │
                       ▼
                  MySQL Database
```

---

# 📂 Project Structure

```
TGMS/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── application.yml
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── vite.config.ts
│
├── docs/
│
├── .env.example
│
└── README.md
```

---

# 🔐 Authentication & Security

The application follows enterprise-level authentication practices.

### Authentication

- JWT Authentication
- BCrypt Password Encryption
- Stateless Authentication
- Secure Session Management

### Authorization

- Role-Based Access Control (RBAC)

Supported Roles:

- ADMIN
- STUDENT

### Security Features

- Spring Security
- Password Encryption
- Protected Routes
- Secure REST APIs
- Input Validation
- SQL Injection Prevention
- Cross-Site Scripting (XSS) Protection
- Secure Authentication Flow

---

# 📦 Supported Event Categories

- Programming
- Aptitude
- Verbal
- Core Computer Science

---

# ❓ Supported Question Types

- Multiple Choice Questions (MCQ)
- Coding Problems
- Debugging Questions
- Output Prediction
- Fill Missing Code
- Logical Reasoning
- Numerical Aptitude
- Fill in the Blanks
- Match the Following

---

# ⚙ Prerequisites

Before running the project, ensure the following software is installed:

- Java 21 or later
- Maven 3.8+
- Node.js 18+
- npm
- MySQL 8+

---

# 🚀 Installation

## 1. Clone the Repository

```bash
git clone https://github.com/vijayaragavan-dev/turing-guild.git
cd turing-guild
```

---

## 2. Create Database

```sql
CREATE DATABASE turing_guild;
```

---

## 3. Configure Environment

Copy the environment file:

```bash
cp .env.example .env
```

Update your MySQL credentials and JWT configuration.

---

## 4. Start Backend

```bash
cd backend

mvn clean install

mvn spring-boot:run
```

Backend:

```
http://localhost:8081
```

---

## 5. Start Frontend

```bash
cd frontend

npm install

npm run dev
```

Frontend:

```
http://localhost:5173
```

---

# 👨‍💼 Default Administrator Account

> The initial administrator account is automatically created during application startup if no administrator exists.

| Field | Value |
|-------|-------|
| Email | Configurable |
| Password | Configurable |

> **Note:** Update the administrator credentials through the application configuration before deploying to production.

---

# 📑 API Documentation

Swagger UI

```
http://localhost:8081/swagger-ui.html
```

---

# 🧪 Testing

## Backend

```bash
cd backend

mvn test
```

---

## Frontend

```bash
cd frontend

npm run build
```

---

# 📈 Future Enhancements

- Excel Student Import
- CSV Student Import
- Coding Compiler Integration
- File Upload Support
- Email Notifications
- Event Analytics Dashboard
- PDF Report Generation
- Excel Export
- Real-Time Leaderboards
- Redis Caching
- Docker Deployment
- CI/CD Pipeline
- Multi-Department Support

---

# 📋 Project Highlights

- Enterprise-Level Project Structure
- Responsive UI Design
- Secure Authentication & Authorization
- Modular Backend Architecture
- Scalable Database Design
- RESTful APIs
- Role-Based Access Control
- JWT Security
- BCrypt Password Encryption
- Flyway Database Versioning
- Production-Oriented Code Organization

---

# 🤝 Contributing

Contributions are welcome.

1. Fork the repository.
2. Create a new feature branch.
3. Commit your changes.
4. Push to your branch.
5. Open a Pull Request.

---

# 📄 License

This project is developed for educational and academic purposes under the **Turing Guild Programming Club**.

---

# 👨‍💻 Developer

**Vijayaragavan U**

- 🎓 B.E. Computer Science and Engineering
- 💻 Java Full Stack Developer
- 🌐 GitHub: https://github.com/vijayaragavan-dev
- 🔗 LinkedIn: https://www.linkedin.com/in/vijaya-ragavan-ki10052007

---

⭐ If you found this project useful, consider giving it a **Star** on GitHub.
