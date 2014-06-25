package br.udesc.ads.ponto.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.manager.Manager;

/**
 * Class 
 * @author anderson
 *
 */
public class SetorService {
	
	private static SetorService instance;
	
	private SetorService(){}
	
	public static synchronized SetorService get() {
		if (instance == null) {
			instance = new SetorService();
		}
		
		return instance;
	}
	
	/**
	 * Retorna a lista de setores ativos.
	 * @return
	 */
	public List<Setor> getSetores() {
		List<Setor> setores = new ArrayList<Setor>();
		
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<Setor> criteria = builder.createQuery(Setor.class);
		Root<Setor> root = criteria.from(Setor.class);
		criteria
			.select(root)
			.where(builder.equal(root.get("situacao"), Situacao.ATIVO))
			.orderBy(builder.asc(root.get("nome")));
		
		setores = entity.createQuery(criteria).getResultList();
		
		return setores;
	}
	
	/**
	 * Obtem um setor pelo seu id.
	 * @param id
	 * @return Setor caso seja encontrado ou então null
	 */
	public Setor getById(long id) {		
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<Setor> criteria = builder.createQuery(Setor.class);
		Root<Setor> root = criteria.from(Setor.class);
		criteria
			.select(root)
			.where(builder.equal(root.get("id"), id));
		
		try {
			return entity.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
