package entity;

public class FetchRequest {
	private String path;
	private String filename;
	
	public FetchRequest() {
		
	}

	public FetchRequest(String path) {
		super();
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getFilename() {
		String[] split = path.split("/");
		return split[split.length-1];
	}
	
	
}
