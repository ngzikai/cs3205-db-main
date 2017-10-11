<html>
<body>
    <h2>CS3205 Database RESTful Web Application!</h2>
    <p><a href="webapi/myresource">Jersey resource</a>
    <p>Visit <a href="http://jersey.java.net">Project Jersey website</a>
    for more information on Jersey!<br>
    <br>
    Team 1 API:<br>
    - /api/team1/user/<br>
    - /api/team1/user/username/{username}<br>
    - /api/team1/user/uid/{uid}<br>
    - /api/team1/user/therapists<br>
    - /api/team1/user/create/{username}/{password}/{fname}/{lname}/{nric}/{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}<br>
    - /api/team1/user/update/{uid}/{user}/{password}/{fname}/{lname}/{nric}/{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}<br>
    - /api/team1/user/delete/{uid}<br>
    - /api/team1/user/secret/set/{uid}/{secret}<br>
    - /api/team1/user/secret/{uid}<br>
    - /api/team1/admin/<br>
    - /api/team1/admin/{username}<br>
    - /api/team1/treatment<br>
    - /api/team1/treatment/{treatment_id}<br>
    - /api/team1/treatment/create/{patient}/{therapist}<br>
    - /api/team1/treatment/update/{id}<br>
    - /api/team1/treatment/delete/{id}<br>

    <br>
    Team 3 API:<br>
    - /api/team3/step/{userID}<br>
    - /api/team3/heart/{userID}<br>
    - /api/team3/image/{userID}<br>
    - /api/team3/video/{userID}<br>
    - prefix + /get/{objectID}<br>
    - prefix + /all<br>
    - prefix + /upload/{timestamp}<br>

</body>
</html>
