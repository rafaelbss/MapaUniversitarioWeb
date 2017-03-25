package app.web.mu.config;

import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Classe com escopo de aplicação que armazena e inicializa variáveis e contextos como Resource Bundles
 * @author santos.rafaelbs
 * @version 1.0.0
 */
@ApplicationScoped
public class Config {
	
	public static final String APPLICATION_URL;
	public static final String ICON_MORADIA;
	
	public static final String FACEBOOK_APP_ID = Config.getBundle().getString(Keys.FACEBOOK_APP_ID.getValue());
	public static final String FACEBOOK_APP_SECRET = Config.getBundle().getString(Keys.FACEBOOK_APP_SECRET.getValue());
	
	public static final String REDIRECT_TO_CURRENT_URL = ((HttpServletRequest) FacesContext.getCurrentInstance().
																			   			    getExternalContext().
																		   			   	    getRequest()).getRequestURL().toString();
	static {
		StringBuilder imageUrlAppender = new StringBuilder();
		
		
		APPLICATION_URL = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString().replaceAll("/home.xhtml", "");
		
		imageUrlAppender.append(APPLICATION_URL);
		imageUrlAppender.append(Icon.MORADIA.getImage());
		
		ICON_MORADIA = imageUrlAppender.toString();
	}
	
	public static ResourceBundle getBundle() {
		FacesContext context = FacesContext.getCurrentInstance();
	    return context.getApplication().evaluateExpressionGet(context, "#{app}", ResourceBundle.class);
	}
	
	public static ResourceBundle getInternacionalization() {
		FacesContext context = FacesContext.getCurrentInstance();
	    return context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
	}
}
