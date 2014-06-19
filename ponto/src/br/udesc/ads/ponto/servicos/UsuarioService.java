package br.udesc.ads.ponto.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.util.CriptografaSenha;

public class UsuarioService {
	
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
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> root = criteria.from(Usuario.class);
		
		Predicate[] filtros = {
			builder.like(builder.upper(root.<String>get("nomeUsuario")), nomeUsuario.toUpperCase()),
			builder.equal(root.get("senha"), CriptografaSenha.criptografa(senha)),
			builder.equal(root.get("situacao"), Situacao.ATIVO)
		};
		
		criteria
			.select(root)
			.where(filtros);
		
		try {
			return entity.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
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
			.where(builder.like(builder.upper(root.<String>get("nomeUsuario")), nomeUsuario.toUpperCase()));
		
		usuarios = entity.createQuery(criteria).getResultList();
		
		return !usuarios.isEmpty();
	}
	
	/**
	 * Método que persiste um usuário na base de dados
	 * @param usuario
	 */
	public void persisteUsuario(Usuario usuario) {
		EntityManager entity = Manager.get().getEntityManager();
		entity.persist(usuario);
	}
	
}
