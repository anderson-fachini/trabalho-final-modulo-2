package br.udesc.ads.ponto.servicos;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.joda.time.LocalDate;

import br.udesc.ads.ponto.entidades.Feriado;
import br.udesc.ads.ponto.manager.Manager;

public class FeriadoService {

	private static FeriadoService instance;

	public static synchronized FeriadoService get() {
		if (instance == null) {
			instance = new FeriadoService();
		}
		return instance;
	}

	private FeriadoService() {
	}

	/**
	 * Realiza a importação do arquivo de feriados para a base de dados.
	 * 
	 * @param arquivo
	 *            O arquivo a ser importado.
	 */
	public void importarFeriados(File arquivo) {
		new ImportadorFeriados().importar(arquivo);
	}

	/**
	 * Verifica se uma data específica está maracada como feriado ou não.
	 * 
	 * @param data
	 *            A data a ser verificada.
	 * @return <code>true</code> se esta data é feriado. Caso contrário,
	 *         <code>false</code>.
	 */
	public boolean existeFeriado(LocalDate data) {
		EntityManager entityManager = Manager.get().getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Feriado> query = cb.createQuery(Feriado.class);
		Root<Feriado> root = query.from(Feriado.class);
		query.select(root).where(cb.equal(root.get("data"), data));
		return !entityManager.createQuery(query).getResultList().isEmpty();
	}

}
