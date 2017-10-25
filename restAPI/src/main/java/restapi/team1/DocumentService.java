package restapi.team1;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import controller.DocumentController;
import entity.Document;

@Path("/team1/record/document")
public class DocumentService {
	DocumentController dc = new DocumentController();
	
	@Path("/create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response createDocumentPost(Document newDocument) throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		System.out.println("Old date value: " + newDocument.getCreationdate());
		jsonObject = dc.createDocument(newDocument);

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
				.header("ALLOW", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
	}
}
