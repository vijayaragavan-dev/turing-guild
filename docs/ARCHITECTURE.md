# System Architecture

```mermaid
graph TB
    subgraph Frontend
        React[React + Vite]
        Router[React Router]
        Axios[Axios HTTP Client]
    end

    subgraph Backend
        subgraph Security
            JWT[JWT Filter]
            RBAC[Role-Based Access]
        end
        
        subgraph Controllers
            AuthCtrl[AuthController]
            StudentCtrl[StudentController]
            EventCtrl[EventController]
            SubmissionCtrl[SubmissionController]
            LeaderboardCtrl[LeaderboardController]
        end

        subgraph Services
            AuthService[AuthService]
            StudentService[StudentService]
            EventService[EventService]
            SubmissionService[SubmissionService]
            EvaluationService[EvaluationService]
            LeaderboardService[LeaderboardService]
        end

        subgraph Repositories
            UserRepo[UserRepository]
            EventRepo[EventRepository]
            QuestionRepo[QuestionRepository]
            SubmissionRepo[SubmissionRepository]
            LeaderboardRepo[LeaderboardRepository]
        end

        subgraph Entities
            User[User]
            Event[Event]
            Question[Question]
            Option[Option]
            Submission[Submission]
            Answer[Answer]
            Leaderboard[Leaderboard]
        end
    end

    subgraph Database
        MySQL[(MySQL)]
    end

    subgraph External
        Swagger[Swagger UI]
        Actuator[Actuator]
    end

    React --> Router
    Router --> Axios
    Axios --> JWT
    JWT --> RBAC
    RBAC --> AuthCtrl
    RBAC --> StudentCtrl
    RBAC --> EventCtrl
    RBAC --> SubmissionCtrl
    RBAC --> LeaderboardCtrl

    AuthCtrl --> AuthService
    StudentCtrl --> StudentService
    EventCtrl --> EventService
    SubmissionCtrl --> SubmissionService
    SubmissionCtrl --> EvaluationService
    LeaderboardCtrl --> LeaderboardService

    AuthService --> UserRepo
    StudentService --> UserRepo
    EventService --> EventRepo
    EventService --> QuestionRepo
    SubmissionService --> SubmissionRepo
    LeaderboardService --> LeaderboardRepo

    UserRepo --> User
    EventRepo --> Event
    QuestionRepo --> Question
    QuestionRepo --> Option
    SubmissionRepo --> Submission
    SubmissionRepo --> Answer
    LeaderboardRepo --> Leaderboard

    UserRepo --> MySQL
    EventRepo --> MySQL
    QuestionRepo --> MySQL
    SubmissionRepo --> MySQL
    LeaderboardRepo --> MySQL

    Backend --> Swagger
    Backend --> Actuator
```

## Design Pattern: Strategy Pattern for Event Types

The event system uses the **Strategy pattern** because each question type (MCQ, Coding, Debugging, etc.) has distinct validation and scoring logic. By defining a `QuestionHandler` interface and implementing it per type, adding a new category/type requires only:
1. A new enum value in `QuestionType`
2. A new handler class implementing `QuestionHandler`

No existing code needs modification (Open/Closed Principle).
