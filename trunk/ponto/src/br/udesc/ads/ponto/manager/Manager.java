package br.udesc.ads.ponto.manager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.Config;
import br.udesc.ads.ponto.entidades.DiaSemana;
import br.udesc.ads.ponto.entidades.Escala;
import br.udesc.ads.ponto.entidades.EscalaMarcacao;

/**
 * Classe principal que cont�m as configura��es do ambiente, conex�o com o banco
 * e etc.
 * 
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

	public Config getConfig() {
		Config config = lerConfigBanco();
		if (config == null) {
			config = criarConfiguracaoPadrao();
		}
		return config;
	}

	private Config criarConfiguracaoPadrao() {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			Config result = new Config();
			result.setEscalaPadrao(criarEscalaPadrao());
			result.setIntervaloMaximoTrabalho(60 * 6);
			result.setIntervaloMinimoAlmoco(60);
			result.setIntervaloMinimoInterjornadas(60 * 11);
			result.setIntervaloMinimoIntrajornada(15);
			result.setMargemHorasFaltas(20);
			result.setMargemMarcacoes(5);
			entityManager.persist(result);
			transaction.commit();
			return result;
		} catch (Throwable ex) {
			transaction.rollback();
			throw ex;
		}
	}

	private Escala criarEscalaPadrao() {
		Escala escala = new Escala();
		for (DiaSemana dia : DiaSemana.getDiasUteisPadrao()) {
			escala.addMarcacao(new EscalaMarcacao(dia, new LocalTime(8, 0)));
			escala.addMarcacao(new EscalaMarcacao(dia, new LocalTime(12, 0)));
			escala.addMarcacao(new EscalaMarcacao(dia, new LocalTime(13, 0)));
			escala.addMarcacao(new EscalaMarcacao(dia, new LocalTime(17, 0)));
		}
		entityManager.persist(escala);
		return escala;
	}

	private Config lerConfigBanco() throws AssertionError {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Config> query = cb.createQuery(Config.class);
		query.select(query.from(Config.class));
		List<Config> results = entityManager.createQuery(query).getResultList();
		if (results.isEmpty()) {
			return null;
		}
		if (results.size() > 1) {
			throw new AssertionError("N�o deveria haver mais de um registro na tabela Config.");
		}
		return results.get(0);
	}

}
