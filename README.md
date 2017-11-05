# cs3205-db-main

# Overview
Web Server: Apache Tomcat

Programming Language: Java

Framework: Jersey Framework for RESTful Web Application

Web Application is deployed on https://cs3205-4-i.comp.nus.edu.sg/api.


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
