create database matching_validation;

CREATE USER 'mv_web'@'localhost' IDENTIFIED BY 'cArn88r0w';

GRANT ALL PRIVILEGES ON matching_validation.* TO 'mv_web'@'localhost'; 