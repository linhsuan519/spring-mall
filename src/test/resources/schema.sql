CREATE TABLE IF NOT EXISTS user
(
    user_id            INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email              VARCHAR(256) NOT NULL UNIQUE,
    role               VARCHAR(16)  NOT NULL,
    password           VARCHAR(256) NOT NULL,
    created_date       TIMESTAMP    NOT NULL,
    last_modified_date TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS court
(
    court_id           INT           NOT NULL PRIMARY KEY AUTO_INCREMENT,
    court_name         VARCHAR(128)  NOT NULL,
    court_type         VARCHAR(32)   NOT NULL,
    location           VARCHAR(256)  NOT NULL,
    price_per_hour     INT           NOT NULL,
    status             VARCHAR(32)   NOT NULL DEFAULT 'AVAILABLE',
    description        VARCHAR(1024),
    image_url          VARCHAR(256),
    created_date       TIMESTAMP     NOT NULL,
    last_modified_date TIMESTAMP     NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation
(
    reservation_id     INT        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id            INT        NOT NULL,
    court_id           INT        NOT NULL,
    reservation_date   DATE       NOT NULL,
    start_time         TIME       NOT NULL,
    end_time           TIME       NOT NULL,
    total_price        INT        NOT NULL,
    status             VARCHAR(32) NOT NULL DEFAULT 'PENDING',
    created_date       TIMESTAMP  NOT NULL,
    last_modified_date TIMESTAMP  NOT NULL
);