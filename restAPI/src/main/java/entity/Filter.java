package entity;

import java.util.ArrayList;

public class Filter {
	private String id;
	private String parent_id;
	private String key;
	private String value;
	private String isset;
	private String type;
	private ArrayList<Filter> children;
	
	public Filter() {
		
	}

	public Filter(String id, String parent_id, String key, String value, String isset, String type,
			ArrayList<Filter> children) {
		super();
		this.id = id;
		this.parent_id = parent_id;
		this.key = key;
		this.value = value;
		this.isset = isset;
		this.type = type;
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIsset() {
		return isset;
	}

	public void setIsset(String isset) {
		this.isset = isset;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<Filter> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Filter> children) {
		this.children = children;
	}

}
