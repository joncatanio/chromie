CREATE TABLE users (
   id            INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
   slack_id      VARCHAR(16) NOT NULL UNIQUE,
   deleted       BOOLEAN DEFAULT FALSE,
   created_time  TIMESTAMP NOT NULL,
   modified_time TIMESTAMP
);

CREATE TABLE karma (
   id           INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
   recipient    INT UNSIGNED REFERENCES Users(id),
   donor        INT UNSIGNED REFERENCES Users(id),
   points       INT NOT NULL,
   created_time TIMESTAMP NOT NULL
);

CREATE INDEX user_index ON Users(slack_id);
