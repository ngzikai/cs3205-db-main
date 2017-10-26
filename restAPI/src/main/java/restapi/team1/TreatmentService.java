package restapi.team1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import controller.TreatmentController;
import entity.Treatment;
import entity.User;

@Path("/team1/treatment")
public class TreatmentService {
	private TreatmentController tc = new TreatmentController();

	@GET
	@Produces("application/json")
	public Response getAllTreatment() throws JSONException {

		JSONObject jsonObject = tc.getAllTreatment();

		return createResponse(jsonObject);
	}

	@Path("{s}")
	@GET
	@Produces("application/json")
	public Response getTreatmentWithId(@PathParam("s") int id) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		System.out.println("Retrieving details of Treatment: " + id);
		jsonObject = tc.getTreatmentWithId(id);

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
	public Response updateUser(@PathParam("id") int id) throws JSONException {

		JSONObject jsonObject = tc.updateTreatment(id);

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
	public Response deleteTreatment(@PathParam("id") int id) throws JSONException {

		JSONObject jsonObject = tc.deleteTreatment(id);

		return createResponse(jsonObject);
	}
	
	@Path("/patient/{patientid}/{status}")
	@GET
	@Produces("application/json")
	public Response getTreatmentWithPatientId(@PathParam("patientid") int patientid, @PathParam("status") boolean status) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		System.out.println("Retrieving all details of Patientid: " + patientid + " with status : " + status);
		jsonObject = tc.getTreatmentWithPatientId(patientid, status);

		return createResponse(jsonObject);
	}
	
	@Path("/therapist/{therapistid}/{status}")
	@GET
	@Produces("application/json")
	public Response getTreatmentWithTherapistId(@PathParam("therapistid") int therapistid, @PathParam("status") boolean status) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		System.out.println("Retrieving all details of therapistid: " + therapistid + " with status : " + status);
		jsonObject = tc.getTreatmentWithTherapistId(therapistid, status);

		return createResponse(jsonObject);
	}
	
	@Path("/update/consentsetting")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response updateConsentSetting(Treatment treatment) throws JSONException {

		JSONObject jsonObject = new JSONObject();
		jsonObject = tc.updateConsentSetting(treatment);

		return createResponse(jsonObject);
	}
	
}
