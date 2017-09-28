package restapi.team1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import controller.UserController;

@Path("/team1/user")
public class UserService {
	UserController uc = new UserController();
	
	@GET
	@Produces("application/json")
	public Response getAllUser() throws JSONException {

		JSONObject jsonObject = uc.getAllUser();

		//String result = "@Produces(\"application/json\") List of Users \n\n" + jsonObject;
		return createResponse(jsonObject);
	}

	@Path("{s}")
	@GET
	@Produces("application/json")
	public Response getUserFromUsername(@PathParam("s") String user) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		if(!validateString(user)) {
			jsonObject.put("result", false); 
			//String result = "@Produces(\"application/json\") Incorrect username";
			return createResponse(jsonObject);
		}

		
		jsonObject = uc.getUser(user); 

		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false); 
			//String result = "@Produces(\"application/json\") Incorrect username";
			return createResponse(jsonObject);
		}
		
		//String result = "@Produces(\"application/json\") User details\n\n" + jsonObject;
		return createResponse(jsonObject);
	}
	
	@Path("/create/{user}/{password}/{fname}/{lname}/{nric}/"
			+ "{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}"
			+ "/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}")
	@GET
	@Produces("application/json")
	public Response createUser(@PathParam("user") String user, @PathParam("password") String password,
			@PathParam("fname") String fname, @PathParam("lname") String lname, @PathParam("nric") String nric, 
			@PathParam("dob") String dob, @PathParam("gender") String gender,
			@PathParam("phone1") String phone1, @PathParam("phone2") String phone2, @PathParam("phone3") String phone3,
			@PathParam("addr1") String addr1, @PathParam("addr2") String addr2, @PathParam("addr3") String addr3,
			@PathParam("zip1") int zip1, @PathParam("zip2") int zip2, @PathParam("zip3") int zip3,
			@PathParam("qualify") int qualify, @PathParam("bloodtype") String bloodType, @PathParam("nfcid") String nfcid
			) throws JSONException {

		JSONObject jsonObject = uc.createUser(user, password, fname, lname, nric, dob, gender.charAt(0), phone1, phone2, phone3, 
				addr1, addr2, addr3, zip1, zip2, zip3, qualify, bloodType, nfcid);

		//String result = "@Produces(\"application/json\") Creating user .... \n\n" + jsonObject;
		return createResponse(jsonObject);
	}
	
	@Path("/update/{uid}/{user}/{password}/{fname}/{lname}/{nric}/"
			+ "{dob}/{gender}/{phone1}/{phone2}/{phone3}/{addr1}/{addr2}/{addr3}"
			+ "/{zip1}/{zip2}/{zip3}/{qualify}/{bloodtype}/{nfcid}")
	@GET
	@Produces("application/json")
	public Response updateUser(@PathParam("uid") String uid, @PathParam("user") String user, @PathParam("password") String password,
			@PathParam("fname") String fname, @PathParam("lname") String lname, @PathParam("nric") String nric, 
			@PathParam("dob") String dob, @PathParam("gender") String gender,
			@PathParam("phone1") String phone1, @PathParam("phone2") String phone2, @PathParam("phone3") String phone3,
			@PathParam("addr1") String addr1, @PathParam("addr2") String addr2, @PathParam("addr3") String addr3,
			@PathParam("zip1") int zip1, @PathParam("zip2") int zip2, @PathParam("zip3") int zip3,
			@PathParam("qualify") int qualify, @PathParam("bloodtype") String bloodType, @PathParam("nfcid") String nfcid
			) throws JSONException {

		JSONObject jsonObject = uc.updateUser(uid, user, password, fname, lname, nric, dob, gender.charAt(0), phone1, phone2, phone3, 
				addr1, addr2, addr3, zip1, zip2, zip3, qualify, bloodType, nfcid);

		//String result = "@Produces(\"application/json\") Creating user .... \n\n" + jsonObject;
		return createResponse(jsonObject);
	}
	
	@Path("/delete/{uid}")
	@GET
	@Produces("application/json")
	public Response deleteUser(@PathParam("uid") int uid) throws JSONException {

		JSONObject jsonObject = uc.deleteUser(uid);

		//String result = "@Produces(\"application/json\") Delete user "+ uid +"\n\n" + jsonObject;
		return createResponse(jsonObject);
	}
	
	public Response createResponse(JSONObject jsonObject) {
		return Response.status(200).entity(jsonObject.toString())
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
	}

	private boolean validateString(String username) {
		return StringUtils.isAlphanumeric(username);
	}
}
