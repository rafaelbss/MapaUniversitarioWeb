package app.web.mu.business.model;

import java.io.Serializable;

/**
 * Classe que representa uma entidade Marker
 * @author santos.rafaelbs
 * @version 1.0.0
 */
public class Marker implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String nome;
	
	private Double latitude;
	private Double longitude;
	
	private String telefone;
	
	private MarkerType tipo;
	
	private String facebookUser;
	
	public Marker() {}
	
	public Marker(String nome) {
		this.nome = nome;
	}
	
	public Marker(String nome, Double latitude, Double longitude) {
		this(nome);
		
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Marker(String nome, Double latitude, Double longitude, String telefone) {
		this(nome, latitude, longitude);
		
		this.telefone = telefone;
	}
	
	public Marker(String nome, Double latitude, Double longitude, String telefone, MarkerType tipo) {
		this(nome, latitude, longitude, telefone);
		
		this.tipo = tipo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public MarkerType getTipo() {
		return tipo;
	}

	public void setTipo(MarkerType tipo) {
		this.tipo = tipo;
	}

	public String getFacebookUser() {
		return facebookUser;
	}

	public void setFacebookUser(String facebookUser) {
		this.facebookUser = facebookUser;
	}

	@Override
	public String toString() {
		return this.nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Marker))
			return false;
		Marker other = (Marker) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
}
