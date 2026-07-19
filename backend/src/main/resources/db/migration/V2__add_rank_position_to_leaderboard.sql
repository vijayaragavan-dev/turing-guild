-- V2: Add missing rank_position column to leaderboard table
-- The turing_guild database was created from an older version of V1__init.sql
-- that did not include rank_position. Flyway baseline-on-migrate prevented
-- re-running V1, so we add the missing column here.

SET @column_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'leaderboard'
      AND COLUMN_NAME = 'rank_position'
);

SET @sql = IF(@column_exists = 0,
    'ALTER TABLE leaderboard ADD COLUMN rank_position INT',
    'SELECT 1'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
