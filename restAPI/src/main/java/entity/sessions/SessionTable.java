package entity.sessions;

import utils.db.core.*;
import utils.GUID;

public class SessionTable extends DataTable{

  private String fileLocation = "";
  public SessionTable(String tableName){
    super(tableName);
  }

  public Session getDataObject(String uid){
    // Sanity Check
    if(super.table == null){
      super.getAllObjects();
    }
    DataObject dObj = super.table.get(uid);
    Session s = new Session(this);
    s.putAll(dObj);
    s.setFileLocation(s.get("ownerID").toString() + "/" + tableName
                         + "/" + s.get("location").toString());
    return s;
  }

  public Session newSession(){
    String uid = GUID.BASE58();
    Session s = new Session(this);
    s.put("uid", uid);
    s.save();
    return s;
  }

  public String getTableName(){
    return super.tableName;
  }
}
