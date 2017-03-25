package app.web.mu.jsf.util;

import java.io.IOException;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Classe utilizada para lidar com todas as exceções ocorridas no sistema
 * @author santos.rafaelbs
 * @version 1.0.0
 */
public class JsfExceptionHandler extends ExceptionHandlerWrapper {

	private static Log log = LogFactory.getLog(JsfExceptionHandler.class);
	
	private ExceptionHandler wrapped;
	
	public JsfExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}
	
	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}
	
	/**
	 * Método que lida com todas as exceções ocorridas e grava no log
	 */
	@Override
	public void handle() throws FacesException {
		Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();
		 
		while (events.hasNext()) {
			ExceptionQueuedEvent event = events.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
			
			Throwable exception = context.getException();
			HttpClientErrorException httpClientErrorException = getHttpClientErrorException(exception);
			Exception errorException = getException(exception);
			
			boolean handled = false;
			
			try {
				if (exception instanceof ViewExpiredException) {
					handled = true;
					
					redirect("/");
				} else if (httpClientErrorException != null) {
					handled = true;
					
					Message.error(httpClientErrorException.getMessage());
					log.error(httpClientErrorException.getMessage(), httpClientErrorException);
					
				} else if (errorException != null) {
					handled = true;
					
					Message.error(errorException.getMessage());
					log.error(errorException.getMessage(), errorException);
					
				} else {
					handled = true;
					
					Message.error(exception.getMessage());
					log.error(exception.getMessage(), exception);
				}
			} finally {
				if(handled)
					events.remove();
			}
		}
		
		getWrapped().handle();
	}
	
	private HttpClientErrorException getHttpClientErrorException(Throwable exception) {
		if (exception instanceof HttpClientErrorException) {
			return (HttpClientErrorException) exception;
		} else if (exception.getCause() != null) {
			return getHttpClientErrorException(exception.getCause());
		}
		
		return null;
	}
	
	private Exception getException(Throwable exception) {
		if (exception instanceof Exception) {
			return (Exception) exception;
		} else if (exception.getCause() != null) {
			return getException(exception.getCause());
		}
		
		return null;
	}
	
	private void redirect(String page) {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			String contextPath = externalContext.getRequestContextPath();
	
			externalContext.redirect(contextPath + page);
			facesContext.responseComplete();
			
		} catch (IOException e) {
			throw new FacesException(e);
		}
	}
}
