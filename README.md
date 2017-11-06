# CS3205 Server 4 (Database Server)

## Overview
**Web Server:** Apache Tomcat

**Programming Language:** Java

**Framework:** Jersey Framework for RESTful Web Application

Web Application is deployed on https://cs3205-4-i.comp.nus.edu.sg/api.

## Architecture

[[https://github.com/ngzikai/cs3205-db-main/blob/master/doc/ServerDiagram.PNG|alt=octocat]]

Server 4 db server is using a RESTful API architecture to serve the queries for the database. This RESTful API is the endpoint for any interaction to the database, and is splitted into Team 1, Team 2 and Team 3's endpoints respectively. Each provides a separate endpoint for the various systems' server to be integrated with, while residing on the same storage system. This creates the integration of a one full solution, designed to cater to the different needs of each system. 

The storage system involves the database server, MySQL, to store most of the data needed, and the file storage system for storing data that comes as a file. This combination of the various type of data handling is well-abstracted by the system developed by Team 1, 2 and 3 to serve as the single storage component for the main architecture of the entire project. All the various different data entities are designed to serve the usage of the 3 systems effectively and efficiently while integrating the different functionalities into the core solution.  

## Security Features
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

## List of Available Endpoints
## Team 1: 
### User Service
**1.** /api/team1/user/<br/>
**2.** /api/team1/user/username/{username}<br/>
**3.** /api/team1/user/uid/{uid}<br/>
**4.** /api/team1/user/uid/public/{uid}<br/>
**5.** /api/team1/user/therapists<br/>
**6.**  /api/team1/user/create/{username}/{password}/{salt}/{fname}/{lname}/{nric}/{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}<br/>
**7.** /api/team1/user/create/ **(Accepts POST request JSON data)**<br/>
**8.**/api/team1/user/update/{uid}/{user}/{password}/{salt}/{fname}
/{lname}/{nric}/{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}<br/>
**9.** /api/team1/user/update/ **(Accepts POST request JSON data)**<br/>
**10.** /api/team1/user/update/{username}/{password}/{salt}<br/>
**11.** /api/team1/user/update/password **(Accepts POST request JSON data)**<br/>
**12.** /api/team1/user/delete/{uid}<br/>
**13.** /api/team1/user/secret/set/{uid}/{secret}<br/>
**14.** /api/team1/user/secret/{uid}<br/>

### Admin Service
**15.** /api/team1/admin/<br/>
**16.** /api/team1/admin/{username}<br/>
**17.** /api/team1/admin/secret/set/{adminid}/{secret}<br/>
**18.** /api/team1/admin/secret/{adminid}<br/>

### Treatment Service
**19.** /api/team1/treatment<br/>
**20.** /api/team1/treatment/{treatment_id}<br/>
**21.** /api/team1/treatment/patient/{patientid}/{status}<br/>
**22.** /api/team1/treatment/therapist/{therapistid}/{status}<br/>
**23.** /api/team1/treatment/create/{patient}/{therapist}/{currentConsent}/{futureConsent}<br/>
**24.** /api/team1/treatment/update/{id}<br/>
**25.** /api/team1/treatment/delete/{id}<br/>
**26.** /api/team1/treatment/update/consentsetting **(Accepts POST request JSON data)**<br/>

### OTL Service
**27.** /api/team1/otl<br/>
**28.** /api/team1/otl/{token}<br/>
**29.** /api/team1/otl/create/{token}/{uid}/{filepath}/{csrf}/{dataType} **(Accepts POST request JSON data)**<br/>
**30.** /api/team1/otl/update/{token}/{csrf}<br/>
**31.** /api/team1/otl/delete/{token}<br/>

### CSRF Service
**32.** /api/team1/csrf<br/>
**33.** /api/team1/csrf/{csrfToken}<br/>
**34.** /api/team1/csrf/create/{csrfToken}/{uid}/{expiry}/{description}<br/>
**35.** /api/team1/csrf/delete/{csrfToken}<br/>

### Record Service
**36.** /api/team1/record/all/{uid}<br/>
**37.** /api/team1/record/get/{rid}<br/>
**38.** /api/team1/record/{rid}<br/>
**39.** /api/team1/record **(Accepts POST request JSON data)**<br/>
**40.** /api/team1/record/document/create **(Accepts POST request JSON data)**<br/>
**41.** /api/team1/record/delete/{rid}/{uid}<br/>
**42.** /api/team1/record/alldocuments/{uid}<br/>

### Consent Service
**43.** /api/team1/consent<br/>
**44.** /api/team1/consent/{consent_id}<br/>
**45.** /api/team1/consent/create/{uid}/{rid}<br/>
**46.** /api/team1/consent/update/{consent_id}<br/>
**47.** /api/team1/consent/delete/{consent_id}<br/>
**48.** /api/team1/consent/user/{uid}<br/>
**49.** /api/team1/consent/user/{uid}/{status}<br/>
**50.** /api/team1/consent/record/{rid}<br/>
**51.** /api/team1/consent/owner/{ownerid}/{therapistid}<br/>
**52.** /api/team1/consent/check/{uid}/{rid}<br/>

### Log Service
**53.** /api/team1/log/<br/>
**54.** /api/team1/log/create **(Accepts POST request JSON data)**<br/>
 
## Team 2:
### Category Service
**GET:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/info <br/>
**Consumes:** - <br/>
**Produces:** JSON list of all categories {category_id, category_name}<br/>

**GET:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/list<br/>
**Consumes:** -<br/>
**Produces:** JSON list of all categories {category_id, category_name, conditions[condition_id, condition_name]}<br/>

**GET:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/{category_id}<br/>
**Consumes:** -<br/>
**Produces:** JSON list of all conditions in category {condition_id, condition_name}<br/>

**POST:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/request<br/>
**Consumes:** JSON {researcher_id, category_id}<br/>
**Produces:** "Success" or "Failed" depending on request success<br/>

**POST:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/approve<br/>
**Consumes:** JSON {researcher_id, category_id}<br/>
**Produces:** "Success" or "Failed" depending on status change success<br/>

**POST:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/decline<br/>
**Consumes:** JSON {researcher_id, category_id}<br/>
**Produces:** "Success" or "Failed" depending on status change success<br/>

**GET:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/list/{researcher_id}<br/>
**Consumes:** -<br/>
**Produces:** JSON {researcher_id, categories[{category_id, category_name, status}]}<br/>

**GET:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/category/list/all<br/>
**Consumes:** -<br/>
**Produces:** JSON [{researcher_id, categories[{category_id, category_name, status}]}]<br/>

### Ethnicity Service
*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/ethnicity<br/>
**Consumes:** -<br/>
**Produces:** JSON list of all ethnicity [{result}]<br/>

### Filter Service
*GET:* http://cs3205-4-i.comp.nus.edu.sg/api/team2/filters<br/>
**Consumes:** -<br/>
**Produces:** JSON list of all filters [{id, parent_id, key, value, isset, type, children[{id, parent_id, key, value, isset, type, children}]}]<br/>

### Heartrate Service
**POST:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/heart<br/>
**Consumes:** JSON {uid}<br/>
**Produces:** JSON [week{sunday[{heartrate}], monday[{heartrate}], tuesday[{heartrate}], wednesday[{heartrate}], thursday[{heartrate}], friday[{heartrate}], saturday[{heartrate}]}]<br/>

### Nationality Service
**POST:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/nationality<br/>
**Consumes**: -<br/>
**Produces:** JSON list of all nationality [{result}]<br/>

### Researcher Service
**POST:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/login<br/>
**Consumes:** JSON {researcher_username}<br/>
**Produces:** Password Hash<br/>

**POST:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/register<br/>
**Consumes:** JSON {firstname, lastname, researcher_username, password}<br/>
**Produces:** "Success" or "Failed" depending on registration success<br/>

**GET:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/{researcher_username}<br/>
**Consumes:** -<br/>
**Produces:** JSON Object of researcher {researcher_id, researcher_username, password, firstname, lastname, nric, dob, gender, phone1, phone2, address1, address2, zipcode1, zipcode2, qualification, qualification_name, isAdmin, otpsecret, research_category{category}}<br/>

**PUT:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/edit<br/>
**Consumes:** JSON {researcher_username, password, firstname, lastname, nric, dob, gender, address1, address2, phone1, phone2, zipcode1, zipcode2, qualification, qualification_name}<br/>
**Produces:** "Success" or "Failed" depending on update success<br/>

**DELETE:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/{researcher_id}<br/>
**Consumes:** -<br/>
**Produces:** "Success" or "Failed" depending on deletion success<br/>

**PUT:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/registerOTP<br/>
**Consumes:** {researcher_username, otpsecret}<br/>
**Produces:** "Success" or "Failed" depending on registration success<br/>

**PUT:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/passwordChange<br/>
**Consumes:** {researcher_username, password}<br/>
**Produces:** "Success" or "Failed" depending on edit success<br/>

**GET:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/OTPenabled/{researcher_username}<br/>
**Consumes:** -<br/>
**Produces:** "true" or "false" depending on query<br/>

**DELETE:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/researcher/OTPdelete/{researcher_username}<br/>
**Consumes:** -<br/>
**Produces:** "Success" or "Failed" depending on deletion success<br/>

### Search Service
**POST:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/search<br/>
**Consumes:** {ageRange[age], gender[gender], bloodType[bloodType], zipcode[zipcode], cid[condition_id], ethnicity[ethnicity], nationality[nationality], drug_allergy[drug_allergy]}<br/>
**Produces:** {dob, gender, zipcode1, zipcode2, bloodtype, condition_name, ethnicity, nationality, drug_allergy, timeseries_path, heartrate_path}<br/>

### Timeseries Service
**POST:** http://cs3205-4-i.comp.nus.edu.sg/api/team2/timeseries<br/>
**Consumes:** JSON {uid}<br/>
**Produces:** JSON {timeseries_data} (FROM FILE)<br/>


### Team 3:

**1.** /api/team3/step/{userID}<br/>
**2.** /api/team3/heart/{userID}<br/>
**3.** /api/team3/image/{userID}<br/>
**4.** /api/team3/video/{userID}<br/>
*prefix + /get/{objectID}*<br/>
*prefix + /get/{objectID}/meta*<br/>
*prefix + /all*<br/>
*prefix + /upload/{timestamp}*<br/>


