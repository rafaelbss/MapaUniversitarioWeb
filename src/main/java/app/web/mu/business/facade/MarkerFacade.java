package app.web.mu.business.facade;

import java.io.Serializable;
import java.util.Collection;

import javax.inject.Inject;

import app.web.mu.business.model.Marker;
import app.web.mu.business.model.MoradiaMarker;
import app.web.mu.config.ClientAPI;

public class MarkerFacade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ClientAPI api;
	
	public Collection<MoradiaMarker> buscarTodasMoradias() {
		return this.api.buscarTodasMoradias();
	}
	
	public void salvar(Marker marker) {
		if(marker.getId() == null)
			this.api.salvarMoradia((MoradiaMarker)marker);
		else
			this.api.atualizarMoradia((MoradiaMarker)marker);
	}
	
	public void remover(Marker marker) {
		this.api.removerMoradia((MoradiaMarker)marker);
	}
}
