-- Create filters table
CREATE TABLE filters (
    id INT PRIMARY KEY AUTO_INCREMENT
    ,parent_id INT
    ,`key` VARCHAR(64) NOT NULL
    ,`value` VARCHAR(64) NOT NULL
    ,isset INT NOT NULL
    ,type INT NOT NULL
);



-- Insert search filter categories (type = 1)
INSERT INTO filters(
    parent_id
    ,`key`
    ,`value`
    ,isset
    ,type
) 
VALUES
(null, "Age Group", "ageRange", 0, 1),
(null, "Blood Type", "bloodType", 0, 1),
(null, "Diagnosis Terms", "conditions", 0, 1),
(null, "Gender", "gender", 0, 1),
(null, "Zipcode", "zipcode", 0, 1);



-- Insert search filter category values (type = 3)
-- AGE GROUP
SELECT id INTO @parent_id FROM filters WHERE `key` = "Age Group" AND type = 1;
INSERT INTO filters(parent_id, `key`, `value`, isset, type) 
VALUES
(@parent_id, "Below 10", "0-10", 0, 3),
(@parent_id, "11 - 20", "10-20", 0, 3),
(@parent_id, "21 - 30", "20-30", 0, 3),
(@parent_id, "31 - 40", "30-40", 0, 3),
(@parent_id, "41 - 50", "40-50", 0, 3),
(@parent_id, "51 - 60", "50-60", 0, 3),
(@parent_id, "61 - 70", "60-70", 0, 3),
(@parent_id, "71 - 80", "70-80", 0, 3),
(@parent_id, "81 - 90", "80-90", 0, 3),
(@parent_id, "Above 90", "90-200", 0, 3);

-- BLOOD TYPE
SELECT id INTO @parent_id FROM filters WHERE `key` = "Blood Type" AND type = 1;
INSERT INTO filters(parent_id, `key`, `value`, isset, type) 
VALUES
(@parent_id, "AB+", "AB+", 0, 3),
(@parent_id, "AB-", "AB-", 0, 3),
(@parent_id, "AB+", "AB+", 0, 3),
(@parent_id, "AB-", "AB-", 0, 3),
(@parent_id, "AB+", "AB+", 0, 3),
(@parent_id, "AB-", "AB-", 0, 3),
(@parent_id, "AB+", "AB+", 0, 3),
(@parent_id, "AB-", "AB-", 0, 3);

-- CONDITION (CID / DIAGNOSIS TERMS)
SELECT id INTO @parent_id FROM filters WHERE `key` = "Diagnosis Terms" AND type = 1;
-- Not inserting any values at the moment

-- GENDER
SELECT id INTO @parent_id FROM filters WHERE `key` = "Gender" AND type = 3;
INSERT INTO filters(parent_id, `key`, `value`, isset, type) 
VALUES
(@parent_id, "Male", "M", 0, 3),
(@parent_id, "Female", "F", 0, 3);

-- ZIPCODE
SELECT id INTO @parent_id FROM filters WHERE `key` = "Diagnosis Terms" AND type = 1;
-- Not inserting any values at the moment



-- Insert return result categories (type = 2)
INSERT INTO filters(
    parent_id
    ,`key`
    ,`value`
    ,isset
    ,type
) 
VALUES
(null, "Blood Type", "bloodtype", 0, 2),
(null, "Condition(s)", "condition_name", 0, 2),
(null, "Age Group", "ageRange", 0, 2),
(null, "Gender", "gender", 0, 2),
(null, "ZipCode", "zipcode1", 0, 2);
