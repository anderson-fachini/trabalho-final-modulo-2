package br.udesc.ads.ponto.servicos;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.joda.time.LocalDateTime;

import br.udesc.ads.ponto.entidades.AjusteBH;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.servicos.impl.ImportadorColaboradores;

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

	/**
	 * Realiza um ajuste no saldo do Banco de Horas de um colaborador.
	 * 
	 * @param colaborador
	 *            O colaborador que terá o Banco de Horas ajustado.
	 * @param valorAjuste
	 *            O valor do incremento (positivo) ou decremento (negativo) a
	 *            ser feito.
	 * @param responsavel
	 *            O usuário responsável por este ajuste.
	 * @param observacoes
	 *            As observações/justificativas do ajuste (obrigatório).
	 */
	public void ajustarBancoHoras(Colaborador colaborador, double valorAjuste, Usuario responsavel, String observacoes) {
		if (responsavel == null) {
			throw new IllegalArgumentException("É obrigatório informar um usuário responsável pelo ajuste.");
		}
		if (observacoes == null || observacoes.trim().isEmpty()) {
			throw new IllegalArgumentException("A observação do ajuste é obrigatória e não pode estar vazia.");
		}

		EntityManager entityManager = Manager.get().getEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			AjusteBH ajusteBH = new AjusteBH();
			ajusteBH.setColaborador(colaborador);
			ajusteBH.setDataHora(LocalDateTime.now());
			ajusteBH.setResponsavel(responsavel);
			ajusteBH.setSaldoAnterior(colaborador.getSaldoBH());
			ajusteBH.setValorAjuste(valorAjuste);
			ajusteBH.setObservacoes(observacoes);
			entityManager.persist(ajusteBH);

			colaborador.setSaldoBH(colaborador.getSaldoBH() + valorAjuste);
			entityManager.merge(colaborador);

			transaction.commit();
		} catch (Throwable ex) {
			transaction.rollback();
			throw ex;
		}
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
	
	/**
	 * @param setor O setor a ser filtrado, ou null para buscar de todos os setores.
	 * @return A lista de colaboradores.
	 */
	public List<Colaborador> getColaboradoresAtivosPorSetor(Setor setor) {
		EntityManager entityManager = Manager.get().getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Colaborador> query = cb.createQuery(Colaborador.class);
		Root<Colaborador> root = query.from(Colaborador.class);
		query.select(root).where(cb.equal(root.get("situacao"), Situacao.ATIVO));
		if (setor != null) {
			query.where(cb.equal(root.get("setor"), setor));
		}
		query.orderBy(cb.asc(root.get("nome")));
		return entityManager.createQuery(query).getResultList();
	}

	public List<Colaborador> getColaboradoresAtivos() {
		EntityManager entityManager = Manager.get().getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Colaborador> query = cb.createQuery(Colaborador.class);
		Root<Colaborador> root = query.from(Colaborador.class);
		query.select(root).where(cb.equal(root.get("situacao"), Situacao.ATIVO));
		query.orderBy(cb.asc(root.get("codigo")));
		return entityManager.createQuery(query).getResultList();
	}
	
}
