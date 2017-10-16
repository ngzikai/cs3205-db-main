// package restapi.team3;
//
// import utils.db.core.*;
// import utils.db.*;
// import java.util.*;
// import javax.ws.rs.*;
// import javax.ws.rs.core.MediaType;
// import javax.ws.rs.core.Response;
//
// import controller.SessionController;
//
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.ArrayList;
//
// import java.sql.Connection;
// import entity.Admin;
// @Path("/session")
// public class SessionService{
//   private SessionController sc = null;
//
//   @Path("/{type}")
//   @GET
//   @Produces(MediaType.TEXT_PLAIN)
//   public String get(@PathParam("type") String type){
//     sc = new SessionController(type);
// 		ArrayList<Admin> adminList = null;
//     // List<DataObject> list = sc.getAllObjects();
// 		String sql = "SELECT * FROM CS3205.users";
// 		try {
// 			Connection connect = MySQLAccess.connectDatabase();
// 			PreparedStatement preparedStatement = connect.prepareStatement(sql);
// 			adminList = resultSetToAdminList(MySQLAccess.readDataBasePS(preparedStatement));
// 		} catch (Exception e) {
// 			// TODO Auto-generated catch block
// 			e.printStackTrace();
// 		}
//     String result = "TESTING: ";
//     for (Admin a : adminList){
//       a.print();
//       result += a.getUsername()+ " "+a.getAdminId()+" "+a.getPassword()+" ";
//     }
//
//     return result;
//   }
//
//   @Path("/{type}/save")
//   @GET
//   @Produces(MediaType.APPLICATION_JSON)
//   public Response insert(@PathParam("type") String type){
//     sc = new SessionController(type);
//     DataObject dataObject = new DataObject(sc.getTable());
//     dataObject.put("firstname", "another test");
//     dataObject.put("lastname", "test awefawef");
//     System.out.println(dataObject.save());
//     String result = "what";
//     return Response.status(200).entity(result).build();
//
//   }
//
//   @Path("/{type}/save/again")
//   @GET
//   @Produces(MediaType.APPLICATION_JSON)
//   public Response update(@PathParam("type") String type){
//     sc = new SessionController(type);
//     List<DataObject> list = sc.getAllObjects();
//     for (DataObject dobj : list){
//       if(dobj.get("firstname").toString().equalsIgnoreCase("another test")){
//         dobj.put("lastname", "it updated");
//         System.out.println(dobj.save());
//       }
//     }
//     String result = "what";
//     return Response.status(200).entity(result).build();
//   }
//
//   @Path("/{type}/delete")
//   @GET
//   @Produces(MediaType.APPLICATION_JSON)
//   public Response delete(@PathParam("type") String type){
//     sc = new SessionController(type);
//     List<DataObject> list = sc.getAllObjects();
//     for (DataObject dobj : list){
//       if(dobj.get("firstname").toString().equalsIgnoreCase("another test")){
//         System.out.println(dobj.delete());
//       }
//     }
//     String result = "what";
//     return Response.status(200).entity(result).build();
//   }
//
// 	private ArrayList<Admin> resultSetToAdminList(ResultSet resultSet) throws SQLException {
// 		// ResultSet is initially before the first data set
// 		ArrayList<Admin> adminList = new ArrayList<Admin>();
// 		while (resultSet.next()) {
// 			int id = resultSet.getInt("uid");
// 			String username = resultSet.getString("firstname");
// 			String password = resultSet.getString("lastname");
// 			String salt = "hello";
//
// 			Admin admin = new Admin(id, username, password, salt);
// 			adminList.add(admin);
// 		}
// 		MySQLAccess.close();
// 		return adminList;
// 	}
// }
