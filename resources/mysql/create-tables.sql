CREATE TABLE users (
   id            INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
   slack_id      VARCHAR(16) NOT NULL UNIQUE,
   deleted       BOOLEAN NOT NULL DEFAULT FALSE,
   created_time  DATETIME NOT NULL DEFAULT NOW(),
   modified_time TIMESTAMP
);

CREATE TABLE karma (
   id           INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
   recipient    INT UNSIGNED NOT NULL REFERENCES users(id),
   donor        INT UNSIGNED NOT NULL REFERENCES users(id),
   points       INT NOT NULL,
   created_time DATETIME NOT NULL DEFAULT NOW()
);

CREATE INDEX user_index ON users(slack_id);
