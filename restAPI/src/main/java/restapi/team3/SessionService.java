package restapi.team3;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import controller.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import entity.*;
import entity.steps.*;

import org.json.JSONObject;
import com.google.gson.*;

import utils.SystemConfig;
import utils.GUID;
import utils.ImageChecker;
import utils.team3.CommonUtil;
import utils.team3.JSONUtil;
import utils.team3.Generate;

// Video Checking
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegStream;
import net.bramp.ffmpeg.FFprobe;

public class SessionService{

	private String fileDirectory = SystemConfig.getConfig("storage_directory");
	private int userID;
	SessionController sc = null;
	private String type = "";
	private JSONObject user = null;
	private UserDataController udc;

	public SessionService(){
		 sc = new SessionController();
		 udc = new UserDataController(user);
	}

	public SessionService(String type, int userID, JSONObject user){
		sc = new SessionController();
		this.userID = userID;
		this.type = type;
		this.user = user;
		udc = new UserDataController(user);
	}

	@Path("/get/{uid}/meta")
	@GET
	@Produces("application/octet-stream")
	public Response getMeta(@PathParam("uid") int uid){
		Data data = null;
		data = sc.get(userID, uid, type);

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

		return Response.status(500).entity("Server error, contact the administrator.").build();
	}

	@Path("/get/{uid}/csv")
	@GET
	@Produces("application/octet-stream")
	public Response getCSV(@PathParam("uid") int uid){
		Data data = null;
		data = sc.get(userID, uid, type);

		if(data == null){
			return Response.status(400).entity("Invalid request.").build();
		}

		if(data.getSubtype().equalsIgnoreCase("step")){
				Steps s = sc.readStepFile(data.getAbsolutePath());
				String[] filesLocation = JSONUtil.stepsToCSVAndMeta(s, fileDirectory+"/time series/csv/", data.getContent());
				System.out.println("fileLocation: "+filesLocation[1]);
				File file = new File(fileDirectory+"/time series/csv/"+filesLocation[1]);
				return Response.ok(file, "application/octet-stream").build();
		}

		return Response.status(400).entity("Invalid request.").build();
	}

	@Path("/get/{uid}")
	@GET
	@Produces("application/octet-stream")
	public Response get(@PathParam("uid") int uid){
		File file = null;
		Data data = null;
		data = sc.get(userID, uid, type);

		if(data == null){
			return Response.status(400).entity("Invalid request.").build();
		}

		JSONObject jObj = new JSONObject(data);
		if(jObj != null && data.getSubtype().equalsIgnoreCase("heart")){
			return Response.status(200).entity(jObj.toString()).build();
		} else {
			file = sc.getActualFile(data.getAbsolutePath());
		}

		if(file != null){
			return Response.ok(file, "application/octet-stream").build();
		}
		return Response.status(400).entity("Server error, contact the administrator.").build();
	}

	@Path("/all")
	@GET
	@Produces("application/json")
	public Response getAll(){
		// Authentication
		List<Data> dataList = null;
		dataList = sc.getAll(userID, type);

		if(dataList == null){
			return Response.status(400).entity("Invalid request.").build();
		}

		JSONObject jObj = new JSONObject();
		jObj.put("list", dataList);
		return Response.status(200).entity(jObj.toString())
						.header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
						.build();
	}

	@GET
	@Path("/generate/{timestamp}")
	@Produces("application/octet-stream")
	public Response generateData(@PathParam("timestamp") long createdDate){
		ArrayList<Steps> stepsDataList = Generate.createStepsData(10);
		for (int i = 0; i < stepsDataList.size(); i++) {
			Gson gson = new Gson();
			String json = gson.toJson(stepsDataList.get(i));
			InputStream stream = new ByteArrayInputStream(json.getBytes());
			Response response = insert("RANDOM FOR NOW", createdDate, stream);
			System.out.println("result of insert:"+response.getStatus());
		}
		return Response.status(200).entity("test").build();
	}

	@Path("/upload/{timestamp}")
	@POST
	@Produces("application/octet-stream")
	public Response insert(@HeaderParam("X-NFC-Response")String nfcToken, @PathParam("timestamp") long createdDate, InputStream inputstream){
		if(nfcToken == null){
			return Response.status(401).entity("No NFC Token provided.").build();
		}
		// Obtain the NFC challenge in the database
		UserChallenge uc = udc.getChallengeData(user.getString("username"), "nfc");
		if (uc == null){
			return Response.status(401).entity("No NFC challenge found for user.").build();
		}
		// Base64 decode
		byte[] challenge = Base64.getDecoder().decode(uc.getChallengeString().getBytes());
		System.out.println(Base64.getEncoder().encodeToString(challenge));
		// Base64 decode user's nfc response
		byte[] nfcTokenByte = Base64.getDecoder().decode(nfcToken.getBytes());
		System.out.println("NFCTOKEN: "+nfcToken);
		// Base64 decode the hash(secret) nfcid of the database
		byte[] nfcHash = Base64.getDecoder().decode(user.getString("nfcid").getBytes());
		System.out.println(user.getString("nfcid"));

		// remove challenge as it has been used
		udc.deleteUserChallenge(user.getString("username"), "nfc");

		if(!udc.validateNFCResponse(nfcTokenByte, challenge, nfcHash)){
			return Response.status(401).entity("Invalid NFC token.").build();
		}
		ConsentController cc = new ConsentController();

		if(type.equalsIgnoreCase("heart")){
			int heartrate = -1;
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
				heartrate = Integer.parseInt(br.readLine());
			}catch(Exception e){
				return Response.status(400).entity("heartrate is not an integer.").build();
			}
			Data data = new Data(-1, userID, "Heart Rate", type,
													 "Heartrate of user: " + userID,
													 new Timestamp(createdDate),
													 new Timestamp(System.currentTimeMillis()),
													 String.valueOf(heartrate));
			int result = sc.insert(data);
			if(result >= 1){
				cc.createConsentByRecordCreation(data.getUid(), result);
				System.out.println("result:"+result);
				return Response.status(200).entity("successfully added heartrate: " + data.getContent()).build();
			}
		} else if (type.equalsIgnoreCase("image")){
			String mediaExt = "";
			File file = null;
			boolean isImage = false;
			// Check if it is a video/image file
			try{
				file = File.createTempFile(GUID.BASE58(), ".check");
				writeToTempFile(file, inputstream);
				// Check if temp file is an Image or Video
				isImage = ImageChecker.madeSafe(file);
				mediaExt = "."+ImageChecker.getExt(file);
				inputstream = new FileInputStream(file);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(!isImage){
				if(file != null){
					file.delete();
				}
				return Response.status(400).entity("Invalid file received.").build();
			}
			Data data = new Data(-1, userID, "File", type,
													 type + " of user: " + userID,
													 new Timestamp(createdDate),
													 new Timestamp(System.currentTimeMillis()),
													 createdDate + "_" + GUID.BASE58() + mediaExt);
			int result = sc.insert(data);
			if(result >= 1){
				cc.createConsentByRecordCreation(data.getUid(), result);
				sc.writeToFile(inputstream, fileDirectory + "/" + data.getUid() + "/" + type +"/" + data.getContent());
				return Response.status(200).entity("successfully added "+type+": " + data.getContent()).build();
			}
		} else if (type.equalsIgnoreCase("video")){
			File file = null;
			boolean isVideo = false;
			try{
				file = File.createTempFile(GUID.BASE58(), ".check");
				writeToTempFile(file, inputstream);
				// Server must have ffprobe installed
				FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");
				FFmpegProbeResult probeResult = ffprobe.probe(file.getAbsolutePath());
				FFmpegFormat format = probeResult.getFormat();
				// System.out.format("%nFile: '%s' ; Format: '%s' ; Duration: %.3fs",
				// 	format.filename,
				// 	format.format_long_name,
				// 	format.duration
				// );

				FFmpegStream stream = probeResult.getStreams().get(0);
				// System.out.format("%nCodec: '%s' ; Width: %dpx ; Height: %dpx",
				// 	stream.codec_long_name,
				// 	stream.width,
				// 	stream.height
				// );
				inputstream = new FileInputStream(file);
				String stringForm = format.format_long_name;
				if(stringForm.equalsIgnoreCase("QuickTime / MOV")){
					isVideo = true;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			if(!isVideo){
				if(file!=null){
					file.delete();
				}
				return Response.status(400).entity("Invalid file received.").build();
			}
			Data data = new Data(-1, userID, "File", type,
													 type + " of user: " + userID,
													 new Timestamp(createdDate),
													 new Timestamp(System.currentTimeMillis()),
													 createdDate + "_" + GUID.BASE58() + ".mp4");
			int result = sc.insert(data);
			if(result >= 1){
				cc.createConsentByRecordCreation(data.getUid(), result);
				System.out.println("result:"+result);
				sc.writeToFile(inputstream, fileDirectory + "/" + data.getUid() + "/" + type +"/" + data.getContent());
				return Response.status(200).entity("successfully added "+type+": " + data.getContent()).build();
			}
		} else if (type.equalsIgnoreCase("step")){
			// Check if it is an JSON object
			try{
				byte[] content = CommonUtil.inputStreamToByteArray(inputstream);
				BufferedReader reader = sc.prepareContentToJSON(content);
				Gson gson = new GsonBuilder().create();
				Steps steps = gson.fromJson(reader, Steps.class);
				reader.close();
				Data data = new Data(-1, userID, "Time Series", type,
														 type + " of user: " + userID,
														 new Timestamp(createdDate),
														 new Timestamp(System.currentTimeMillis()),
														 createdDate + "_" + GUID.BASE58() + ".json");
				int result = sc.insert(data);
				if(result >= 1){
					cc.createConsentByRecordCreation(data.getUid(), result);
					System.out.println("result:"+result);
					InputStream targetStream = new ByteArrayInputStream(content);
					sc.writeToFile(targetStream, fileDirectory + "/" + data.getUid() + "/time series/" + data.getContent());
					return Response.status(200).entity("successfully added step: " + data.getContent()).build();
				}
			}catch(Exception e){
				e.printStackTrace();
				return Response.status(400).entity("Not a valid Step JSON file.").build();
			}
		}
		return Response.status(400).entity("unknown type request").build();
	}

	private void writeToTempFile(File file, InputStream inputstream) throws Exception{
		FileOutputStream out = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int bytesRead;
		while((bytesRead=inputstream.read(buffer))!= -1){
			out.write(buffer, 0, bytesRead);
		}
		out.flush();
		out.close();
	}
}
