-- Drop old tables
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS court;

-- Activity table
CREATE TABLE IF NOT EXISTS activity (
    activity_id          INT AUTO_INCREMENT PRIMARY KEY,
    host_user_id         INT NOT NULL,
    title                VARCHAR(100) NOT NULL,
    location             VARCHAR(200) NOT NULL,
    scheduled_time       DATETIME NOT NULL,
    max_participants     INT NOT NULL,
    current_participants INT NOT NULL DEFAULT 1,
    skill_level          ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') NOT NULL,
    status               ENUM('OPEN', 'FULL', 'CANCELLED', 'COMPLETED') NOT NULL DEFAULT 'OPEN',
    description          TEXT,
    created_at           DATETIME NOT NULL,
    FOREIGN KEY (host_user_id) REFERENCES user(user_id)
);

-- Activity participant table
CREATE TABLE IF NOT EXISTS activity_participant (
    participant_id INT AUTO_INCREMENT PRIMARY KEY,
    activity_id    INT NOT NULL,
    user_id        INT NOT NULL,
    status         ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'APPROVED',
    joined_at      DATETIME NOT NULL,
    FOREIGN KEY (activity_id) REFERENCES activity(activity_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    UNIQUE KEY uq_activity_user (activity_id, user_id)
);
