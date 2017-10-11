package entity;

public class Treatment {
	private int id;
	private int patientId;
	private int therapistId;
	private boolean status;
	
	public Treatment(int id, int patientId, int therapistId, boolean status) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.therapistId = therapistId;
		this.status = status;
	}
	
	public Treatment(int patientId, int therapistId, boolean status) {
		super();
		this.patientId = patientId;
		this.therapistId = therapistId;
		this.status = status;
	}

	public Treatment() {
	}

	public boolean isStatus() {
		return status;
	}
	
	public boolean setStatus() {
		status = !status;
		return status;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
	
	
}
