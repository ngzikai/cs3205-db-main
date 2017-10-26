CREATE DATABASE CS3205;
USE CS3205;
CREATE TABLE user(
   uid  INT NOT NULL  AUTO_INCREMENT,
   username VARCHAR(20) NOT NULL UNIQUE,
   password VARCHAR(255)  NOT NULL,
   password2 VARCHAR(255)  NOT NULL,
   salt VARCHAR(50)  NOT NULL,
   salt2 VARCHAR(50)  NOT NULL,
   firstname  VARCHAR (20)  NOT NULL,
   lastname  VARCHAR (20)  NOT NULL,
   nric  CHAR (10)  NOT NULL,
   dob  DATE  NOT NULL,
   sex  ENUM('M', 'F')  NOT NULL,
   phone1  VARCHAR (20)  NOT NULL,
   phone2  VARCHAR (20),
   phone3  VARCHAR (20),
   address1 VARCHAR(255) NOT NULL,
   address2 VARCHAR(255),
   address3 VARCHAR(255),
   zipcode1 INT NOT NULL,
   zipcode2 INT,
   zipcode3 INT,
   qualify INT NOT NULL,
   bloodtype VARCHAR(3) NOT NULL,
   nfcid VARCHAR(255),
   secret VARCHAR(255),
   ethnicity VARCHAR(255) NOT NULL,
   nationality VARCHAR(255) NOT NULL,
   drug_allergy TINYINT(1) NOT NULL,
   PRIMARY KEY (uid)
);


CREATE TABLE treatment(
   treatment_id INT UNIQUE NOT NULL AUTO_INCREMENT,
   patient_id INT NOT NULL,
   therapist_id INT NOT NULL,
   status TINYINT(1) DEFAULT 0,
   current_consent TINYINT(1) DEFAULT 0,
   future_consent TINYINT(1) DEFAULT 0,
   PRIMARY KEY(patient_id, therapist_id),
   FOREIGN KEY (patient_id) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (therapist_id) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE data(
   rid INT NOT NULL AUTO_INCREMENT,
   uid INT NOT NULL,
   type ENUM('Heart Rate', 'Images', 'Time Series', 'File') NOT NULL,
   subtype VARCHAR(20),
   title VARCHAR(200) NOT NULL,
   creationdate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
   modifieddate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
   content VARCHAR(100) NOT NULL,
   PRIMARY KEY(rid),
   FOREIGN KEY (uid) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE inclusion(
   inclusion_id INT NOT NULL AUTO_INCREMENT,
   report_id INT NOT NULL,
   record_id INT NOT NULL,
   PRIMARY KEY(inclusion_id),
   FOREIGN KEY (report_id) REFERENCES data(rid) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (record_id) REFERENCES data(rid) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE consent(
   consent_id INT UNIQUE NOT NULL AUTO_INCREMENT,
   uid INT NOT NULL,
   rid INT NOT NULL,
   status TINYINT(1),
   PRIMARY KEY(uid, rid),
   FOREIGN KEY (uid) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (rid) REFERENCES data(rid) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE `condition`(
   condition_id INT NOT NULL,
   condition_name VARCHAR(255) NOT NULL,
   PRIMARY KEY (condition_id)
);

CREATE TABLE diagnosis(
   diagnosis_id INT NOT NULL AUTO_INCREMENT,
   patient_id INT NOT NULL,
   condition_id INT NOT NULL,
   date_diagnosed TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
   date_treated TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
   PRIMARY KEY(diagnosis_id),
   FOREIGN KEY (patient_id) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (condition_id) REFERENCES `condition`(condition_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE admin(
   admin_id INT NOT NULL AUTO_INCREMENT,
   username VARCHAR(20) NOT NULL,
   password VARCHAR(255) NOT NULL,
   salt VARCHAR(50)  NOT NULL,
   secret VARCHAR(255),
   PRIMARY KEY(admin_id)
);

CREATE TABLE researcher(
   researcher_id  INT NOT NULL  AUTO_INCREMENT,
   researcher_username VARCHAR(255) NOT NULL UNIQUE,
   password VARCHAR(255)  NOT NULL,
   firstname  VARCHAR (20) ,
   lastname  VARCHAR (20) ,
   nric  CHAR (10),
   dob  DATE ,
   gender  ENUM('M', 'F') ,
   phone1  VARCHAR (20) ,
   phone2  VARCHAR (20),
   address1 VARCHAR(255),
   address2 VARCHAR(255),
   zipcode1 INT,
   zipcode2 INT,
   qualification VARCHAR(255),
   qualification_name VARCHAR(255),
   isAdmin INT,
   PRIMARY KEY (researcher_id)
);

CREATE TABLE category(
   category_id INT NOT NULL AUTO_INCREMENT,
   category_name VARCHAR(255) NOT NULL,
   PRIMARY KEY (category_id)
);

CREATE TABLE researcher_category(
   approval_id INT NOT NULL AUTO_INCREMENT,
   researcher_id INT NOT NULL,
   category_id INT NOT NULL,
   approval_status ENUM('Approved', 'Pending', 'Not Approved') NOT NULL,
   PRIMARY KEY (approval_id),
	FOREIGN KEY (researcher_id) REFERENCES researcher(researcher_id) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (category_id) REFERENCES category(category_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE condition_category(
   approval_id INT NOT NULL AUTO_INCREMENT,
   condition_id INT NOT NULL,
   category_id INT NOT NULL,
   PRIMARY KEY (approval_id),
   FOREIGN KEY (condition_id) REFERENCES `condition`(condition_id) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY (category_id) REFERENCES category(category_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE one_time_link(
   token VARCHAR(255),
   uid INT NOT NULL,
   filepath VARCHAR(100) NOT NULL,
   csrf VARCHAR(255),
   datatype VARCHAR(50) NOT NULL,
   PRIMARY KEY (token),
   FOREIGN KEY (uid) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE csrf(
   csrf_token VARCHAR(255) NOT NULL,
   uid INT NOT NULL,
   expiry int NOT NULL,
   PRIMARY KEY (csrf_token),
   FOREIGN KEY (uid) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE user_challenge(
  uid INT NOT NULL,
  challengeString VARCHAR(300) NOT NULL,
  PRIMARY KEY(uid),
  FOREIGN KEY (uid) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE user_metadata(
  uid INT NOT NULL,
  lockAttempts INT NOT NULL DEFAULT 0,
  PRIMARY KEY(uid),
  FOREIGN KEY (uid) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE
);
