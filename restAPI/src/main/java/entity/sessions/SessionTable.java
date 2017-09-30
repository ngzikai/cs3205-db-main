// package entity.sessions;
//
// import utils.db.core.*;
// import utils.db.*;
// import utils.GUID;
//
// import java.util.*;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
//
// public class SessionTable extends DataTable{
//
//   private String fileLocation = "";
//   public SessionTable(String tableName){
//     super(tableName);
//   }
//
//   public Session getDataObject(String uid){
//     // Sanity Check
//     if(super.table == null){
//       super.getAllObjects();
//     }
//     DataObject dObj = super.table.get(uid);
//     Session s = new Session(this);
//     s.putAll(dObj);
//     s.setFileLocation("user1" + "/" + tableName
//                          + "/" + s.get("location").toString());
//     return s;
//   }
//
//   public Session newSession(){
//     String uid = GUID.BASE58();
//     Session s = new Session(this);
//     s.put("uid", uid);
//     s.save();
//     return s;
//   }
//
//   public String getTableName(){
//     return super.tableName;
//   }
//
//   public List<DataObject> getAllUserObjects(String userID){
//     List<DataObject> rows = new ArrayList<>();
//     PreparedStatement ps = null;
//     String sql = "SELECT * FROM CS3205." + this.tableName + " WHERE ownerID = ?";
//     try{
//       ps = MySQLAccess.connectDatabase().prepareStatement(sql);
//       ps.setString(1, userID);
//       ResultSet rs = MySQLAccess.readDataBasePS(ps);
//       while(rs.next()){
//         DataObject row = new DataObject(this);
//         for (String column : super.getColumns()) {
//           row.put(column, rs.getObject(column));
//         }
//         rows.add(row);
//       }
//     }catch(Exception e){
//       e.printStackTrace();
//     }
//     return rows;
//   }
// }
