use matching_validation;

CREATE TABLE user 
(
  user_id         INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name            VARCHAR(30),
  email           VARCHAR(120),
  password        VARCHAR(30)
);

INSERT INTO user (name, email,password) VALUES ('Nathan Bunker', 'nbunker@immregistries.org', 'welcome');

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
  patient_data_a       TEXT NOT NULL,
  patient_data_b       TEXT NOT NULL,
  expect_status        VARCHAR(20) NOT NULL,
  user_id              INTEGER NOT NULL,
  update_date          DATETIME NOT NULL,
  data_source          VARCHAR(120) NOT NULL
);

CREATE TABLE patient
(
  patient_id  INT PRIMARY KEY,
  name_first      VARCHAR(100),
  name_last       VARCHAR(100),
  name_middle     VARCHAR(100),
  name_suffix     VARCHAR(100),
  birth_date      VARCHAR(100),
  grd_name_first  VARCHAR(100),
  grd_name_last   VARCHAR(100),
  mth_name_first  VARCHAR(100),
  mth_name_middle VARCHAR(100),
  mth_name_maiden VARCHAR(100),
  phone           VARCHAR(100),
  race            VARCHAR(100),
  ethnicity       VARCHAR(100),
  adr_street1     VARCHAR(100),
  adr_street2     VARCHAR(100),
  adr_city        VARCHAR(100),
  adr_state       VARCHAR(100),
  adr_zip         VARCHAR(100),
  gender          VARCHAR(100),
  birth_type      VARCHAR(100),
  birth_order     VARCHAR(100),
  mrns            VARCHAR(100),
  ssn             VARCHAR(100),
  medicaid        VARCHAR(100),
  shot_history    VARCHAR(100)
);

