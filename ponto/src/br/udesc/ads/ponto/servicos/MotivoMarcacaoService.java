package br.udesc.ads.ponto.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.udesc.ads.ponto.entidades.MotivoMarcacao;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.util.JPAUtils;

public class MotivoMarcacaoService {
	
	private static MotivoMarcacaoService instance;
	
	private MotivoMarcacaoService() {}
	
	/**
	 * @return A instância da classe
	 */
	public static synchronized MotivoMarcacaoService get() {
		if (instance == null) {
			instance = new MotivoMarcacaoService();
		}
		
		return instance;
	}
	
	/**
	 * Método que obtém todos os motivos de marcação cadastrados na base de dados
	 * @return
	 */
	public List<MotivoMarcacao> getMotivosMarcacao() {
		List<MotivoMarcacao> motivosMarcacao = new ArrayList<MotivoMarcacao>();
		
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<MotivoMarcacao> criteria = builder.createQuery(MotivoMarcacao.class);
		Root<MotivoMarcacao> root = criteria.from(MotivoMarcacao.class);
		criteria
			.select(root)
			.orderBy(builder.asc(root.get("descricao")));
		
		motivosMarcacao = entity.createQuery(criteria).getResultList();
		
		return motivosMarcacao;
	}
	
	/**
	 * Método que verifica se existe um motivo de marcação cadastrado na base buscando pela sua descrição
	 * @param descricao Descrição do motivo de marcação
	 * @return true caso o motivo de marcação já exista
	 */
	public boolean checaExisteMotivoMarcacaoPelaDescicao(String descricao) {
		List<MotivoMarcacao> motivosMarcacao;
		
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<MotivoMarcacao> criteria = builder.createQuery(MotivoMarcacao.class);
		Root<MotivoMarcacao> root = criteria.from(MotivoMarcacao.class);
		criteria
			.select(root)
			.where(builder.equal(root.get("descricao"), descricao));
		
		motivosMarcacao = entity.createQuery(criteria).getResultList();
		
		return !motivosMarcacao.isEmpty();
	}
	
	/**
	 * Persiste um motivo de marcacao na base de dados
	 * @param motivoMarcacao
	 */
	public void persisteMotivoMarcacao(MotivoMarcacao motivoMarcacao) {
		JPAUtils.persisteObjetoNaBase(motivoMarcacao);
	}
}
