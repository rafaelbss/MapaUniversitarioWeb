package app.web.mu.business.model;

public enum MarkerType {
	
	MORADIA("Moradia");
	
	private String type;
	
	MarkerType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
