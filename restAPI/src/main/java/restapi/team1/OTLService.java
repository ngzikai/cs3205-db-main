package restapi.team1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import controller.OTLController;

@Path("/team1/otl")
public class OTLService {
	private OTLController oc = new OTLController();

	@GET
	@Produces("application/json")
	public Response getAllOTL() throws JSONException {

		JSONObject jsonObject = oc.getAllOTL();
		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false); 
			return createResponse(jsonObject);
		}

		//String result = "@Produces(\"application/json\") List of Admins \n\n" + jsonObject;
		return createResponse(jsonObject);
	}

	@Path("{s}")
	@GET
	@Produces("application/json")
	public Response getOTLwithToken(@PathParam("s") String token) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject = oc.getOTLWithToken(token);

		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false);
			return createResponse(jsonObject);
		}
		return createResponse(jsonObject);
	}
	
	@Path("/create/{token}/{uid}/{filepath: .+}/{csrf}")
	@GET
	@Produces("application/json")
	public Response createOTL(@PathParam("token") String token,
			@PathParam("uid") int uid, @PathParam("filepath") String filepath, 
			@PathParam("csrf") String csrf) throws JSONException {
		JSONObject jsonObject = oc.createOTL(token, uid, filepath, csrf);
		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false); 
			return createResponse(jsonObject);
		}
		return createResponse(jsonObject);
	}

	public Response createResponse(JSONObject jsonObject) {
		return Response.status(200).entity(jsonObject.toString())
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
	}
	
	@Path("/update/{token}/{csrf}")
	@GET
	@Produces("application/json")
	public Response updateOTL(@PathParam("token") String token,
			@PathParam("csrf") String csrf) throws JSONException {

		JSONObject jsonObject = oc.updateCSRF(token, csrf);
		
		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false); 
			return createResponse(jsonObject);
		}

		return createResponse(jsonObject);
	}
	
	@Path("/delete/{token}")
	@GET
	@Produces("application/json")
	public Response deleteTreatment(@PathParam("token") String token) throws JSONException {

		JSONObject jsonObject = oc.deleteOTL(token);
		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false); 
			return createResponse(jsonObject);
		}

		//String result = "@Produces(\"application/json\") Delete user "+ uid +"\n\n" + jsonObject;
		return createResponse(jsonObject);
	}
}
