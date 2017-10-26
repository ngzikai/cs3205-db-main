package entity;

public class Treatment {
	private int id;
	private int patientId;
	private int therapistId;
	private boolean status;
	private boolean currentConsent;
	private boolean futureConsent;
	
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

	public Treatment(int id, int patientId, int therapistId, boolean status, boolean currentConsent,
			boolean futureConsent) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.therapistId = therapistId;
		this.status = status;
		this.currentConsent = currentConsent;
		this.futureConsent = futureConsent;
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

	public boolean isCurrentConsent() {
		return currentConsent;
	}

	public void setCurrentConsent(boolean currentConsent) {
		this.currentConsent = currentConsent;
	}

	public boolean isFutureConsent() {
		return futureConsent;
	}

	public void setFutureConsent(boolean futureConsent) {
		this.futureConsent = futureConsent;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
