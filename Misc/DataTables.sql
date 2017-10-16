FLUSH PRIVILEGES;
GRANT ALL PRIVILEGES ON CS3205.* TO 'holyman'@'%' IDENTIFIED BY 'dude' WITH GRANT OPTION;
use CS3205;

CREATE TABLE user(
   uid  INT NOT NULL  AUTO_INCREMENT,
   username VARCHAR(20) NOT NULL UNIQUE,
   password VARCHAR(255)  NOT NULL,
   salt   VARCHAR(255) NOT NULL,
   firstname  VARCHAR (20)  NOT NULL,
   lastname  VARCHAR (20)  NOT NULL,
   nric  CHAR (10)  NOT NULL,
   dob  DATE  NOT NULL,
   gender  ENUM('M', 'F')  NOT NULL,
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
   PRIMARY KEY (uid)
);

CREATE TABLE file(
   uid VARCHAR(22) NOT NULL UNIQUE,
   createdDate BIGINT NOT NULL,
   location VARCHAR(100) NOT NULL,
   ownerID int NOT NULL,
   type VARCHAR(20) NOT NULL,
   PRIMARY KEY (uid),
   FOREIGN KEY (ownerID) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE step(
   uid VARCHAR(22) NOT NULL UNIQUE,
   createdDate BIGINT NOT NULL,
   location VARCHAR(100) NOT NULL,
   ownerID int NOT NULL,
   PRIMARY KEY (uid),
   FOREIGN KEY (ownerID) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE heartrate(
   uid VARCHAR(22) NOT NULL UNIQUE,
   createdDate BIGINT NOT NULL,
   location VARCHAR(100) NOT NULL,
   ownerID int NOT NULL,
   PRIMARY KEY (uid),
   FOREIGN KEY (ownerID) REFERENCES user(uid) ON UPDATE CASCADE ON DELETE CASCADE
);


/*        POPULATE DATA        */
/*        STEPS        */
-- INSERT INTO user VALUES (default, "Bob99", "hello123", "saltysalt", "Bobby", "Mike", "S1234567Z", DATE '2000-12-01',
-- 'M', 98989898, 97979797, 96969696, "Kent Ridge", "PGP", "Sentosa Cove", 555555, 544444, 533333, 0, "B+", "123"
-- );
-- INSERT INTO step VALUES("Hd4nYvis8StSVLzqfrh7Xt", 1506441101, "Hd4nYvis8StSVLzqfrh7Xt.json", 1);
-- INSERT INTO step VALUES("JWTEwvTK4h5o5ghWVPphfD", 1506441101, "JWTEwvTK4h5o5ghWVPphfD.json", 1);
-- INSERT INTO step VALUES("SJjhuxkfcKHnFCY7tPeTyZ", 1506441101, "SJjhuxkfcKHnFCY7tPeTyZ.json", 1);
-- INSERT INTO step VALUES("4NSBs8JpZf94L7RVJTL9HZ", 1506441101, "4NSBs8JpZf94L7RVJTL9HZ.json", 1);
-- INSERT INTO step VALUES("9vKu53qNngoow6CGNPsi8M", 1506441101, "9vKu53qNngoow6CGNPsi8M.json", 1);
