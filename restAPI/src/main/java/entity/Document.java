package entity;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {

	private int rid;
	private int patientId;
	private int therapistId;
	private String title;
	private String notes;
	private int[] records;
	@XmlJavaTypeAdapter(utils.TimeStampAdapter.class)
	private Timestamp creationdate = null;
	@XmlJavaTypeAdapter(utils.TimeStampAdapter.class)
	private Timestamp modifieddate = null;
	
	public Document() {
	}
	
	public Document(int patientId, int therapistId, String title, String notes, int[] records) {
		super();
		this.patientId = patientId;
		this.therapistId = therapistId;
		this.title = title;
		this.notes = notes;
		this.records = records;
	}
	
	public Document(int patientId, int therapistId, String title, String notes, int[] records, Timestamp creationdate,
			Timestamp modifieddate) {
		super();
		this.patientId = patientId;
		this.therapistId = therapistId;
		this.title = title;
		this.notes = notes;
		this.records = records;
		this.creationdate = creationdate;
		this.modifieddate = modifieddate;
	}

	public int getPatientId() {
		return patientId;
	}
	
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	
	public int getTherapistId() {
		return therapistId;
	}
	
	public void setTherapistId(int therapistId) {
		this.therapistId = therapistId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public int[] getRecords() {
		return records;
	}
	
	public void setRecords(int[] records) {
		this.records = records;
	}

	public Timestamp getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Timestamp creationdate) {
		this.creationdate = creationdate;
	}

	public Timestamp getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Timestamp modifieddate) {
		this.modifieddate = modifieddate;
	}
	
	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public void print() {
		System.out.println("PatientId: "+patientId);
		System.out.println("TherapistId: "+therapistId);
		System.out.println("Title: "+title);
		System.out.println("Notes: "+notes);
		System.out.println("Creationdate: "+creationdate);
		System.out.println("Modifieddate: "+modifieddate);
		System.out.print("Records:");
		for(int i : records) {
			System.out.print(i + ",");
		}
		System.out.print("Rid :" + rid);
		System.out.println();
	}
	
	
}
