package br.udesc.ads.ponto.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerUtil {
	
	private static EntityManager entityManager;
	
	public static EntityManager getEntityManager() {
		if (entityManager == null) {
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("ponto");
			entityManager = factory.createEntityManager();
		}
		
		return entityManager;
	}
	
}
