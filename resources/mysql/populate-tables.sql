INSERT INTO users
   (slack_id, created_time)
VALUES
   ('NYY99', NOW()),
   ('OAK24', NOW())
;

INSERT INTO karma
   (recipient, donor, points, created_time)
VALUES
   (1, 2, 2, NOW()),
   (1, 2, 4, NOW()),
   (1, 2, -1, NOW())
;
