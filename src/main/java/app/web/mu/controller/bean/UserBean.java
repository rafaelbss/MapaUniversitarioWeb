package app.web.mu.controller.bean;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;

import org.primefaces.context.RequestContext;

import app.web.mu.business.model.User;
import app.web.mu.business.model.util.SocialUtil;
import app.web.mu.config.Config;
import app.web.mu.config.Keys;

/**
 * Bean que gerencia as operações de usuário na tela
 * @author santos.rafaelbs
 * @version 1.0.0
 */
@Named(value="userBean")
@SessionScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SocialAuthManager manager;
	
	private String originalURL;
	private String providerID;
	private String trigger;
	
	private User user;
	
	/**
	 * Inicia a operação para autenticar via facebook
	 * @return Retorna a URL de autenticação do facebook
	 * @throws Exception
	 */
	public String socialConnect() throws Exception {
		try {
				if((trigger != null) && (trigger.equals("header"))) {
					Properties props = System.getProperties();
					props.put(Config.getBundle().getString(Keys.FACEBOOK_APP_ID_URL.getValue()), Config.FACEBOOK_APP_ID);
					props.put(Config.getBundle().getString(Keys.FACEBOOK_APP_SECRET_URL.getValue()), Config.FACEBOOK_APP_SECRET);
					props.put(Config.getBundle().getString(Keys.FACEBOOK_PERMISSIONS_URL.getValue()), 
					Config.getBundle().getString(Keys.FACEBOOK_PERMISSIONS.getValue()));
		
					SocialAuthConfig config = SocialAuthConfig.getDefault();
					config.load(props);
					
					manager = new SocialAuthManager();
					manager.setSocialAuthConfig(config);
					
					trigger = null;
					
					String authenticationURL = manager.getAuthenticationUrl(providerID, Config.REDIRECT_TO_CURRENT_URL);
					
					FacesContext.getCurrentInstance().getExternalContext().redirect(authenticationURL);
				}
				
		} catch (Exception e) {
			throw e;
		}
		
		return Config.getBundle().getString(Keys.HOME_OUTCOME_REDIRECT.getValue());
	}

	/**
	 * Obtém os dados do usuário autenticado no facebook e faz o parse do objeto JSON recuperando o usuário da sessão
	 * @param event
	 */
	public void fillUser(ComponentSystemEvent event) {
		if(providerID != null)
			try {
					HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
					String parameter = request.getParameter("code");
					System.out.println("Parameter: " + parameter);
		
					if (parameter != null) {
						SocialUtil fb = new SocialUtil();
						
						// Intepreta da url
						fb.encode(parameter, "GET");
						user = fb.authFacebookLogin();
						
						//Recupera o usuário na sessão
						ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			        	HttpSession session = (HttpSession) externalContext.getSession(true);
			        	user = (User)session.getAttribute("USER");
			        	
			        	providerID = null;
					}
					
			} catch (Exception ex) {
				ex.getCause().printStackTrace();
			}
	}
	
	/**
	 * Remove o usuário logado da sessão
	 */
	public void logout() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    	HttpSession session = (HttpSession) externalContext.getSession(true);
    	session.removeAttribute("USER");
		
		this.user = new User();
		this.user.setLogged(false);
	}
	
	/**
	 * Abre um novo DialogBox para inserir um novo Marker
	 */
	public void addMarker() {
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("width", 640);
        options.put("height", 430);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");
        options.put("responsive", true);
        
        RequestContext.getCurrentInstance().openDialog("add-marker", options, null);
    }

	public String getOriginalURL() {
		return originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

	public String getProviderID() {
		return providerID;
	}

	public void setProviderID(String providerID) {
		this.providerID = providerID;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
}