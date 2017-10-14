package entity;

import java.sql.Timestamp;
import utils.SystemConfig;

public class Data{
  private int rid = -1;
  private int uid = -1;
  private String type = "";
  private String subtype = "";
  private String title = "";
  private Timestamp creationdate = null;
  private Timestamp modifieddate = null;
  private String content = "";

	/**
	* Default empty Data constructor
	*/
	public Data() {
		super();
	}

	/**
	* Default Data constructor
	*/
	public Data(int rid, int uid, String type, String subtype, String title, Timestamp creationdate, Timestamp modifieddate, String content) {
		super();
		this.rid = rid;
		this.uid = uid;
		this.type = type;
		this.subtype = subtype;
		this.title = title;
		this.creationdate = creationdate;
		this.modifieddate = modifieddate;
		this.content = content;
	}

	/**
	* Returns value of rid
	* @return
	*/
	public int getRid() {
		return rid;
	}

	/**
	* Sets new value of rid
	* @param
	*/
	public void setRid(int rid) {
		this.rid = rid;
	}

	/**
	* Returns value of uid
	* @return
	*/
	public int getUid() {
		return uid;
	}

	/**
	* Sets new value of uid
	* @param
	*/
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	* Returns value of type
	* @return
	*/
	public String getType() {
		return type;
	}

	/**
	* Sets new value of type
	* @param
	*/
	public void setType(String type) {
		this.type = type;
	}

	/**
	* Returns value of subtype
	* @return
	*/
	public String getSubtype() {
		return subtype;
	}

	/**
	* Sets new value of subtype
	* @param
	*/
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	/**
	* Returns value of title
	* @return
	*/
	public String getTitle() {
		return title;
	}

	/**
	* Sets new value of title
	* @param
	*/
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	* Returns value of creationdate
	* @return
	*/
	public Timestamp getCreationdate() {
		return creationdate;
	}

	/**
	* Sets new value of creationdate
	* @param
	*/
	public void setCreationdate(Timestamp creationdate) {
		this.creationdate = creationdate;
	}

	/**
	* Returns value of modifieddate
	* @return
	*/
	public Timestamp getModifieddate() {
		return modifieddate;
	}

	/**
	* Sets new value of modifieddate
	* @param
	*/
	public void setModifieddate(Timestamp modifieddate) {
		this.modifieddate = modifieddate;
	}

	/**
	* Returns value of content
	* @return
	*/
	public String getContent() {
		return content;
	}

	/**
	* Sets new value of content
	* @param
	*/
	public void setContent(String content) {
		this.content = content;
	}

  public String getAbsolutePath(){
    if(!subtype.equalsIgnoreCase("heart")){
      return SystemConfig.getConfig("storage_directory") + "/" + uid + "/" + subtype + "/" + content;
    }
    return null;
  }
}
