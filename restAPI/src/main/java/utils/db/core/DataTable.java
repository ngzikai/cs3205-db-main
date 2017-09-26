package utils.db.core;

import java.util.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import utils.db.*;


public class DataTable{
  String tableName = "";
  Map<String, DataObject> table = null;
  ResultSet rs = null;
  List<String> columns = null;
  public DataTable(String tableName){
    this.tableName = tableName;
    getAllObjects();
    getColumns();
  }

  public DataObject getDataObject(String oid){
    if(table == null){
      getAllObjects();
    }

    return table.get(oid);
  }

  public List<DataObject> getAllObjects(){
    if(table != null){
      List<DataObject> list = new ArrayList<DataObject>(table.values());
      return list;
    }
    table = new HashMap<String, DataObject>();
    return queryAll();
  }

  private List<DataObject> queryAll(){
    List<DataObject> rows = new ArrayList<>();
    String sql = "SELECT * FROM CS3205."+tableName;

    try{
      PreparedStatement ps = MySQLAccess.connectDatabase().prepareStatement(sql);
      rs = rs = MySQLAccess.readDataBasePS(ps);
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
    // return query(null, null, null);
  }

  // public List<DataObject> query(String[] columns, String[] conditions, Object[] variables){
  //   List<DataObject> rows = new ArrayList<>();
  //   table = new HashMap<>();
  //   try{
  //     rs = DBQueryParser.query(tableName, columns, conditions, variables);
  //     while(rs.next()){
  //       DataObject row = new DataObject(this);
  //       for (String column : getColumns()) {
  //         row.put(column, rs.getObject(column));
  //       }
  //       table.put(rs.getString("oid"), row);
  //       rows.add(row);
  //     }
  //   } catch(Exception s){
  //     s.printStackTrace();
  //   }
  //   return rows;
  // }

  public List<String> getColumns(){
    if(columns != null){
      return columns;
    }
    columns = new ArrayList<>();
    try{
      ResultSetMetaData rsmd = rs.getMetaData();
      for(int i = 1; i <= rsmd.getColumnCount(); i++){
        System.out.println(rsmd.getColumnCount() +" " +i);
        columns.add(rsmd.getColumnName(i));
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
      if(dataObject.get("uid") == null) {
        String columnOrder = "(";
        String variables = "(";
        for(String key : getColumns()){
          if (key.equalsIgnoreCase("uid")){
            continue;
          } else if(dataObject.get(key)==null){
            System.out.println("Ensure that all the keys are filled. "+key+" is missing.");
            return -1;
          } else {
            columnOrder += key+",";
            variables += "?, ";
            updateVariableOrder.add(dataObject.get(key));
          }
        }
        // remove the last ,
        columnOrder = (new StringBuilder(columnOrder).replace(columnOrder.lastIndexOf(","), columnOrder.lastIndexOf(",") + 1, "").toString())+")";
        variables = (new StringBuilder(variables).replace(variables.lastIndexOf(","), variables.lastIndexOf(",") + 1, "").toString())+")";
        sql += columnOrder+" VALUES "+variables;
        System.out.println(sql);
      } else {
        int uid = (int) dataObject.get("uid");
        sql = "UPDATE CS3205."+tableName+" SET ";
        for(String key : getColumns()){
          if (key.equalsIgnoreCase("uid")){
            continue;
          } else if(dataObject.get(key)==null){
            System.out.println("Ensure that all the keys are filled. "+key+" is missing.");
            return -1;
          } else {
            sql += key+" = ?, ";
            updateVariableOrder.add(dataObject.get(key));
          }
        }
        sql = (new StringBuilder(sql).replace(sql.lastIndexOf(","), sql.lastIndexOf(",") + 1, "").toString());
        sql += " WHERE uid = ?";
        updateVariableOrder.add(dataObject.get("uid"));
      }
      preparedStatement = MySQLAccess.connectDatabase().prepareStatement(sql);
      int pt = 0;
      for(Object obj : updateVariableOrder){
        preparedStatement = updateVariables(preparedStatement, obj, pt);
        pt++;
      }
      result = MySQLAccess.updateDataBasePS(preparedStatement);
    } catch(Exception e) {
      e.printStackTrace();
      return result;
    }
    return result;
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
