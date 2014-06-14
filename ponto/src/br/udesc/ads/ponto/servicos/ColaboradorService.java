package br.udesc.ads.ponto.servicos;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.manager.Manager;

public class ColaboradorService {

	private static ColaboradorService instance;

	public static synchronized ColaboradorService get() {
		if (instance == null) {
			instance = new ColaboradorService();
		}
		return instance;
	}
	
	private ColaboradorService() {
	}

	/**
	 * Realiza a importação do arquivo de colaboradores para a base de dados.
	 * 
	 * @param arquivo
	 *            O arquivo a ser importado.
	 */
	public void importarColaboradores(File arquivo) {
		ImportadorColaboradores importador = new ImportadorColaboradores();
		importador.importar(arquivo);
	}

	public Colaborador getColaboradorPorCodigo(Long codCol, boolean nullSeNaoEncontrar) {
		EntityManager entityManager = Manager.get().getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Colaborador> criteria = builder.createQuery(Colaborador.class);
		Root<Colaborador> root = criteria.from(Colaborador.class);
		criteria.select(root).where(builder.equal(root.get("codigo"), codCol));
		try {
			return entityManager.createQuery(criteria).getSingleResult();
		} catch (NoResultException ex) {
			if (nullSeNaoEncontrar) {
				return null;
			}
			throw new RuntimeException(String.format("Nenhum colaborador encontrado com código '%d'.", codCol));
		}
	}

}
