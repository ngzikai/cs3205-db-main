# CS3205 Server 4 (Database Server)

## Overview
Web Server: Apache Tomcat

Programming Language: Java

Framework: Jersey Framework for RESTful Web Application

Web Application is deployed on https://cs3205-4-i.comp.nus.edu.sg/api.

## Architecture
Server 4 db server is using a RESTful API architecture to serve the queries for the database. This RESTful API is the endpoint for any interaction to the database, and is splitted into Team 1, Team 2 and Team 3's endpoints respectively. Each provides a separate endpoint for the various systems' server to be integrated with, while residing on the same storage system. This creates the integration of a one full solution, designed to cater to the different needs of each system. 

The storage system involves the database server, MySQL, to store most of the data needed, and the file storage system for storing data that comes as a file. This combination of the various type of data handling is well-abstracted by the system developed by Team 1, 2 and 3 to serve as the single storage component for the main architecture of the entire project. All the various different data entities are designed to serve the usage of the 3 systems effectively and efficiently while integrating the different functionalities into the core solution.  

## List of Available Endpoints
### Team 1: 
#### User Service
1. /api/team1/user/
2. /api/team1/user/username/{username}
3. /api/team1/user/uid/{uid}
4. /api/team1/user/uid/public/{uid}
5. /api/team1/user/therapists
6.  /api/team1/user/create/{username}/{password}/{salt}/{fname}/{lname}/{nric}/{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}
7. /api/team1/user/create/ **(Accepts POST request JSON data)**
8. /api/team1/user/update/{uid}/{user}/{password}/{salt}/{fname}/{lname}/{nric}/{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}
9. /api/team1/user/update/ **(Accepts POST request JSON data)**
10. /api/team1/user/update/{username}/{password}/{salt}
11. /api/team1/user/update/password **(Accepts POST request JSON data)**
12. /api/team1/user/delete/{uid}
13. /api/team1/user/secret/set/{uid}/{secret}
14. /api/team1/user/secret/{uid}

#### Admin Service
15. /api/team1/admin/
16. /api/team1/admin/{username}
17. /api/team1/admin/secret/set/{adminid}/{secret}
18. /api/team1/admin/secret/{adminid}

#### Treatment Service
19. /api/team1/treatment
20. /api/team1/treatment/{treatment_id}
21. /api/team1/treatment/patient/{patientid}/{status}
22. /api/team1/treatment/therapist/{therapistid}/{status}
23. /api/team1/treatment/create/{patient}/{therapist}/{currentConsent}/{futureConsent}
24. /api/team1/treatment/update/{id}
25. /api/team1/treatment/delete/{id}
26. /api/team1/treatment/update/consentsetting **(Accepts POST request JSON data)**

#### OTL Service
27. /api/team1/otl
28. /api/team1/otl/{token}
29. /api/team1/otl/create/{token}/{uid}/{filepath}/{csrf}/{dataType} **(Accepts POST request JSON data)**
30. /api/team1/otl/update/{token}/{csrf}
31. /api/team1/otl/delete/{token}

#### CSRF Service
32. /api/team1/csrf
33. /api/team1/csrf/{csrfToken}
34. /api/team1/csrf/create/{csrfToken}/{uid}/{expiry}/{description}
35. /api/team1/csrf/delete/{csrfToken}

#### Record Service
36. /api/team1/record/all/{uid}
37. /api/team1/record/get/{rid}
38. /api/team1/record/{rid}
39. /api/team1/record **(Accepts POST request JSON data)**
40. /api/team1/record/document/create **(Accepts POST request JSON data)**
41. /api/team1/record/delete/{rid}/{uid}
42. /api/team1/record/alldocuments/{uid}

#### Consent Service
43. /api/team1/consent
44. /api/team1/consent/{consent_id}
45. /api/team1/consent/create/{uid}/{rid}
46. /api/team1/consent/update/{consent_id}
47. /api/team1/consent/delete/{consent_id}
48. /api/team1/consent/user/{uid}
49. /api/team1/consent/user/{uid}/{status}
50. /api/team1/consent/record/{rid}
51. /api/team1/consent/owner/{ownerid}/{therapistid}
52. /api/team1/consent/check/{uid}/{rid}

#### Log Service
53. /api/team1/log/
54. /api/team1/log/create **(Accepts POST request JSON data)**
 
### Team 2:

#### Category Service
*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/info
**Consumes:** - 
**Produces:** JSON list of all categories {category_id, category_name}

*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/list
**Consumes:** -
**Produces:** JSON list of all categories {category_id, category_name, conditions[condition_id, condition_name]}

*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/{category_id}
**Consumes:** -
**Produces:** JSON list of all conditions in category {condition_id, condition_name}

*POST:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/request
**Consumes:** JSON {researcher_id, category_id}
**Produces:** "Success" or "Failed" depending on request success

*POST:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/approve
**Consumes:** JSON {researcher_id, category_id}
**Produces:** "Success" or "Failed" depending on status change success

*POST:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/decline
**Consumes:** JSON {researcher_id, category_id}
**Produces:** "Success" or "Failed" depending on status change success

*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/list/{researcher_id}
**Consumes:** -
**Produces:** JSON {researcher_id, categories[{category_id, category_name, status}]}

*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/list/all
**Consumes:** -
**Produces:** JSON [{researcher_id, categories[{category_id, category_name, status}]}]

#### Ethnicity Service
*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/ethnicity
**Consumes:** -
**Produces:** JSON list of all ethnicity [{result}]

#### Filter Service
*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/filters
**Consumes:** -
**Produces:** JSON list of all filters [{id, parent_id, key, value, isset, type, children[{id, parent_id, key, value, isset, type, children}]}]

#### Heartrate Service
*POST:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/heart
**Consumes:** JSON {uid}
**Produces:** JSON [week{sunday[{heartrate}], monday[{heartrate}], tuesday[{heartrate}], wednesday[{heartrate}], thursday[{heartrate}], friday[{heartrate}], saturday[{heartrate}]}]

#### Nationality Service
*POST:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/nationality
**Consumes**: -
**Produces:** JSON list of all nationality [{result}]

#### Researcher Service
*POST:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/login
**Consumes:** JSON {researcher_username}
**Produces:** Password Hash

*POST:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/register
**Consumes:** JSON {firstname, lastname, researcher_username, password}
**Produces:** "Success" or "Failed" depending on registration success

*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/{researcher_username}
**Consumes:** -
**Produces:** JSON Object of researcher {researcher_id, researcher_username, password, firstname, lastname, nric, dob, gender, phone1, phone2, address1, address2, zipcode1, zipcode2, qualification, qualification_name, isAdmin, otpsecret, research_category{category}}

*PUT:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/edit
**Consumes:** JSON {researcher_username, password, firstname, lastname, nric, dob, gender, address1, address2, phone1, phone2, zipcode1, zipcode2, qualification, qualification_name}
**Produces:** "Success" or "Failed" depending on update success

*DELETE:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/{researcher_id}
**Consumes:** -
**Produces:** "Success" or "Failed" depending on deletion success

*PUT:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/registerOTP
**Consumes:** {researcher_username, otpsecret}
**Produces:** "Success" or "Failed" depending on registration success

*PUT:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/passwordChange
**Consumes:** {researcher_username, password}
**Produces:** "Success" or "Failed" depending on edit success

*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/OTPenabled/{researcher_username}
**Consumes:** -
**Produces:** "true" or "false" depending on query

*DELETE:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/OTPdelete/{researcher_username}
**Consumes:** -
**Produces:** "Success" or "Failed" depending on deletion success

#### Search Service
*POST:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/search
**Consumes:** {ageRange[age], gender[gender], bloodType[bloodType], zipcode[zipcode], cid[condition_id], ethnicity[ethnicity], nationality[nationality], drug_allergy[drug_allergy]}
**Produces:** {dob, gender, zipcode1, zipcode2, bloodtype, condition_name, ethnicity, nationality, drug_allergy, timeseries_path, heartrate_path}

#### Timeseries Service
*POST:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/timeseries
**Consumes:** JSON {uid}
**Produces:** JSON {timeseries_data} (FROM FILE)


### Team 3:

1. /api/team3/step/{userID}
2. /api/team3/heart/{userID}
3. /api/team3/image/{userID}
4. /api/team3/video/{userID}
*prefix + /get/{objectID}*
*prefix + /get/{objectID}/meta*
*prefix + /all*
*prefix + /upload/{timestamp}*


## Security
### TLS/SSL
In order to create a secure communication channel between the front service servers and the database server, we established a two-way SSL authentication protocol using digital certificates signed by server 4. This allows authentication and authorization of the usage of the API provided by server 4, as well as encrypting the communication channel up to ensure confidentiality of information. 

### HTTP BASIC AUTH
On top of that, basic authentication is also used to verify that only authorized users are to use the specific endpoints.This feature essentially allows for role-based access control of the endpoints across the various teams. While every team's endpoints are deployed on the same server, only authorized users (i.e team members of the various endpoints) are allowed into their APIs. 

### File Encryption
AES-256 Encryption is also implemented on the files that are stored on the server. As our server may potentially hold a bulk of sensitive information, it is important that the infomation is kept secure even in the unlikely event of a compromise. The encryption algorithm is designed in such a way that the files can only be decrypted through the use of the system in normal, authorized proceedure. 


## Server Deployment
### Setup
Ensure that the username and password in `restAPI/conf/settings.xml` is inside tomcat's `tomcat-users.xml` with the `manager-script` role

```
<role rolename="manager-script">
<user username="<name>" password="<password>" roles="manager-script" />
```
### Deployment
After editing sources, to auto deploy to tomcat server, run the command
```
mvn tomcat7:redeploy
```
***
