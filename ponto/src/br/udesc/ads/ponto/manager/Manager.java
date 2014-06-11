package br.udesc.ads.ponto.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Manager {

	private static Manager instance;

	public static Manager get() {
		if (instance == null) {
			instance = new Manager();
		}
		return instance;
	}

	private EntityManager entityManager;

	private Manager() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("ponto");
		this.entityManager = factory.createEntityManager();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public String getUrlServicoLeitora() {
		return null; // TODO Parametrizar;
	}
	
	public int getTamanhoBlocoLeituraMarcacoes() {
		return 1000; // TODO Parametrizar;
	}

}
