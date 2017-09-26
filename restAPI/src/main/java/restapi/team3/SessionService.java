package restapi.team3;

// import utils.db.core.*;
// import utils.db.*;
// import java.util.*;
import java.io.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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


  public SessionService(String tableName){
    sc = new SessionController(tableName);
  }

  @Path("/upload/test")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response getasd(){

    Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target("http://localhost:8080/cs3205-db-main/step/upload");

		return Response.status(200).entity("wheeee")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
  }

  @Path("/get")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response get(@PathParam("type") String uid){
		return Response.status(200).entity(uid)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
  }

  @POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
		@FormDataParam("file") InputStream uploadedInputStream,
		@FormDataParam("file") FormDataContentDisposition fileDetail) {
    boolean result = sc.uploadFile(uploadedInputStream);

		return Response.status(200).entity("result").build();
	}

  @Path("/delete")
  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  public Response delete(@PathParam("type") String uid){
		return Response.status(200).entity(uid)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
				.build();
  }
}
