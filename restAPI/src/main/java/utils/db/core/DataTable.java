package utils.db.core;

import java.util.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import utils.db.core.*;


public class DataTable{
  String tableName = "";
  Map<String, DataObject> table = null;
  ResultSet rs = null;
  public DataTable(String tableName){
    this.tableName = tableName;
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
    return queryAll();
  }

  private List<DataObject> queryAll(){
    return query(null, null, null);
  }

  public List<DataObject> query(String[] columns, String[] conditions, Object[] variables){
    List<DataObject> rows = new ArrayList<>();
    table = new HashMap<>();
    try{
      // rs = DBQueryParser.query(tableName, columns, conditions, variables);
      while(rs.next()){
        DataObject row = new DataObject(this);
        for (String column : getColumns()) {
          row.put(column, rs.getObject(column));
        }
        table.put(rs.getString("oid"), row);
        rows.add(row);
      }
    } catch(Exception s){
      s.printStackTrace();
    }
    return rows;
  }

  public List<String> getColumns(){
    List<String> columns = new ArrayList<>();
    try{
      ResultSetMetaData rsmd = rs.getMetaData();
      for(int i = 0; i < rsmd.getColumnCount(); i++){
        columns.add(rsmd.getColumnName(i));
      }
    }catch(Exception s){
      s.printStackTrace();
    }
    return columns;
  }

  // Creates a prepareStatement and pass to DBAccess;
  public int update(DataObject dataObject){
    // PreparedStatement preparedStatement = connect.prepareStatement(sql);
    // // to be implemented
    // return MySQLAccess.updateDataBasePS(prepareStatement);
    return 0;
  }

}
