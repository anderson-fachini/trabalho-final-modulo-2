package br.udesc.ads.ponto.entidades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;

/**
 * Classe base para os testes de persistência.
 * @author Samuel
 *
 */
public abstract class PersistenceTest {

	protected EntityManager entityManager;
	private EntityTransaction transaction;

	@Before
	public void setup() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("ponto");
		this.entityManager = factory.createEntityManager();
		this.transaction = entityManager.getTransaction();
		transaction.begin();
	}

	@After
	public void tearDown() {
		transaction.rollback();
	}

}
