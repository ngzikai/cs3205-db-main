package entity;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement 
public class Log {
	private int logId;
	private String api;
	private String classification;
	private Timestamp time;
	private int uid;
	private String description;
	
	public Log(String api, String classification, Timestamp time, int uid, String description) {
		super();
		this.api = api;
		this.classification = classification;
		this.time = time;
		this.uid = uid;
		this.description = description;
	}
	public Log(int logId, String api, String classification, Timestamp time, int uid, String description) {
		super();
		this.logId = logId;
		this.api = api;
		this.classification = classification;
		this.time = time;
		this.uid = uid;
		this.description = description;
	}
	public int getLogId() {
		return logId;
	}
	public void setLogId(int logId) {
		this.logId = logId;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
