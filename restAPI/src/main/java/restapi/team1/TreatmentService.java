package restapi.team1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import controller.TreatmentController;

@Path("/team1/treatment")
public class TreatmentService {
	private TreatmentController tc = new TreatmentController();

	@GET
	@Produces("application/json")
	public Response getAllTreatment() throws JSONException {

		JSONObject jsonObject = tc.getAllTreatment();

		//String result = "@Produces(\"application/json\") List of Admins \n\n" + jsonObject;
		return createResponse(jsonObject);
	}

	@Path("{s}")
	@GET
	@Produces("application/json")
	public Response getTreatmentWithId(@PathParam("s") int id) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		System.out.println("Retrieving details of Treatment: " + id);
		jsonObject = tc.getTreatmentWithId(id);

		if(jsonObject == null) {
			jsonObject = new JSONObject();
			jsonObject.put("result", false);
			return createResponse(jsonObject);
		}
		return createResponse(jsonObject);
	}
	
	@Path("/create/{patient}/{therapist}")
	@GET
	@Produces("application/json")
	public Response createUser(@PathParam("patient") int patientId,
			@PathParam("therapist") int therapistId) throws JSONException {
		JSONObject jsonObject = tc.createTreatment(patientId, therapistId);
		return createResponse(jsonObject);
	}

	public Response createResponse(JSONObject jsonObject) {
		return Response.status(200).entity(jsonObject.toString())
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
	}
	
	@Path("/update/{id}")
	@GET
	@Produces("application/json")
	public Response updateUser(@PathParam("id") int id) throws JSONException {

		JSONObject jsonObject = tc.updateTreatment(id);

		return createResponse(jsonObject);
	}
	
	@Path("/delete/{id}")
	@GET
	@Produces("application/json")
	public Response deleteTreatment(@PathParam("id") int id) throws JSONException {

		JSONObject jsonObject = tc.deleteTreatment(id);

		//String result = "@Produces(\"application/json\") Delete user "+ uid +"\n\n" + jsonObject;
		return createResponse(jsonObject);
	}
	
}
