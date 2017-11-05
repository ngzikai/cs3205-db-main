<html>
<body>
    <h2>CS3205 Database RESTful Web Application!</h2>
    <p><a href="webapi/myresource">Jersey resource</a>
    <p>Visit <a href="http://jersey.java.net">Project Jersey website</a>
    for more information on Jersey!<br>
    <br>
    <h3>Team 1 API:</h3>
    - /api/team1/user/<br>
    - /api/team1/user/username/{username}<br>
    - /api/team1/user/uid/{uid}<br>
    - /api/team1/user/uid/public/{uid}<br>
    - /api/team1/user/therapists<br>
    - /api/team1/user/create/{username}/{password}/{salt}/{fname}/{lname}/{nric}/{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}<br>
    - /api/team1/user/create/<br>
    	&emsp;- Accepts POST request JSON data <br>
    - /api/team1/user/update/{uid}/{user}/{password}/{salt}/{fname}/{lname}/{nric}/{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}<br>
    - /api/team1/user/update/<br>
    	&emsp;- Accepts POST request JSON data <br>
    - /api/team1/user/update/{username}/{password}/{salt}<br>
    - /api/team1/user/update/password<br>
    	&emsp;- Accepts POST request JSON data <br>
    - /api/team1/user/delete/{uid}<br>
    - /api/team1/user/secret/set/{uid}/{secret}<br>
    - /api/team1/user/secret/{uid}<br><br>
    - /api/team1/admin/<br>
    - /api/team1/admin/{username}<br>
    - /api/team1/admin/secret/set/{adminid}/{secret}<br>
    - /api/team1/admin/secret/{adminid}<br><br>
    - /api/team1/treatment<br>
    - /api/team1/treatment/{treatment_id}<br>
    - /api/team1/treatment/patient/{patientid}/{status}<br>
    - /api/team1/treatment/therapist/{therapistid}/{status}<br>
    - /api/team1/treatment/create/{patient}/{therapist}/{currentConsent}/{futureConsent}<br>
    - /api/team1/treatment/update/{id}<br>
    - /api/team1/treatment/delete/{id}<br><br>
    - /api/team1/treatment/update/consentsetting<br>
    &emsp;- Accepts POST request JSON data <br>
    - /api/team1/otl<br>
	- /api/team1/otl/{token}<br>
	- /api/team1/otl/create/{token}/{uid}/{filepath}/{csrf}/{dataType}<br>
		&emsp;- Accepts POST request JSON data <br>
	- /api/team1/otl/update/{token}/{csrf}<br>
	- /api/team1/otl/delete/{token}<br><br>
    - /api/team1/csrf<br>
	- /api/team1/csrf/{csrfToken}<br>
	- /api/team1/csrf/create/{csrfToken}/{uid}/{expiry}/{description}<br>
	- /api/team1/csrf/delete/{csrfToken}<br><br>
	- /api/team1/record/all/{uid}<br>
	- /api/team1/record/get/{rid}<br>
	- /api/team1/record/{rid}<br>
	- /api/team1/record<br>
		&emsp;- Accepts POST request JSON data <br>
	- /api/team1/record/document/create<br>
		&emsp;- Accepts POST request JSON data <br>
	- /api/team1/record/delete/{rid}/{uid}<br>
	- /api/team1/record/alldocuments/{uid}<br><br>
	- /api/team1/consent<br>
    - /api/team1/consent/{consent_id}<br>
    - /api/team1/consent/create/{uid}/{rid}<br>
    - /api/team1/consent/update/{consent_id}<br>
    - /api/team1/consent/delete/{consent_id}<br>
    - /api/team1/consent/user/{uid}<br>
    - /api/team1/consent/user/{uid}/{status}<br>
    - /api/team1/consent/record/{rid}<br>
    - /api/team1/consent/owner/{ownerid}/{therapistid}<br>
    - /api/team1/consent/check/{uid}/{rid}<br><br>
    - /api/team1/log/<br>
    - /api/team1/log/create<br>
    	&emsp;- Accepts POST request JSON data <br><br>


    <br>
    <h3>Team 3 API:</h3>
    - /api/team3/step/{userID}<br>
    - /api/team3/heart/{userID}<br>
    - /api/team3/image/{userID}<br>
    - /api/team3/video/{userID}<br>
    - prefix + /get/{objectID}<br>
    - prefix + /get/{objectID}/meta<br>
    - prefix + /all<br>
    - prefix + /upload/{timestamp}<br>

</body>
</html>
