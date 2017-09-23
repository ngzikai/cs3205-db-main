package cs3205.db.restapi.team2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
	private Connection conn = null;
	
	public DatabaseConnector(String dbUser, String dbPassword) {
		String url = "jdbc:mysql://172.25.76.76/CS3205";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection(url, dbUser, dbPassword);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
//	public ResultSet executeQuery(String sql) throws SQLException{
//		Statement statement = conn.createStatement();
//		return statement.executeQuery(sql);
//	}
//	
//	public int executeUpdate(String sql) throws SQLException{
//		Statement statement = conn.createStatement();
//		return statement.executeUpdate(sql);
//	}
}
