package restapi.team3;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import controller.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import entity.*;
import entity.sessions.*;

import org.json.JSONObject;

import utils.SystemConfig;

@Path("/team3/{type}/{userID}")
public class SessionService{
  // @Context
  // UriInfo uri;

  private String fileDirectory = SystemConfig.getConfig("storage_directory");

  // @GET
  // public Response getRoot() {
  //     return Response.ok(new Links(Links.newLink(this.uri, "", "self", "GET"),
  //             Links.newLink(this.uri, "get/{uid}", "oauth.token", "GET"),
  //             Links.newLink(this.uri, "all", "all", "GET"),
  //             Links.newLink(this.uri, "upload", "upload", "POST"))).build();
  // }
  @Path("/get/{uid}")
  @GET
  @Produces("application/octet-stream")
  public Response get(@PathParam("type")String type, @PathParam("userID") int userID, @PathParam("uid") String uid){
    SessionController sc = getSessionController(type);
    HeartRate hr = null;
    ImageVideo iv = null;
    Step s = null;
    File file = null;
    if(type.equalsIgnoreCase("heart")){
       hr = sc.getHeartrate(uid, userID);
    } else if (type.equalsIgnoreCase("video") || type.equalsIgnoreCase("image")){
      iv = sc.getFile(uid, userID, type);
      file = (iv != null) ? sc.getActualFile(iv.getFileLocation(), iv.getOwnerID(), type) : null;
    } else if (type.equalsIgnoreCase("step")){
      s = sc.getStep(uid, userID);
      file = (s != null) ? sc.getActualFile(s.getFileLocation(), s.getOwnerID(), type) : null;
    }

    if(s == null && iv == null && hr == null){
      return Response.status(400).entity("Invalid request.").build();
    }

    JSONObject jObj = (hr != null) ? new JSONObject(hr) : null;
    if(jObj != null){
      return Response.status(200).entity(jObj.toString()).build();
    } else if(file != null){
      return Response.ok(file, "application/octet-stream").build();
    }
    return Response.status(400).entity("Server error, contact the administrator.").build();
  }

  @Path("/all")
  @GET
  @Produces("application/json")
  public Response getAll(@PathParam("type")String type, @PathParam("userID") int userID){
    // Authentication
    SessionController sc = getSessionController(type);
    List<HeartRate> hr = null;
    List<ImageVideo> iv = null;
    List<Step> s = null;
    if(type.equalsIgnoreCase("heart")){
       hr = sc.getAllHeartRate(userID);
    } else if (type.equalsIgnoreCase("video") || type.equalsIgnoreCase("image")){
      iv = sc.getAllImageVideo(userID, type);
    } else if (type.equalsIgnoreCase("step")){
      s = sc.getAllStep(userID);
    }

    if(s == null && iv == null && hr == null){
      return Response.status(400).entity("Invalid request.").build();
    }

    JSONObject jObj = new JSONObject();
    jObj.put("list", ((hr!=null)? hr : (iv != null) ? iv : s));
    return Response.status(200).entity(jObj.toString())
    				.header("Access-Control-Allow-Origin", "*")
    				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
            .build();
  }

  @Path("/upload/{timestamp}")
  @POST
  @Produces("application/octet-stream")
  public Response insert(@PathParam("type")String type, @PathParam("timestamp") long createdDate, @PathParam("userID") int userID, InputStream inputstream){
    SessionController sc = getSessionController(type);
    if(type.equalsIgnoreCase("heart")){
      int heartrate = -1;
      try{
        BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
        heartrate = Integer.parseInt(br.readLine());
      }catch(Exception e){
        return Response.status(400).entity("heartrate is not an integer.").build();
      }
      HeartRate hr = new HeartRate();
      hr.setOwnerID(userID);
      hr.setHeartrate(heartrate);
      hr.setTimestamp(createdDate);
      int result = sc.insertHeartRate(hr);
      if(result == 1){
        return Response.status(200).entity("successfully added uid: " + hr.getUid()).build();
      }
    } else if (type.equalsIgnoreCase("video") || type.equalsIgnoreCase("image")){
      ImageVideo s = new ImageVideo();
      s.setTimestamp(createdDate);
      s.setOwnerID(userID);
      String mediaExt = (type.equalsIgnoreCase("video")) ? ".mp4" : ".jpeg";
      if(mediaExt.equalsIgnoreCase("unknown")){
        return Response.status(400).entity("unknown type request").build();
      }
      // Check if it is a video/image file
      try{
        File file = File.createTempFile("temp", ".check");
        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while((bytesRead=inputstream.read(buffer))!= -1){
          out.write(buffer, 0, bytesRead);
        }
        out.flush();
        String fileType = Files.probeContentType(file.toPath());
        if(!fileType.matches("^(video|image).*")){
          out.close();
          file.delete();
          return Response.status(400).entity("Not a correct file").build();
        }
        out.close();
        inputstream = new FileInputStream(file);
        file.delete();
      }catch(Exception e){
        e.printStackTrace();
        return Response.status(400).entity("Invalid file received.").build();
      }

      s.setFileLocation(createdDate + "_" + s.getUid() + mediaExt);
      s.setType(type);
      int result = sc.insertFile(s);
      if (result == 1){
        sc.writeToFile(inputstream, fileDirectory + "/" + s.getOwnerID() + "/" + type +"/" + s.getFileLocation());
        return Response.status(200).entity("successfully added uid: " + s.getUid()).build();
      }
    } else if (type.equalsIgnoreCase("step")){
      // Check if it is an JSON object
      try{
        BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
        String jsonString = "";
        String line = "";
        while ((line=br.readLine()) != null){
          jsonString += line;
        }
        inputstream = new ByteArrayInputStream(jsonString.getBytes());
        JSONObject jsonObject = new JSONObject(jsonString);
      }catch(Exception e){
        return Response.status(400).entity("Not a JSON file.").build();
      }
      Step s = new Step();
      s.setTimestamp(createdDate);
      s.setOwnerID(userID);
      s.setFileLocation(createdDate + "_" + s.getUid() + ".json");
      int result = sc.insertStep(s);
      if (result == 1){
        sc.writeToFile(inputstream, fileDirectory + "/" + s.getOwnerID() + "/" + type +"/" + s.getFileLocation());
        return Response.status(200).entity("successfully added uid: " + s.getUid()).build();
      }
    }
    return Response.status(400).entity("unknown type request").build();
  }

  private SessionController getSessionController(String type){
    if(type.equalsIgnoreCase("image") || type.equalsIgnoreCase("video")){
      return new SessionController("file");
    } else if(type.equalsIgnoreCase("heart")){
      return new SessionController("heartrate");
    } else if(type.equalsIgnoreCase("step")){
      return new SessionController("step");
    }
    return null;
  }
}
