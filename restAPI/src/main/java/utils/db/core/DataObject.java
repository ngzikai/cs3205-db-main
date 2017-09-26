package utils.db.core;

import java.util.*;

import utils.db.*;

public class DataObject extends HashMap<String, Object>{
  private DataTable table = null;
  public DataObject(DataTable table){
    this.table = table;
  }

  public int save(){
    int result = table.update(this);
    return result;
  }

}
