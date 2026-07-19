# Entity-Relationship Diagram

```mermaid
erDiagram
    USERS {
        bigint id PK
        varchar batch_number UK
        varchar email UK
        varchar password_hash
        varchar full_name
        enum role "STUDENT, ADMIN"
        boolean is_active
        boolean first_login
        timestamp created_at
        timestamp updated_at
    }

    EVENTS {
        bigint id PK
        varchar title
        text description
        enum category "PROGRAMMING, APTITUDE, VERBAL, CORE_CS"
        enum status "DRAFT, PUBLISHED, CLOSED, DELETED"
        int duration_minutes
        int total_marks
        timestamp start_time
        timestamp end_time
        bigint created_by FK
        timestamp created_at
        timestamp updated_at
    }

    QUESTIONS {
        bigint id PK
        bigint event_id FK
        enum question_type "MCQ, CODING, DEBUGGING, OUTPUT_PREDICTION, FILL_MISSING_CODE, LOGICAL, NUMERICAL, FILL_BLANKS, MATCH"
        text question_text
        int marks
        int order_index
        text coding_template
        text expected_output
        text sample_input
        text sample_output
        timestamp created_at
    }

    OPTIONS {
        bigint id PK
        bigint question_id FK
        varchar option_text
        boolean is_correct
        int order_index
    }

    SUBMISSIONS {
        bigint id PK
        bigint user_id FK
        bigint event_id FK
        enum status "IN_PROGRESS, SUBMITTED, EVALUATED"
        int total_score
        timestamp submitted_at
        timestamp created_at
        timestamp updated_at
    }

    ANSWERS {
        bigint id PK
        bigint submission_id FK
        bigint question_id FK
        text answer_text
        boolean is_correct
        int score_awarded
        text feedback
    }

    LEADERBOARD {
        bigint id PK
        bigint user_id FK
        bigint event_id FK
        int total_score
        int rank
        timestamp updated_at
    }

    USERS ||--o{ EVENTS : "creates"
    USERS ||--o{ SUBMISSIONS : "submits"
    USERS ||--o{ LEADERBOARD : "ranks"
    EVENTS ||--o{ QUESTIONS : "contains"
    EVENTS ||--o{ SUBMISSIONS : "has"
    EVENTS ||--o{ LEADERBOARD : "tracks"
    QUESTIONS ||--o{ OPTIONS : "has"
    QUESTIONS ||--o{ ANSWERS : "answered"
    SUBMISSIONS ||--o{ ANSWERS : "contains"
```

## Normalization Notes

- **3NF** throughout: no transitive dependencies, all non-key attributes depend on the primary key
- **No deliberate denormalization** — leaderboard scores are computed and stored for performance but derived from submissions (could be regenerated)
- **Indexes**: batch_number, email (unique), event_id + user_id (submission uniqueness), question_id (answer lookup)
