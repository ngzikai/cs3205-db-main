# cs3205-db-main

# Overview
Web Server: Apache Tomcat

Programming Language: Java

Framework: Jersey Framework for RESTful Web Application

Web Application is deployed on https://cs3205-4-i.comp.nus.edu.sg/api.

# Architecture
Server 4 db server is using a RESTful API architecture to serve the queries for the database. This RESTful API is the endpoint for any interaction to the database, and is splitted into Team 1, Team 2 and Team 3's endpoints respectively. Each provides a separate endpoint for the various systems' server to be integrated with, while residing on the same storage system. This creates the integration of a one full solution, designed to cater to the different needs of each system. 

The storage system involves the database server, MySQL, to store most of the data needed, and the file storage system for storing data that comes as a file. This combination of the various type of data handling is well-abstracted by the system developed by Team 1, 2 and 3 to serve as the single storage component for the main architecture of the entire project. All the various different data entities are designed to serve the usage of the 3 systems effectively and efficiently while integrating the different functionalities into the core solution.  

[List of endpoint APIs](restAPI/src/main/webapp/dunno_dun_tell.jsp)

#Security
In order to secure the communication channel between the front serving servers and the database server, we established a two-way SSL authentication protocol using digital certificates signed by server 4. This allows authentication and authorization of the usage of the API provided by server 4, as well as encrypting the communication channel up to ensure confidentiality of information. 

On top of that, basic authentication is also used to verify that only authorized users are to use the specific endpoints. 

AES-256 Encryption is also implemented on the files that are stored on the server, such that only through this application will the file be decrypted and used in the normal, authorized procedure. 


# Tomcat Server
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
