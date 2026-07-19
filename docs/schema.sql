-- ============================================================
-- TGMS (Turing Guild Management System)
-- Complete Database Schema
-- MySQL 8+
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS leaderboard;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS submissions;
DROP TABLE IF EXISTS options;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- USERS
-- ============================================================

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    batch_number VARCHAR(50) UNIQUE,
    email VARCHAR(255) UNIQUE,

    password_hash VARCHAR(255) NOT NULL,

    full_name VARCHAR(255) NOT NULL,

    role ENUM('STUDENT','ADMIN')
        NOT NULL DEFAULT 'STUDENT',

    is_active BOOLEAN
        NOT NULL DEFAULT TRUE,

    first_login BOOLEAN
        NOT NULL DEFAULT TRUE,

    last_login TIMESTAMP NULL,

    created_at TIMESTAMP
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_users_role(role),
    INDEX idx_users_active(is_active),
    INDEX idx_users_batch(batch_number)
);

-- ============================================================
-- EVENTS
-- ============================================================

CREATE TABLE events (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    title VARCHAR(255) NOT NULL,

    description TEXT,

    category ENUM(
        'PROGRAMMING',
        'APTITUDE',
        'VERBAL',
        'CORE_CS'
    ) NOT NULL,

    status ENUM(
        'DRAFT',
        'PUBLISHED',
        'CLOSED',
        'DELETED'
    ) NOT NULL DEFAULT 'DRAFT',

    duration_minutes INT NOT NULL,

    total_marks INT NOT NULL DEFAULT 0,

    start_time TIMESTAMP NULL,

    end_time TIMESTAMP NULL,

    created_by BIGINT NOT NULL,

    created_at TIMESTAMP
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_events_creator
        FOREIGN KEY(created_by)
        REFERENCES users(id),

    INDEX idx_events_status(status),
    INDEX idx_events_category(category),
    INDEX idx_events_creator(created_by),
    INDEX idx_events_title(title)
);

-- ============================================================
-- QUESTIONS
-- ============================================================

CREATE TABLE questions (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    event_id BIGINT NOT NULL,

    question_type ENUM(
        'MCQ',
        'CODING',
        'DEBUGGING',
        'OUTPUT_PREDICTION',
        'FILL_MISSING_CODE',
        'LOGICAL',
        'NUMERICAL',
        'FILL_BLANKS',
        'MATCH'
    ) NOT NULL,

    difficulty ENUM(
        'EASY',
        'MEDIUM',
        'HARD'
    ) DEFAULT 'MEDIUM',

    question_text TEXT NOT NULL,

    marks INT NOT NULL DEFAULT 1,

    order_index INT NOT NULL,

    coding_template TEXT,

    expected_output TEXT,

    sample_input TEXT,

    sample_output TEXT,

    created_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_questions_event
        FOREIGN KEY(event_id)
        REFERENCES events(id)
        ON DELETE CASCADE,

    UNIQUE KEY uk_event_question_order(event_id, order_index),

    INDEX idx_questions_event(event_id),
    INDEX idx_questions_type(question_type)
);

-- ============================================================
-- OPTIONS
-- ============================================================

CREATE TABLE options (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    question_id BIGINT NOT NULL,

    option_text TEXT NOT NULL,

    is_correct BOOLEAN
        NOT NULL DEFAULT FALSE,

    order_index INT NOT NULL,

    CONSTRAINT fk_options_question
        FOREIGN KEY(question_id)
        REFERENCES questions(id)
        ON DELETE CASCADE,

    UNIQUE KEY uk_question_option_order(question_id, order_index),

    INDEX idx_options_question(question_id)
);

-- ============================================================
-- SUBMISSIONS
-- ============================================================

CREATE TABLE submissions (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    user_id BIGINT NOT NULL,

    event_id BIGINT NOT NULL,

    status ENUM(
        'IN_PROGRESS',
        'SUBMITTED',
        'EVALUATED'
    ) DEFAULT 'IN_PROGRESS',

    total_score INT DEFAULT 0,

    submitted_at TIMESTAMP NULL,

    evaluated_at TIMESTAMP NULL,

    evaluated_by BIGINT NULL,

    created_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_submission_user
        FOREIGN KEY(user_id)
        REFERENCES users(id),

    CONSTRAINT fk_submission_event
        FOREIGN KEY(event_id)
        REFERENCES events(id),

    CONSTRAINT fk_submission_evaluator
        FOREIGN KEY(evaluated_by)
        REFERENCES users(id),

    UNIQUE KEY uk_submission(user_id,event_id),

    INDEX idx_submission_event(event_id),
    INDEX idx_submission_status(status)
);

-- ============================================================
-- ANSWERS
-- ============================================================

CREATE TABLE answers (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    submission_id BIGINT NOT NULL,

    question_id BIGINT NOT NULL,

    answer_text TEXT,

    is_correct BOOLEAN,

    score_awarded INT DEFAULT 0,

    feedback TEXT,

    CONSTRAINT fk_answer_submission
        FOREIGN KEY(submission_id)
        REFERENCES submissions(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_answer_question
        FOREIGN KEY(question_id)
        REFERENCES questions(id),

    UNIQUE KEY uk_submission_question(submission_id, question_id),

    INDEX idx_answers_submission(submission_id)
);

-- ============================================================
-- LEADERBOARD
-- ============================================================

CREATE TABLE leaderboard (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    user_id BIGINT NOT NULL,

    event_id BIGINT NOT NULL,

    total_score INT DEFAULT 0,

    percentage DECIMAL(5,2) DEFAULT 0.00,

    leaderboard_rank INT,

    updated_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_leaderboard_user
        FOREIGN KEY(user_id)
        REFERENCES users(id),

    CONSTRAINT fk_leaderboard_event
        FOREIGN KEY(event_id)
        REFERENCES events(id),

    UNIQUE KEY uk_leaderboard(user_id,event_id),

    INDEX idx_leaderboard_event(event_id),
    INDEX idx_leaderboard_rank(event_id, leaderboard_rank)
);