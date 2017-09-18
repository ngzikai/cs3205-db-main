package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class MySQLAccess {
	private static Connection connect = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	private static String hostname = "localhost";
	private static String user = "root";
	private static String pass = "root";

	//This method will load MySQL driver and set up connection to the DB
	public static Connection connectDatabase() throws Exception {
		if(connect != null) {
			return connect;
		}
		try {
			//Load MySQL driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setting up the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://"+ hostname +"/cs3205?"
							+ "user=" + user +"&password=" + pass);
		} catch (Exception e) {
			throw e;
		}
		return connect;
	}
	
	//This method will perform a SQL statement on the database and return an result set.
	public ResultSet readDataBase(String sqlSelect)  throws Exception{
		// Statements allow to issue SQL queries to the database
		Statement statement = connect.createStatement();
		ResultSet result = statement.executeQuery(sqlSelect);
		writeMetaData(result);
		return result;
	}
	
	public static ResultSet readDataBasePS(PreparedStatement preparedStatement) throws Exception {
		ResultSet resultSet = preparedStatement.executeQuery();
		return resultSet;
	}
	
	public static int updateDataBasePS(PreparedStatement preparedStatement) throws Exception {
		int result = preparedStatement.executeUpdate();
		return result;
	}
	
	/*
	public ResultSet readDataBasePS(String sql, String table, String element, String target) throws Exception {
		preparedStatement = connect
				.prepareStatement("Select * from ? where ? = ? ");
		preparedStatement.setString(1, table);
		preparedStatement.setString(2, element);
		preparedStatement.setString(3, target);
		resultSet = preparedStatement.executeQuery();
		return resultSet;
	}*/

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		//  Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			int id = resultSet.getInt("admin_id");
			String username = resultSet.getString("username");
			String password = resultSet.getString("password");
			String salt = resultSet.getString("salt");
			//Date date = resultSet.getDate("datum");
			//String comment = resultSet.getString("comments");
			System.out.println("Admin id: " + id);
			System.out.println("User: " + username);
			System.out.println("Password: " + password);
			System.out.println("Salt: " + salt);
			//System.out.println("Date: " + date);
			//System.out.println("Comment: " + comment);
		}
	}

	// You need to close the resultSet
	public static void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}

			if (statement != null) {
				statement.close();
				statement = null;
			}

			if (connect != null) {
				connect.close();
				connect = null;
			}
		} catch (Exception e) {

		}
	}

}
