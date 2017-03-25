package app.web.mu.persistence.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe que representa um produdor de EntityManager, com escopo de aplicação
 * @author santos.rafaelbs
 * @version 1.0.0
 */
@ApplicationScoped
public class EntityManagerProducer {
	
	private EntityManagerFactory emf;
	
	public EntityManagerProducer() {
		this.emf = Persistence.createEntityManagerFactory("MapaUniversitarioPU");
	}
	
	@Produces
	@RequestScoped
	public EntityManager createEntityManager() {
		return emf.createEntityManager();
	}
	
	public void closeEntityManager(@Disposes EntityManager em) {
		em.close();
	}
}
