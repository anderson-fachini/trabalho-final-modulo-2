package br.udesc.ads.ponto.populadores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.PerfilUsuario;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.servicos.UsuarioService;
import br.udesc.ads.ponto.util.CriptografaSenha;

public class PopuladorBasePadrao {

	public static void criarUsuarioAdmin() {
		criarUsuario("admin", "admin", PerfilUsuario.DONO_EMPRESA, null);
	}
	
	public static void criarUsuario(String nomeUsuario, String senha, PerfilUsuario perfil, Colaborador colaborador) {
		if (UsuarioService.get().checaUsuarioExistePorNome(nomeUsuario)) {
			System.out.println("Usuário '" + nomeUsuario + "' já existe. Ignorando...");
			return;
		}
		Usuario usuario = new Usuario();
		usuario.setNomeUsuario(nomeUsuario);
		usuario.setSenha(CriptografaSenha.criptografa(senha));
		usuario.setPerfil(perfil);
		usuario.setColaborador(colaborador);
		usuario.setSituacao(Situacao.ATIVO);
		UsuarioService.get().persisteUsuario(usuario);
	}

	public static void criarUsuariosParaOsGerentes() {
		EntityManager em = Manager.get().getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Setor> query = cb.createQuery(Setor.class);
		Root<Setor> root = query.from(Setor.class);
		List<Setor> setores = em.createQuery(query.select(root)).getResultList();
		for (Setor setor : setores) {
			Colaborador gerente = setor.getGerente();
			criarUsuario(gerente.getNome(), gerente.getNome(), PerfilUsuario.GERENTE, gerente);
		}
	}

	public static void main(String[] args) {
		criarUsuariosParaOsGerentes();
		// criarUsuarioAdmin();
	}

}
