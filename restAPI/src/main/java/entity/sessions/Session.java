package entity.sessions;

import utils.db.core.*;

public class Session extends DataObject{

  private String fileLocation = "";
  public Session(DataTable table){
    super(table);
  }

  public void setFileLocation(String location){
    fileLocation = location;
  }

  public String getFileLocation(){
    return fileLocation;
  }
}
