package br.udesc.ads.ponto.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.PerfilUsuario;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.util.CriptografaSenha;
import br.udesc.ads.ponto.util.StringUtils;

public class UsuarioService {

	private static UsuarioService instance;

	private UsuarioService() {
	}

	public static synchronized UsuarioService get() {
		if (instance == null) {
			instance = new UsuarioService();
		}
		return instance;
	}

	/**
	 * Verifica as credenciais do usuário na base de dados.
	 * 
	 * @param nomeUsuario
	 *            Nome do usuário
	 * @param senha
	 *            Senha do Usuário
	 * @return Objeto usuário, caso tenha encontradou ou então null
	 */
	public Usuario buscaUsuarioAutenticado(String nomeUsuario, String senha) {
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> root = criteria.from(Usuario.class);

		Predicate[] filtros = { builder.like(builder.upper(root.<String> get("nomeUsuario")), nomeUsuario.toUpperCase()),
				builder.equal(root.get("senha"), CriptografaSenha.criptografa(senha)), builder.equal(root.get("situacao"), Situacao.ATIVO) };

		criteria.select(root).where(filtros);

		try {
			return entity.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Retorna a lista de usuários do sistema.
	 * 
	 * @return
	 */
	public List<Usuario> getTodosUsuarios() {
		List<Usuario> usuarios = new ArrayList<Usuario>();

		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> root = criteria.from(Usuario.class);
		criteria.select(root).orderBy(builder.asc(root.get("nomeUsuario")));

		usuarios = entity.createQuery(criteria).getResultList();

		return usuarios;
	}

	/**
	 * Verifica se um usuário existe pelo nome.
	 * 
	 * @param nomeUsuario
	 *            Nome de usuário
	 * @return true caso o usuário exista
	 */
	public boolean checaUsuarioExistePorNome(String nomeUsuario) {
		List<Usuario> usuarios = new ArrayList<Usuario>();

		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder builder = entity.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
		Root<Usuario> root = criteria.from(Usuario.class);
		criteria.select(root).where(builder.like(builder.upper(root.<String> get("nomeUsuario")), nomeUsuario.toUpperCase()));

		usuarios = entity.createQuery(criteria).getResultList();

		return !usuarios.isEmpty();
	}

	/**
	 * Persiste um usuário na base de dados.
	 * 
	 * @param usuario
	 */
	public void persistirUsuario(Usuario usuario) {
		EntityManager entity = Manager.get().getEntityManager();
		EntityTransaction transaction = entity.getTransaction();

		transaction.begin();
		entity.persist(usuario);
		transaction.commit();
	}

	/**
	 * Busca um usuário pelo colaborador associado com o usuário.
	 * 
	 * @param colaborador
	 *            O colaborador associado.
	 * @return o usuário associado a este colaborador, ou null caso não encontre.
	 */
	public Usuario buscarPorColaborador(Colaborador colaborador) {
		EntityManager entity = Manager.get().getEntityManager();
		CriteriaBuilder cb = entity.getCriteriaBuilder();
		CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
		Root<Usuario> root = query.from(Usuario.class);
		query.select(root).where(cb.equal(root.get("colaborador"), colaborador));
		List<Usuario> result = entity.createQuery(query).getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	/**
	 * Cria um usuário para cada gerente de setor que ainda não possui usuário associado.<br>
	 * Os usuários são criados com nome de usuário e senha igual ao primeiro nome do gerente, sem acentos.<br>
	 */
	public void criarUsuariosParaOsGerentesNovos() {
		List<Setor> setores = SetorService.get().getSetores();
		for (Setor setor : setores) {
			Colaborador gerente = setor.getGerente();
			Usuario old = UsuarioService.get().buscarPorColaborador(gerente);
			if (old != null) {
				continue; // Esse cara já tem usuário.
			}
			String nomeUsuario = StringUtils.toUserName(gerente.getNome());
			Usuario usuario = new Usuario();
			usuario.setNomeUsuario(nomeUsuario);
			usuario.setSenha(CriptografaSenha.criptografa(nomeUsuario));
			usuario.setPerfil(PerfilUsuario.GERENTE);
			usuario.setColaborador(gerente);
			usuario.setSituacao(Situacao.ATIVO);
			persistirUsuario(usuario);
		}
	}

}
