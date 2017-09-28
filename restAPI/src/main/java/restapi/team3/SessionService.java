package restapi.team3;

import utils.db.core.*;
import utils.db.*;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import controller.SessionController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.Connection;
import entity.Admin;
@Path("/session")
public class SessionService{
  private SessionController sc = null;

  @Path("/{type}")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String get(@PathParam("type") String type){
    sc = new SessionController(type);
		ArrayList<Admin> adminList = null;
    // List<DataObject> list = sc.getAllObjects();
		String sql = "SELECT * FROM CS3205.users";
		try {
			Connection connect = MySQLAccess.connectDatabase();
			PreparedStatement preparedStatement = connect.prepareStatement(sql);
			adminList = resultSetToAdminList(MySQLAccess.readDataBasePS(preparedStatement));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    String result = "TESTING: ";
    for (Admin a : adminList){
      a.print();
      result += a.getUsername()+ " "+a.getAdminId()+" "+a.getPassword()+" ";
    }

    return result;
  }

	private ArrayList<Admin> resultSetToAdminList(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		ArrayList<Admin> adminList = new ArrayList<Admin>();
		while (resultSet.next()) {
			int id = resultSet.getInt("uid");
			String username = resultSet.getString("firstname");
			String password = resultSet.getString("lastname");
			String salt = "hello";

			Admin admin = new Admin(id, username, password);
			adminList.add(admin);
		}
		MySQLAccess.close();
		return adminList;
	}
}
