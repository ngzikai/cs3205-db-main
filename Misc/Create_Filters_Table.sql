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
(null, "Drug Allergy", "drug_allergy", 0, 1),
(null, "Ethnicity", "ethnicity", 0, 1),
(null, "Location", "zipcode", 0, 1),
(null, "Nationality", "nationality", 0, 1),
(null, "Sex", "gender", 0, 1);



-- Insert search filter category values (type = 3)
-- AGE GROUP
SELECT id INTO @parent_id FROM filters WHERE `key` = "Age Group" AND type = 1;
INSERT INTO filters(parent_id, `key`, `value`, isset, type)
VALUES
(@parent_id, "Below 10", "0-10", 0, 3),
(@parent_id, "11 - 20", "11-20", 0, 3),
(@parent_id, "21 - 30", "21-30", 0, 3),
(@parent_id, "31 - 40", "31-40", 0, 3),
(@parent_id, "41 - 50", "41-50", 0, 3),
(@parent_id, "51 - 60", "51-60", 0, 3),
(@parent_id, "61 - 70", "61-70", 0, 3),
(@parent_id, "71 - 80", "71-80", 0, 3),
(@parent_id, "81 - 90", "81-90", 0, 3),
(@parent_id, "Above 90", "91-200", 0, 3);

-- BLOOD TYPE
SELECT id INTO @parent_id FROM filters WHERE `key` = "Blood Type" AND type = 1;
INSERT INTO filters(parent_id, `key`, `value`, isset, type)
VALUES
(@parent_id, "AB+", "AB+", 0, 3),
(@parent_id, "AB-", "AB-", 0, 3),
(@parent_id, "A+", "A+", 0, 3),
(@parent_id, "A-", "A-", 0, 3),
(@parent_id, "B+", "B+", 0, 3),
(@parent_id, "B-", "B-", 0, 3),
(@parent_id, "O+", "O+", 0, 3),
(@parent_id, "O-", "O-", 0, 3);

-- CONDITION (CID / DIAGNOSIS TERMS)
SELECT id INTO @parent_id FROM filters WHERE `key` = "Diagnosis Terms" AND type = 1;
-- Not inserting any values at the moment

-- GENDER
SELECT id INTO @parent_id FROM filters WHERE `key` = "Sex" AND type = 1;
INSERT INTO filters(parent_id, `key`, `value`, isset, type)
VALUES
(@parent_id, "Male", "M", 0, 3),
(@parent_id, "Female", "F", 0, 3);

-- ZIPCODE
SELECT id INTO @parent_id FROM filters WHERE `key` = "Location" AND type = 1;
INSERT INTO filters(parent_id, `key`, `value`, isset, type)
VALUES
(@parent_id, "Raffles Place, Cecil, Marina, People's Park", "[1,2,3,4,5,6]", 0, 3),
(@parent_id, "Anson, Tanjong Pagar", "[7,8]", 0, 3),
(@parent_id, "Telok Blangah, Harbourfront", "[9,10]", 0, 3),
(@parent_id, "Pasir Panjang, Hong Leong Garden, Clementi New Town", "[11,12,13]", 0, 3),
(@parent_id, "Queenstown, Tiong Bahru", "[14,15,16]", 0, 3),
(@parent_id, "High Street, Beach Road", "[17]", 0, 3),
(@parent_id, "Middle Road, Golden Mile", "[18,19]", 0, 3),
(@parent_id, "Little India", "[20,21]", 0, 3),
(@parent_id, "Orchard, Cairnhill, River Valley", "[22,23]", 0, 3),
(@parent_id, "Ardmore, Bukit Timah, Holland Road, Tanglin", "[24,25,26,27]", 0, 3),
(@parent_id, "Watten Estate, Novena, Thomson", "[28,29,30]", 0, 3),
(@parent_id, "Balestier, Toa Payoh, Serangoon", "[31,32,33]", 0, 3),
(@parent_id, "Macpherson, Braddell", "[34,35,36,37]", 0, 3),
(@parent_id, "Geylang, Eunos", "[38,39,40,41]", 0, 3),
(@parent_id, "Katong, Joo Chiat, Amber Road", "[42,43,44,45]", 0, 3),
(@parent_id, "Bedok, Upper East Coast, Eastwood, Kew Drive", "[46,47,48]", 0, 3),
(@parent_id, "Loyang, Changi", "[49,50,81]", 0, 3),
(@parent_id, "Simei, Tampines, Pasir Ris", "[51,52]", 0, 3),
(@parent_id, "Serangoon Garden, Hougang, Punggol", "[53,54,55,82]", 0, 3),
(@parent_id, "Bishan, Ang Mo Kio", "[56,57]", 0, 3),
(@parent_id, "Upper Bukit Timah, Clementi Park, Ulu Pandan", "[58,59]", 0, 3),
(@parent_id, "Jurong, Tuas", "[60,61,62,63,64]", 0, 3),
(@parent_id, "Hillview, Dairy Farm, Bukit Panjang, Choa Chu Kang", "[65,66,67,68]", 0, 3),
(@parent_id, "Lim Chu Kang, Tengah", "[69,70,71]", 0, 3),
(@parent_id, "Kranji, Woodgrove, Woodlands", "[72.73]", 0, 3),
(@parent_id, "Yishun, Sembawang", "[75,76]", 0, 3),
(@parent_id, "Upper Thomson, Springleaf", "[77,78]", 0, 3),
(@parent_id, "Seletar", "[79,80]", 0, 3);

-- NATIONALITY
SELECT id INTO @parent_id FROM filters WHERE `key` = "Nationality" AND type = 1;
-- Not inserting any values at the moment

-- ETHNICITY
SELECT id INTO @parent_id FROM filters WHERE `key` = "Ethnicity" AND type = 1;
-- Not inserting any values at the moment

-- DRUG ALLERGY
SELECT id INTO @parent_id FROM filters WHERE `key` = "Drug Allergy" AND type = 1;
INSERT INTO filters(parent_id, `key`, `value`, isset, type)
VALUES
(@parent_id, "No Known Drug Allergies", "0", 0, 3),
(@parent_id, "Has Drug Allergies", "1", 0, 3);


-- Insert return result categories (type = 2)
INSERT INTO filters(
    parent_id
    ,`key`
    ,`value`
    ,isset
    ,type
)
VALUES
(null, "Age Group", "ageRange", 1, 2),
(null, "Blood Type", "bloodtype", 1, 2),
(null, "Condition(s)", "condition_name", 1, 2),
(null, "Drug Allergy", "drug_allergy", 1, 2),
(null, "Ethnicity", "ethnicity", 1, 2),
(null, "Heartrate Data", "heartrate_path", 1, 2),
(null, "Location", "zipcode1", 1, 2),
(null, "Nationality", "nationality", 1, 2),
(null, "Sex", "gender", 1, 2),
(null, "Time Series Data", "timeseries_path", 1, 2);
