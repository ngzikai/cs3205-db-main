package restapi.team3;

// import utils.db.core.*;
// import utils.db.*;
// import java.util.*;
import java.io.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.servlet.ServletContext;

// Rest Client
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import controller.SessionController;

// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.ArrayList;
//
// import java.sql.Connection;
// import entity.Admin;

public class SessionService{
  protected SessionController sc = null;

  @Context
  ServletContext servletContext;

  public SessionService(String tableName){
    sc = new SessionController(tableName);
  }

  @Path("/upload/test")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response testingUploadFileByClient(){
    String path = "/home/jim/temp/test.json";
    File f = new File(path);
    InputStream stream = null;
    try{
      stream = new FileInputStream(f);
    }catch(Exception e){
      e.printStackTrace();
    }
    Client client = ClientBuilder.newClient();
    Invocation.Builder builder = client.target("http://localhost:8080/cs3205-db-main/api/steps/upload").request();
    Response response = builder.post(Entity.entity(stream, "application/json"));
		return Response.status(200).entity("tesing only")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
  }

  @Path("/get")
  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response get(@PathParam("type") String uid){
    // Authentication to be done
    // Obtain the file
    File f = null;
    // send it over
		return Response.ok(f, MediaType.APPLICATION_OCTET_STREAM)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
        .header("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"" )
				.build();
  }

  @POST
  @Path("/upload")
  @Consumes("application/json")
  public Response uploadFile(final InputStream uploadedInputStream) {
      // Authentication to be done
      // From the inputStream, send to controller
    boolean result = sc.uploadFile(uploadedInputStream);
    // return result of uploading
    return Response.status(200).entity(result).build();
  }

  @Path("/delete")
  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  public Response delete(@PathParam("type") String uid){
    // Authentication to be done
    // Obtain the uid of the file to be deleted
		return Response.status(200).entity(uid)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
  }
}
