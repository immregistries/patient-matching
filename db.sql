CREATE DATABASE matching_validation;

use matching_validation;

create user mv_web identified by 'cArn88r0w';

GRANT SELECT, INSERT, DELETE, UPDATE, LOCK TABLES, EXECUTE ON matching_validation.* TO 'mv_web'@'%';


CREATE TABLE patient
(
  patient_id INT PRIMARY KEY,
  link_with  INT,
  last_name  varchar(100),
  first_name  varchar(100),
  dob         varchar(10),
  middle_name varchar(100),
  suffix varchar(100),
  sex varchar(1),
  mom_maiden varchar(100),
  mom_last varchar(100),
  mom_first varchar(100),
  mom_middle varchar(100),
  vac_name varchar(200),
  vac_code varchar(3),
  vac_mfr varchar(100),
  vac_date varchar(10)
);


-- new tables

CREATE TABLE match_test_set
(
  match_test_set_id  INT PRIMARY KEY,
  
);

CREATE TABLE match_test_case
(
  match_test_case_id  INT PRIMARY KEY,
  patient_a_id        INT,
  patient_b_id        INT,
  expect_status       VARCHAR(30),
  description         VARCHAR(300),
  signature           VARCHAR(500)
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

