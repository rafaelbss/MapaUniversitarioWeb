package app.web.mu.jsf.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import app.web.mu.config.Config;
import app.web.mu.config.Keys;

/**
 * Classe utilitária de mensagens do JSF
 * @author santos.rafaelbs
 *
 */
public class Message {
	
	/**
	 * Exibe uma mensagem de informação
	 * @param isRemover
	 */
	public static void info(boolean isRemover) {
		FacesContext context = FacesContext.getCurrentInstance();
		
		if(isRemover)
			context.addMessage(null, new FacesMessage(Config.getInternacionalization().
			getString(Keys.MESSAGE_MARKER_REMOVE_SUCCESS.getValue()),  ""));
		else
			context.addMessage(null, new FacesMessage(Config.getInternacionalization().
			getString(Keys.MESSAGE_MARKER_ADD_SUCCESS.getValue()),  ""));
	}
	
	/**
	 * Exibe uma mensagem de erro
	 * @param isRemover
	 */
	public static void error(boolean isRemover) {
		FacesContext context = FacesContext.getCurrentInstance();
		
		if(isRemover)
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Config.getInternacionalization().
			getString(Keys.MESSAGE_MARKER_REMOVE_ERROR.getValue()),  ""));
		else
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Config.getInternacionalization().
			getString(Keys.MESSAGE_MARKER_ADD_ERROR.getValue()),  ""));
	}
	
	/**
	 * Exibe uma mensagem de erro
	 * @param message
	 */
	public static void error(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
	}
}
