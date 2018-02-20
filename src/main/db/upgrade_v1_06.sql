use matching_validation;

CREATE TABLE user 
(
  user_id         INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name            VARCHAR(30),
  email           VARCHAR(120),
  password        VARCHAR(30)
);

CREATE TABLE match_set
(
  match_set_id    INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  label           VARCHAR(250) NOT NULL,
  update_date     DATETIME NOT NULL
);

CREATE TABLE match_item
(
  match_item_id        INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  match_set_id         INTEGER NOT NULL,
  label                VARCHAR(250) NOT NULL,
  description          VARCHAR(1000),
  patient_data_a       VARCHAR(9000) NOT NULL,
  patient_data_b       VARCHAR(9000) NOT NULL,
  expect_status        VARCHAR(20) NOT NULL,
  user_id              INTEGER NOT NULL,
  update_date          DATETIME NOT NULL,
  data_source          VARCHAR(120) NOT NULL
);
