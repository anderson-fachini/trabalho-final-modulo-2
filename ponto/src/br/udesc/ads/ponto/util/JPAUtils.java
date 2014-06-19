package br.udesc.ads.ponto.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.udesc.ads.ponto.manager.Manager;

/**
 * Classe utilitária para trabalhar com JPA
 * @author anderson
 */
public class JPAUtils {
	
	/**
	 * Persiste um objeto na base de dados
	 * @param objeto
	 */
	public static void persisteObjetoNaBase(Object objeto) {
		EntityManager entity = Manager.get().getEntityManager();
		EntityTransaction transaction = entity.getTransaction();
		
		transaction.begin();
		entity.persist(objeto);
		transaction.commit();
	}
}
