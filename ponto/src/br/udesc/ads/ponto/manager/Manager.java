package br.udesc.ads.ponto.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe principal que contém as configurações do ambiente, conexão com o banco e etc.
 * @author Samuel
 *
 */
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
		return 64; // TODO Parametrizar;
	}

}
