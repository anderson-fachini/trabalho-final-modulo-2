package br.udesc.ads.ponto.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.util.CriptografaSenha;
import br.udesc.ads.ponto.util.EntityManagerUtil;

public class UsuarioService {
	
	private static EntityManager em = EntityManagerUtil.getEntityManager();
	
	private static UsuarioService instance;
	
	private UsuarioService() {}
	
	public static synchronized UsuarioService get() {
		if (instance == null) {
			instance = new UsuarioService();
		}
		return instance;
	}
	
	/**
	 * Método que verifica as credenciais do usuário na base de dados
	 * @param nomeUsuario Nome do usuário
	 * @param senha Senha do Usuário
	 * @return Objeto usuário, caso tenha encontradou ou então null
	 */
	public Usuario buscaUsuarioAutenticado(String nomeUsuario, String senha) {
		Query query = em.createNamedQuery("checkUserAutentication");
		query.setParameter("nomeUsuario", nomeUsuario);
		query.setParameter("senha", CriptografaSenha.criptografa(senha));
		List<Usuario> usuarios = (List<Usuario>) query.getResultList();
		
		if (usuarios != null && usuarios.size() > 0) {
			return usuarios.get(0);
		}
		
		return null;
	}
	
	/**
	 * Método que retorna a lista de usuários do sistema
	 * @return
	 */
	public List<Usuario> getTodosUsuarios() {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> root = criteria.from(Usuario.class);
		criteria
			.select(root)
			.orderBy(builder.asc(root.get("nomeUsuario")));
		
		usuarios = entity.createQuery(criteria).getResultList();
		
		return usuarios;
	}
	
	/**
	 * Métod que verifica se um usuário existe pelo nome
	 * @param nomeUsuario Nome de usuário
	 * @return true caso o usuário exista
	 */
	public boolean checaUsuarioExistePorNome(String nomeUsuario) {		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> root = criteria.from(Usuario.class);
		criteria
			.select(root)
			.where(builder.equal(root.get("nomeUsuario"), nomeUsuario));
		
		usuarios = entity.createQuery(criteria).getResultList();
		
		return !usuarios.isEmpty();
	}
	
}
