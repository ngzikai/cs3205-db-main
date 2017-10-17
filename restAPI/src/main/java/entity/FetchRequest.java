package entity;

public class FetchRequest {
	private String pathPrefix = "/home/sadm/tmp/";
	private String path;
	private String filename;
	
	public FetchRequest() {
		
	}

	public FetchRequest(String path) {
		super();
		this.path = path;
	}

	public String getPath() {
		String[] split = path.split("/home/sadm/tmp/");
		return split[split.length-1];
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getFilename() {
		String[] split = path.split("/");
		return split[split.length-1];
	}
	
	
}
