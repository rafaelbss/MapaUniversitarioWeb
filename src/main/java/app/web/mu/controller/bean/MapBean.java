package app.web.mu.controller.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import app.web.mu.business.facade.MarkerFacade;
import app.web.mu.config.Config;
import app.web.mu.config.Keys;
import app.web.mu.jsf.util.Message;
import app.web.mu.business.model.MarkerType;
import app.web.mu.business.model.MoradiaMarker;
import app.web.mu.business.model.User;

/**
 * Controller responsável pelas ações na página Home.xhtml
 * @author santos.rafaelbs
 * @version 1.0.0
 */
@Named(value="mapBean")
@SessionScoped
public class MapBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//GMap Models
	private MapModel geoModel;
	private MapModel dialogGeoModel;
	
	//Atributos do componente GMap
	private String centerGeoMap;
	private String zoom;
	
	private Marker selectedMarker;
	private Marker selectedDialogMarker;
	
	//Marcadores de mapas to tipo MoradiaMarker
	private app.web.mu.business.model.Marker selectedMapMarker;
	private MoradiaMarker selectedDialogMapMarker;
	
	//Menu Model
	private MenuModel menuModel;
	
	private boolean ativarBotoes;
	
	private DefaultSubMenu markerRootMenu = new DefaultSubMenu("Moradias");
	
	private Map<Integer, app.web.mu.business.model.Marker> markerMap = new HashMap<>();

	@Inject
	private MarkerFacade mapMarkerFacade;

	@PostConstruct
    public void init() {
		this.inicializarDados();
    }
	
	public void selectMarker(String hash) {
		MoradiaMarker marker = (MoradiaMarker) markerMap.get(Integer.parseInt(hash));
		
		StringBuilder coordinates = new StringBuilder();
		coordinates.append(marker.getLatitude());
		coordinates.append(",");
		coordinates.append(marker.getLongitude());
		
	    this.centerGeoMap = coordinates.toString();
	    this.zoom = "20";
	}
	
	//Events e ActionListeners
	public void onGeocode(GeocodeEvent event) {
        List<GeocodeResult> results = event.getResults();
         
        if (results != null && !results.isEmpty()) {
            LatLng center = results.get(0).getLatLng();
            
            StringBuilder coordinates = new StringBuilder();
    		coordinates.append(center.getLat());
    		coordinates.append(",");
    		coordinates.append(center.getLng());
    		
            centerGeoMap = coordinates.toString();
        }
    }
	
	public void onMarkerSelect(OverlaySelectEvent event) {
        selectedMarker = (Marker) event.getOverlay();
        selectedMapMarker = (MoradiaMarker) selectedMarker.getData();
        
        this.checarDisponibilidadeBotoes(selectedMapMarker);
    }
	
	public void onDialogMarkerSelect(OverlaySelectEvent event) {
        selectedDialogMarker = (Marker) event.getOverlay();
        
        selectedDialogMapMarker = new MoradiaMarker();
        selectedDialogMapMarker.setLatitude(selectedDialogMarker.getLatlng().getLat());
        selectedDialogMapMarker.setLongitude(selectedDialogMarker.getLatlng().getLng());
    }
	
	public void addMarker() {
		Marker marker = new Marker(new LatLng(selectedDialogMapMarker.getLatitude(), 
											  selectedDialogMapMarker.getLongitude()), 
											  selectedDialogMapMarker.getNome(), 
											  selectedDialogMapMarker);
        dialogGeoModel.addOverlay(marker);
        
        RequestContext.getCurrentInstance().closeDialog(marker);
	}
	
	public void atualizarMarker() {
		if(this.selectedMapMarker != null) {
			this.mapMarkerFacade.salvar(this.selectedMapMarker);
			
			this.atualizarInformacoes(false);
			
			Message.info(false);
		}
	}
	
	public void removerMarker() {
		if(this.selectedMapMarker != null) {
			this.mapMarkerFacade.remover(this.selectedMapMarker);
			
			this.atualizarInformacoes(false);
			
			Message.info(true);
		}
	}
	
	//Ações realizadas ao salvar um novo Marker
	public void postSaveActions(SelectEvent event) {
		Marker marker = (Marker) event.getObject();
		app.web.mu.business.model.Marker mapMarker = (app.web.mu.business.model.Marker) marker.getData();
		
		//Recupera o usuário na sessão para salvar o marcador
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    	HttpSession session = (HttpSession) externalContext.getSession(true);
    	User user = (User)session.getAttribute("USER");
    	
    	if(user != null) {
        	mapMarker.setFacebookUser(user.getEmail());
    		
    		if(mapMarker != null) {
    			this.mapMarkerFacade.salvar(mapMarker);
    			
    			this.atualizarInformacoes(true);
    			
    			Message.info(false);
    			
    			this.limparInstancias();
    		}
    	} else {
    		Message.error(false);
    	}
	}
	
	//Getters/Setters
	public MapModel getGeoModel() {
		return geoModel;
	}

	public String getCenterGeoMap() {
		return centerGeoMap;
	}

	public Marker getSelectedMarker() {
		return selectedMarker;
	}

	public void setSelectedMarker(Marker selectedMarker) {
		this.selectedMarker = selectedMarker;
	}

	public app.web.mu.business.model.Marker getSelectedMapMarker() {
		return selectedMapMarker;
	}

	public void setSelectedMapMarker(MoradiaMarker selectedMapMarker) {
		this.selectedMapMarker = selectedMapMarker;
	}

	public MapModel getDialogGeoModel() {
		return dialogGeoModel;
	}

	public void setDialogGeoModel(MapModel dialogGeoModel) {
		this.dialogGeoModel = dialogGeoModel;
	}

	public Marker getSelectedDialogMarker() {
		return selectedDialogMarker;
	}

	public void setSelectedDialogMarker(Marker selectedDialogMarker) {
		this.selectedDialogMarker = selectedDialogMarker;
	}
	
	public app.web.mu.business.model.Marker getSelectedDialogMapMarker() {
		return selectedDialogMapMarker;
	}

	public void setSelectedDialogMapMarker(MoradiaMarker selectedDialogMapMarker) {
		this.selectedDialogMapMarker = selectedDialogMapMarker;
	}
	
	public MenuModel getMenuModel() {
		return menuModel;
	}

	public String getZoom() {
		return zoom;
	}

	public void setZoom(String zoom) {
		this.zoom = zoom;
	}
	
	public SelectItem[] getMarkerTypeValues() {
	    SelectItem[] items = new SelectItem[MarkerType.values().length];
	    
	    int i = 0;
	    
	    for(MarkerType g: MarkerType.values()) {
			if(g.getType().equals("Moradia"))
				items[i++] = new SelectItem(g, Config.getInternacionalization().getString(Keys.MORADIA_ENUM_VALUE.getValue()));
			else
				items[i++] = new SelectItem(g, Config.getInternacionalization().getString(Keys.RESTAURANT_ENUM_VALUE.getValue()));
	    }
	    
	    return items;
	}

	public boolean isAtivarBotoes() {
		return ativarBotoes;
	}

	//Métodos internos
	private void preencherMenu(app.web.mu.business.model.Marker marker, boolean isAtualizarMenu) {
		StringBuilder compactarNome = new StringBuilder();
		
		if(marker.getNome().length() > 20) {
			compactarNome.append(marker.getNome().substring(0, 20));
			compactarNome.append("...");
		}
		else {
			compactarNome.append(marker.getNome());
		}

		DefaultMenuItem markerItem = new DefaultMenuItem(compactarNome.toString());
    	
    	if(isAtualizarMenu)
    		marker.setId(System.currentTimeMillis());
    	
    	markerItem.setId(String.valueOf(marker.getId()));
    	markerItem.setIcon("fa  fa-fw  fa-home");
    	markerItem.setStyleClass("menu-item");
    	markerItem.setUpdate("geoMap");
    	
    	markerMap.put(marker.hashCode(), (MoradiaMarker)marker);
    	
    	String expression = String.format("#{mapBean.selectMarker('%s')}", marker.hashCode());
    	markerItem.setCommand(expression);
    	
        markerRootMenu.addElement(markerItem);
	}

	private void plotarMapa(app.web.mu.business.model.Marker marker, boolean aumentarZoom) {
		LatLng coordinates = new LatLng(marker.getLatitude(), marker.getLongitude());
    	Marker point = new Marker(coordinates, marker.getNome(), marker);
    	point.setIcon(Config.ICON_MORADIA);
    	
    	geoModel.addOverlay(point);
    	
    	if(aumentarZoom) {
        	centerGeoMap = point.getLatlng().getLat() + "," + point.getLatlng().getLng();
        	zoom = "20";
    	}
	}

	private void limparInstancias() {
		this.selectedDialogMapMarker = new MoradiaMarker();
		this.selectedDialogMarker = null;
		this.selectedMapMarker = new MoradiaMarker();
		this.selectedMarker = null;
		
		this.dialogGeoModel = new DefaultMapModel();
	}
	
	private void atualizarInformacoes(boolean isSalvando) {
		//Retorno da RESTFul API com a lista de marcadores do tipo Moradia disponíveis
		List<MoradiaMarker> markers = (List<MoradiaMarker>) this.mapMarkerFacade.buscarTodasMoradias();
		
		markerRootMenu = new DefaultSubMenu("Moradias");
		
		geoModel = new DefaultMapModel();
		
		for(app.web.mu.business.model.Marker marker : markers) {
			this.preencherMenu(marker, false);
			
			if(isSalvando)
				this.plotarMapa(marker, true);
			else
				this.plotarMapa(marker, false);
		}
		
		menuModel = new DefaultMenuModel();
		menuModel.addElement(markerRootMenu);
	}
	
	private void checarDisponibilidadeBotoes(app.web.mu.business.model.Marker marker) {
		//Recupera o usuário na sessão
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    	HttpSession session = (HttpSession) externalContext.getSession(true);
    	User user = (User)session.getAttribute("USER");
    	
    	if(user != null) {
    		if(marker.getFacebookUser().equals(user.getEmail()))
    			this.ativarBotoes = true;
    		else
    			this.ativarBotoes = false;
    	} else {
    		this.ativarBotoes = false;
    	}
	}
	
	private void inicializarDados() {
		menuModel = new DefaultMenuModel();
		
		//Retorno da RESTFul API com a lista de marcadores do tipo Moradia disponíveis
		List<MoradiaMarker> markers = (List<MoradiaMarker>) this.mapMarkerFacade.buscarTodasMoradias();
		
        geoModel = new DefaultMapModel();
        dialogGeoModel = new DefaultMapModel();
        
        //Define coordenadas centrais do mapa
        centerGeoMap = Config.getBundle().getString("coordinate.central");
        
        //Define o zoom do mapa
        zoom = "15";
        
        selectedDialogMapMarker = new MoradiaMarker();
        
        for(app.web.mu.business.model.Marker marker : markers) {
        	
        	//Plota no mapa todos os dados retornados
        	//da API RESTFul
        	this.plotarMapa(marker, false);
        	
        	//Preenche o menu com a lista de dados
        	//retornados da API RESTFul
        	this.preencherMenu(marker, false);
        }
        
        //Adiciona o menu com todos os dados ao Menu Model
        menuModel.addElement(markerRootMenu);
	}
}
