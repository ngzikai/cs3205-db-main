package restapi.team1;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import controller.CsrfController;

@Path("/team1/csrf")
public class CsrfService {

	private CsrfController cc = new CsrfController();

	@GET
	@Produces("application/json")
	public Response getAllOTL() throws JSONException {

		JSONObject jsonObject = cc.getAllCsrf();

		return createResponse(jsonObject);
	}

	@Path("{s: .+}")
	@GET
	@Produces("application/json")
	public Response getOTLwithToken(@PathParam("s") String csrfToken) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject = cc.getCsrfWithToken(csrfToken);

		return createResponse(jsonObject);
	}

	@Path("/create/{csrfToken: .+}/{uid}/{expiry}/{description}")
	@GET
	@Produces("application/json")
	public Response createOTL(@PathParam("csrfToken") String csrfToken, @PathParam("uid") int uid,
			@PathParam("expiry") int expiry, @PathParam("description") String description) throws JSONException {
		JSONObject jsonObject = cc.createCsrf(csrfToken, uid, expiry, description);

		return createResponse(jsonObject);
	}

	@Path("/update/{csrfToken: .+}/{expiry}")
	@GET
	@Produces("application/json")
	public Response updateOTL(@PathParam("csrfToken") String csrfToken,
			@PathParam("expiry") int expiry) throws JSONException {

		JSONObject jsonObject = cc.updateCSRF(csrfToken, expiry);

		return createResponse(jsonObject);
	}

	@Path("/delete/{csrfToken: .+}")
	@GET
	@Produces("application/json")
	public Response deleteTreatment(@PathParam("csrfToken") String csrfToken) throws JSONException {

		JSONObject jsonObject = cc.deleteCsrf(csrfToken);

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
}

