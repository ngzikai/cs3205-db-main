package restapi.team1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import controller.OTLController;
import entity.OneTimeLink;
import entity.User;

@Path("/team1/otl")
public class OTLService {
	private OTLController oc = new OTLController();

	@GET
	@Produces("application/json")
	public Response getAllOTL() throws JSONException {

		JSONObject jsonObject = oc.getAllOTL();

		return createResponse(jsonObject);
	}

	@Path("{s}")
	@GET
	@Produces("application/json")
	public Response getOTLwithToken(@PathParam("s") String token) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject = oc.getOTLWithToken(token);

		return createResponse(jsonObject);
	}
	
	@Path("/create/{token}/{uid}/{filepath: .+}/{csrf}/{datatype}")
	@GET
	@Produces("application/json")
	public Response createOTL(@PathParam("token") String token,
			@PathParam("uid") int uid, @PathParam("filepath") String filepath, 
			@PathParam("csrf") String csrf, @PathParam("datatype") String dataType) throws JSONException {
		JSONObject jsonObject = oc.createOTL(token, uid, filepath, csrf, dataType);

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
	
	@Path("/update/{token}/{csrf}")
	@GET
	@Produces("application/json")
	public Response updateOTL(@PathParam("token") String token,
			@PathParam("csrf") String csrf) throws JSONException {

		JSONObject jsonObject = oc.updateCSRF(token, csrf);

		return createResponse(jsonObject);
	}
	
	@Path("/delete/{token}")
	@GET
	@Produces("application/json")
	public Response deleteTreatment(@PathParam("token") String token) throws JSONException {

		JSONObject jsonObject = oc.deleteOTL(token);

		return createResponse(jsonObject);
	}
	
	@Path("/create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response createOTL(OneTimeLink otl) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject = oc.createOTL(otl);

		return createResponse(jsonObject);
	}
}
