package utils.db.core;

import java.util.*;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import utils.db.*;


public class DataTable{
  protected String tableName = "";
  protected Map<String, DataObject> table = null;
  protected List<String> columns = null;
  public DataTable(String tableName){
    this.tableName = tableName;
  }

  public DataObject getDataObject(String uid){
    // // Sanity Check
    // if(table == null){
    //   getAllObjects();
    // }
    getAllObjects();

    return table.get(uid);
  }

  public List<DataObject> getAllObjects(){
    // if(table != null){
    //   List<DataObject> list = new ArrayList<DataObject>(table.values());
    //   return list;
    // }
    table = new HashMap<String, DataObject>();
    return queryAll();
  }

  public List<String> getColumns(){
    if(columns != null){
      return columns;
    }
    columns = new ArrayList<>();
    try{
      DatabaseMetaData meta = MySQLAccess.connectDatabase().getMetaData();
      ResultSet rs = meta.getColumns("CS3205", null, tableName,  "%");
      while(rs.next()){
        columns.add(rs.getString(4));
      }
    }catch(Exception s){
      s.printStackTrace();
    }
    return columns;
  }

  // Insert or Update the object in the database table
  public int update(DataObject dataObject){
    PreparedStatement preparedStatement = null;
    String sql = "INSERT INTO CS3205."+tableName +" ";
    List<Object> updateVariableOrder = new ArrayList<>();
    int result = -1;
    try{
      // dataObject does not exists in table
        String columnOrder = "(";
        String variables = "(";
        for(String key : getColumns()){
          columnOrder += key+",";
          if (key.equalsIgnoreCase("uid")){
          } else if(dataObject.get(key)==null){
            System.out.println("Ensure that all the keys are filled. "+key+" is missing.");
            return -1;
          }
          variables += "?, ";
          updateVariableOrder.add(dataObject.get(key));
        }
        // remove the last ,
        columnOrder = (new StringBuilder(columnOrder).replace(columnOrder.lastIndexOf(","), columnOrder.lastIndexOf(",") + 1, "").toString())+")";
        variables = (new StringBuilder(variables).replace(variables.lastIndexOf(","), variables.lastIndexOf(",") + 1, "").toString())+")";
        sql += columnOrder+" VALUES "+variables;
        System.out.println(sql);
        // ON DUPLICATE, UPDATE
        sql += " ON DUPLICATE KEY UPDATE ";

        for(String key : getColumns()){
          if (key.equalsIgnoreCase("uid")){
          } else if(dataObject.get(key)==null){
            System.out.println("Ensure that all the keys are filled. "+key+" is missing.");
            return -1;
          }
          sql += key+" = ?, ";
          updateVariableOrder.add(dataObject.get(key));
        }
        // remove the last ,
        sql = (new StringBuilder(sql).replace(sql.lastIndexOf(","), sql.lastIndexOf(",") + 1, "").toString());
        preparedStatement = MySQLAccess.connectDatabase().prepareStatement(sql);
        int pt = 0;
        for(Object obj : updateVariableOrder){
          preparedStatement = updateVariables(preparedStatement, obj, pt);
          pt++;
        }
        result = MySQLAccess.updateDataBasePS(preparedStatement);
      //}else {
      //   Object uid = dataObject.get("uid");
      //   sql = "UPDATE CS3205."+tableName+" SET ";
      //   for(String key : getColumns()){
      //     if (key.equalsIgnoreCase("uid")){
      //       continue;
      //     } else if(dataObject.get(key)==null){
      //       System.out.println("Ensure that all the keys are filled. "+key+" is missing.");
      //       return -1;
      //     } else {
      //       sql += key+" = ?, ";
      //       updateVariableOrder.add(dataObject.get(key));
      //     }
      //   }

      //   sql += " WHERE uid = ?";
      //   updateVariableOrder.add(dataObject.get("uid"));
      // }
    } catch(Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public int remove(DataObject dataObject){
    PreparedStatement ps = null;
    String sql = "DELETE FROM CS3205."+tableName +" WHERE uid = ?";
    int result = -1;
    try{
      if(dataObject.get("uid") != null){
        ps = MySQLAccess.connectDatabase().prepareStatement(sql);
        ps = updateVariables(ps, dataObject.get("uid"), 0);
        result = MySQLAccess.updateDataBasePS(ps);
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    return result;
  }

  ////////////////////////////////////////////////////////////////////////
  //
  // Private Methods
  //
  ////////////////////////////////////////////////////////////////////////

  private List<DataObject> queryAll(){
    List<DataObject> rows = new ArrayList<>();
    String sql = "SELECT * FROM CS3205."+tableName;

    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      ResultSet rs = MySQLAccess.readDataBasePS(ps);
      while(rs.next()){
        DataObject row = new DataObject(this);
        for (String column : getColumns()) {
          row.put(column, rs.getObject(column));
        }
        table.put(rs.getString("uid"), row);
        rows.add(row);
      }
    } catch(Exception s){
      s.printStackTrace();
    }
    return rows;
  }

  private static PreparedStatement updateVariables(PreparedStatement ps, Object argObj, int pt) throws SQLException, Exception{
    if (argObj == null) {
      ps.setNull(pt + 1, 0);
    } else if (String.class.isInstance(argObj)) {
      ps.setString(pt + 1, (String) argObj);
    } else if (Integer.class.isInstance(argObj)) {
      ps.setInt(pt + 1, (Integer) argObj);
    } else if (Long.class.isInstance(argObj)) {
      ps.setLong(pt + 1, (Long) argObj);
    } else if (Double.class.isInstance(argObj)) {
      ps.setDouble(pt + 1, (Double) argObj);
    } else if (Float.class.isInstance(argObj)) {
      ps.setFloat(pt + 1, (Float) argObj);
    } else if (Date.class.isInstance(argObj)) {
      java.sql.Date sqlDate = new java.sql.Date(((Date) argObj).getTime());
      ps.setDate(pt + 1, sqlDate);
    } else if(argObj instanceof byte[]) {
      ps.setBytes(pt + 1, (byte[]) argObj);
    } else if (java.sql.Blob.class.isInstance(argObj)) {
      ps.setBlob(pt + 1, (java.sql.Blob) argObj);
    } else {
      String argClassName = argObj.getClass().getName();
      throw new Exception("Unknown argument type (" + pt + ") : " + (argClassName));
    }
    return ps;
  }

}
