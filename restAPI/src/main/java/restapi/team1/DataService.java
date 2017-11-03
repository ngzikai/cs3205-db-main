package restapi.team1;
import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import controller.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.sql.Timestamp;
import entity.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import utils.SystemConfig;
import utils.GUID;

@Path("/team1/record/")
public class DataService {
	private String fileDirectory = SystemConfig.getConfig("storage_directory");
	DocumentController dc = new DocumentController();

	@GET
	@Produces("application/json")
	public Response get(){
		return Response.status(200).entity("it is running").build();
	}
/*
	@Path("/get/{uid}/meta")
	@GET
	@Produces("application/octet-stream")
	public Response getMeta(@PathParam("type")String type, @PathParam("userID") int userID, @PathParam("uid") int uid){
		SessionController sc = getSessionController(type);
		Data data = null;
		data = sc.get(userID, uid);

		if(data == null){
			return Response.status(400).entity("Invalid request.").build();
		}

		JSONObject jObj = new JSONObject(data);
		if(data.getSubtype().equalsIgnoreCase("step")){
			jObj = sc.getStepMetaInfo(data);
		}

		if(jObj != null){
			return Response.status(200).entity(jObj.toString()).build();
		}

		return Response.status(400).entity("Server error, contact the administrator.").build();
	}


*/
	@Path("/all/{uid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@PathParam("uid") int uid){
		// Authentication
		String type = "all";
		SessionController sc = new SessionController();
		List<Data> dataList = null;
		dataList = sc.getAllFromUser(uid);

		if(dataList == null){
			return Response.status(400).entity("Invalid request.").build();
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("records", dataList);
		return createResponse(jsonObject);
	}

	//To get the file over with the rid
	@Path("/get/{rid}")
	@GET
	@Produces( {MediaType.APPLICATION_JSON, "application/octet-stream" })
	public Response get(@PathParam("rid") int rid){
		SessionController sc = new SessionController();
		File file = null;
		Data data = null;
		data = sc.get(rid);

		if(data == null){
			return createResponse(null);
		}

		JSONObject jObj = new JSONObject(data);
		if(jObj != null && data.getType().equalsIgnoreCase("Heart Rate")){
			return createResponse(jObj);
		} else {
			file = sc.getActualFile(data.getAbsolutePath());
		}
		
		String fileType = processFileType(data);
		if(fileType.equals(DocumentController.SUBTYPE) ) {
			jObj = dc.getDocument(data);
			return createResponse(jObj);
		}
		if(file != null){
			return Response.ok(file, "application/octet-stream")
					.header("X-File-Format", data.getContent().substring(data.getContent().lastIndexOf('.') + 1))
					.header("X-File-Type", fileType)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
					.header("ALLOW", "GET, POST, DELETE, PUT, OPTIONS")
					.build();
		}
		return Response.status(400).entity("Server error, contact the administrator.").build();
	}
	
	private String processFileType(Data data) {
		if(data.getSubtype()!= null)
			return data.getSubtype();
		
		return data.getType();
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
	
	//Retrieves a particular records
	@Path("/{rid}")
	@GET
	@Produces( {"application/json"})
	public Response getRecord(@PathParam("rid") int rid){
		SessionController sc = new SessionController();
		Data data = null;
		data = sc.get(rid);
		JSONObject jObj = null;
		
		if(data != null) {
			jObj = new JSONObject(data);
		}
		return createResponse(jObj);
	}

	@Path("/delete/{rid}/{uid}")
	@GET
	@Produces("application/json")
	public Response deleteDocument(@PathParam("rid") int rid, @PathParam("uid") int uid) throws JSONException {

		JSONObject jsonObject = dc.deleteDocument(rid, uid);

		return createResponse(jsonObject);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response getArrayOfRecords(InputStream inputstream) throws JSONException {
		JSONObject input = parseJSON(inputstream);
		SessionController sc = new SessionController();
		List<Data> dataList = new ArrayList<>();
		System.out.println(input);
		JSONArray ridArray = input.getJSONArray("rid");
		for(int i = 0; i < ridArray.length(); i++) {
		    int rid = ridArray.getInt(i);
	        Data data = sc.get(rid);
	        dataList.add(data);
		}

		if(dataList.size() < 1){
			return Response.status(400).entity("Invalid request.").build();
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("records", dataList);
		return createResponse(jsonObject);
	}
	
	/*
	 * Parse inputstream and return as JSONObject
	 */
	private JSONObject parseJSON(InputStream inputstream) {
		JSONObject jsonObject = null;
		try {
	       BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
	       StringBuilder responseStrBuilder = new StringBuilder();

	       String inputStr;
	       while ((inputStr = streamReader.readLine()) != null)
	           responseStrBuilder.append(inputStr);

	       jsonObject = new JSONObject(responseStrBuilder.toString());
		}catch(Exception e) {
			
		}
		return jsonObject;
	}
	
	@Path("/alldocuments/{uid}")
	@GET
	@Produces("application/json")
	public Response getConsentWithUid(@PathParam("uid") int uid) throws JSONException {
		JSONObject jsonObject = new JSONObject();

		System.out.println("Retrieving all documents sharable to uid: " + uid );
		jsonObject = dc.getAllDocumentWithFullConsentsFromUid(uid);

		return createResponse(jsonObject);
	}

}
