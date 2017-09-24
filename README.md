# cs3205-db-main

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
mvn tomcat7:redeploy -s conf/settings.xml
```
***
