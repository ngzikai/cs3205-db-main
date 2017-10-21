package restapi.team1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import controller.ConsentController;

@Path("/team1/consent")
public class ConsentService {
	private ConsentController cc = new ConsentController();

	@GET
	@Produces("application/json")
	public Response getAllConsent() throws JSONException {

		JSONObject jsonObject = cc.getAllConsent();

		return createResponse(jsonObject);
	}

	@Path("{s}")
	@GET
	@Produces("application/json")
	public Response getConsentWithId(@PathParam("s") int id) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		System.out.println("Retrieving details of Consent: " + id);
		jsonObject = cc.getConsentWithId(id);

		return createResponse(jsonObject);
	}
	
	@Path("/create/{uid}/{rid}")
	@GET
	@Produces("application/json")
	public Response createConsent(@PathParam("uid") int uid,
			@PathParam("rid") int rid) throws JSONException {
		JSONObject jsonObject = cc.createConsent(uid, rid);
		
		return createResponse(jsonObject);
	}

	public Response createResponse(JSONObject jsonObject) {
		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false); 
			jsonObject.put("msg", "Null object");
		}
		
		return Response.status(200).entity(jsonObject.toString())
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
	}
	
	@Path("/update/{id}")
	@GET
	@Produces("application/json")
	public Response updateConsent(@PathParam("id") int id) throws JSONException {

		JSONObject jsonObject = cc.updateConsent(id);

		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false);
			return createResponse(jsonObject);
		}
		return createResponse(jsonObject);
	}
	
	@Path("/delete/{id}")
	@GET
	@Produces("application/json")
	public Response deleteConsent(@PathParam("id") int id) throws JSONException {

		JSONObject jsonObject = cc.deleteConsent(id);

		return createResponse(jsonObject);
	}
	
	@Path("/user/{uid}/{status}")
	@GET
	@Produces("application/json")
	public Response getConsentWithPatientId(@PathParam("uid") int uid, @PathParam("status") boolean status) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		System.out.println("Retrieving all details of uid: " + uid + " with status : " + status);
		jsonObject = cc.getConsentWithUid(uid, status);

		return createResponse(jsonObject);
	}
	
}
