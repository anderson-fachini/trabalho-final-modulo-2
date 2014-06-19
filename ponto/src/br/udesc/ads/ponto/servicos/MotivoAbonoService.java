package br.udesc.ads.ponto.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.udesc.ads.ponto.entidades.MotivoAbono;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.util.JPAUtils;

public class MotivoAbonoService {
	
	private static MotivoAbonoService instance;
	
	private MotivoAbonoService() {}
	
	/**
	 * @return A instância da classe
	 */
	public static synchronized MotivoAbonoService get() {
		if (instance == null) {
			instance = new MotivoAbonoService();
		}
		
		return instance;
	}
	
	/**
	 * Método que obtém todos os motivos de abono cadastrados na base de dados
	 * @return
	 */
	public List<MotivoAbono> getMotivosAbono() {
		List<MotivoAbono> motivosAbono = new ArrayList<MotivoAbono>();
		
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<MotivoAbono> criteria = builder.createQuery(MotivoAbono.class);
		Root<MotivoAbono> root = criteria.from(MotivoAbono.class);
		criteria
			.select(root)
			.orderBy(builder.asc(root.get("descricao")));
		
		motivosAbono = entity.createQuery(criteria).getResultList();
		
		return motivosAbono;
	}
	
	/**
	 * Método que verifica se existe um motivo de abono cadastrado na base buscando pela sua descrição
	 * @param descricao Descrição do motivo de abono
	 * @return true caso o motivo de abono já exista
	 */
	public boolean checaExisteMotivoAbonoPelaDescicao(String descricao) {
		List<MotivoAbono> motivosAbono;
		
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<MotivoAbono> criteria = builder.createQuery(MotivoAbono.class);
		Root<MotivoAbono> root = criteria.from(MotivoAbono.class);
		criteria
			.select(root)
			.where(builder.equal(root.get("descricao"), descricao));
		
		motivosAbono = entity.createQuery(criteria).getResultList();
		
		return !motivosAbono.isEmpty();
	}
	
	/**
	 * Persiste um motivo de abono na base de dados
	 * @param motivoAbono
	 */
	public void persisteMotivoAbono(MotivoAbono motivoAbono) {
		JPAUtils.persisteObjetoNaBase(motivoAbono);
	}
}
