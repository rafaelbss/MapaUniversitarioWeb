package app.web.mu.config;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import app.web.mu.business.model.MoradiaMarker;

/**
 * Classe com escopo de aplicação criada para consumir os métodos da API RESTFul
 * @author santos.rafaelbs
 * @version 1.0.0
 */
@ApplicationScoped
public class ClientAPI {
	
	private RestTemplate template;
	
	private String URI_BASE;
	private String URN_BASE = Config.getBundle().getString(Keys.API_CONTEXT_NAME.getValue());
	
	private final String URL = Config.getBundle().getString(Keys.BASE_DOMAIN_URL.getValue());
	
	public ClientAPI() {
		template = new RestTemplate();
		
		URI_BASE = URL.concat(URN_BASE);
	}
	
	/**
	 * URL RESTFul: /MapaUniversitarioAPI/markers/moradias [POST]
	 * @param marker
	 * @return
	 */
	public String salvarMoradia(MoradiaMarker marker) {
		try {
			RequestEntity<MoradiaMarker> request = RequestEntity
					.post(URI.create(URI_BASE + "/markers/moradias"))
					.body(marker);
			
			ResponseEntity<Void> response = template.exchange(request, Void.class);
			
			return response.getHeaders().getLocation().toString();
			
		} catch(Exception error) {
			throw error;
		}
	}
	
	/**
	 * URL RESTFul: /MapaUniversitarioAPI/markers/moradias/{id} [PUT]
	 * @param marker
	 */
	public void atualizarMoradia(MoradiaMarker marker) {
		try {
			RequestEntity<MoradiaMarker> request = RequestEntity
					.put(URI.create(URI_BASE + "/markers/moradias/" + marker.getId()))
					.body(marker);
			
			template.exchange(request, Void.class);
			
		} catch(Exception error) {
			throw error;
		}
	}
	
	/**
	 * URL RESTFul: /MapaUniversitarioAPI/markers/moradias/{id} [DELETE]
	 * @param marker
	 */
	public void removerMoradia(MoradiaMarker marker) {
		try {
			RequestEntity<Void> request = RequestEntity
					.delete(URI.create(URI_BASE + "/markers/moradias/" + marker.getId())).build();
			
			template.exchange(request, Void.class);
			
		} catch(Exception error) {
			throw error;
		}
	}
	
	/**
	 * URL RESTFul: /MapaUniversitarioAPI/markers/ [GET]
	 * @return
	 */
	public Collection<MoradiaMarker> buscarTodasMoradias() {
		try {
			
			RequestEntity<Void> request = RequestEntity.get(URI.create(URI_BASE + "/markers")).build();
			ResponseEntity<MoradiaMarker[]> response = template.exchange(request, MoradiaMarker[].class);
			return Arrays.asList(response.getBody());
			
		} catch(Exception error) {
			throw error;
		}
	}
}
