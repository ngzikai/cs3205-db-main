package controller;

import utils.db.core.*;
import java.util.*;

public class SessionController {

  private DataTable sessionTable = null;

  public SessionController(String tableName){
    sessionTable = new DataTable(tableName);
  }

  public void saveSession(DataObject session){
    session.save();
  }

  public DataObject getSessionObject(String id){
    return sessionTable.getDataObject(id);
  }

  public List<DataObject> getAllObjects(){
    return sessionTable.getAllObjects();
  }

}
