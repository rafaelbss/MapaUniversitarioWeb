package app.web.mu.config;

public enum Icon {

	MORADIA("/javax.faces.resource/img/home.png.xhtml?ln=mu");
	
	private String image;
	
	Icon(String image) {
		this.image = image;
	}
	
	public String getImage() {
		return this.image;
	}
}
