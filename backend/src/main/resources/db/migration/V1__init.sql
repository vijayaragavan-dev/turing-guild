CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_number VARCHAR(50) UNIQUE,
    email VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    first_login BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_role (role),
    INDEX idx_users_active (is_active)
);

CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category ENUM('PROGRAMMING', 'APTITUDE', 'VERBAL', 'CORE_CS') NOT NULL,
    status ENUM('DRAFT', 'PUBLISHED', 'CLOSED', 'DELETED') NOT NULL DEFAULT 'DRAFT',
    duration_minutes INT NOT NULL,
    total_marks INT NOT NULL DEFAULT 0,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_events_status (status),
    INDEX idx_events_category (category),
    INDEX idx_events_created_by (created_by)
);

CREATE TABLE questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    question_type ENUM('MCQ', 'CODING', 'DEBUGGING', 'OUTPUT_PREDICTION', 'FILL_MISSING_CODE', 'LOGICAL', 'NUMERICAL', 'FILL_BLANKS', 'MATCH') NOT NULL,
    question_text TEXT NOT NULL,
    marks INT NOT NULL DEFAULT 1,
    order_index INT NOT NULL DEFAULT 0,
    coding_template TEXT,
    expected_output TEXT,
    sample_input TEXT,
    sample_output TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    INDEX idx_questions_event (event_id),
    INDEX idx_questions_type (question_type)
);

CREATE TABLE options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    order_index INT NOT NULL DEFAULT 0,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    INDEX idx_options_question (question_id)
);

CREATE TABLE submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    status ENUM('IN_PROGRESS', 'SUBMITTED', 'EVALUATED') NOT NULL DEFAULT 'IN_PROGRESS',
    total_score INT NOT NULL DEFAULT 0,
    submitted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (event_id) REFERENCES events(id),
    UNIQUE KEY uk_submission_user_event (user_id, event_id),
    INDEX idx_submissions_event (event_id),
    INDEX idx_submissions_status (status)
);

CREATE TABLE answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    submission_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    answer_text TEXT,
    is_correct BOOLEAN,
    score_awarded INT NOT NULL DEFAULT 0,
    feedback TEXT,
    FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id),
    UNIQUE KEY uk_answer_submission_question (submission_id, question_id),
    INDEX idx_answers_submission (submission_id)
);

CREATE TABLE leaderboard (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    total_score INT NOT NULL DEFAULT 0,
    rank_position INT,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (event_id) REFERENCES events(id),
    UNIQUE KEY uk_leaderboard_user_event (user_id, event_id),
    INDEX idx_leaderboard_event (event_id),
    INDEX idx_leaderboard_rank (event_id, rank_position)
);
