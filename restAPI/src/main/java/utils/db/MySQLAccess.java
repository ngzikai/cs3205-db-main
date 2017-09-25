package utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

public class MySQLAccess {
	private static Connection connect = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;

	private static DataSource datasource = null;
  private static Context initContext = null;
  private static String connectURL = "java:comp/env/jdbc/TestDB";

	public static void setConfiguration(String connectionURL){
    connectURL = connectionURL;
  }

  public static DataSource datasource(){
    if (datasource != null) {
      return datasource;
    }
    establishConnection();
    return datasource;
  }

  public static void establishConnection(){
    if (datasource == null){
      try{
        initContext = new InitialContext();
        // connection = "java:comp/env/jdbc/TestDB"
        datasource = (DataSource) initContext.lookup(connectURL);
      }catch(Exception e){
        e.printStackTrace();
      }
    }
  }

	// Get the connection from tomcat and return the connection to the caller
  public static Connection connectDatabase(){
    if (connect != null){
      return connect;
    }
		try{
			connect = datasource().getConnection();
		}catch(Exception e){
			e.printStackTrace();
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
