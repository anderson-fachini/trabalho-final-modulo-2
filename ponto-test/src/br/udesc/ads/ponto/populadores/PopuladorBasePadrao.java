package br.udesc.ads.ponto.populadores;

import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.PerfilUsuario;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.servicos.UsuarioService;
import br.udesc.ads.ponto.servicos.impl.ImportadorMarcacoes;
import br.udesc.ads.ponto.util.CriptografaSenha;

public class PopuladorBasePadrao {

	public static void criarUsuarioAdmin() {
		criarUsuario("admin", "admin", PerfilUsuario.DONO_EMPRESA, null);
	}

	public static void criarUsuario(String nomeUsuario, String senha, PerfilUsuario perfil, Colaborador colaborador) {
		Usuario usuario = new Usuario();
		usuario.setNomeUsuario(nomeUsuario);
		usuario.setSenha(CriptografaSenha.criptografa(senha));
		usuario.setPerfil(perfil);
		usuario.setColaborador(colaborador);
		usuario.setSituacao(Situacao.ATIVO);
		UsuarioService.get().persistirUsuario(usuario);
	}

	public static void resetarMarcacoes() {
		new ImportadorMarcacoes().resetarLeitora();
	}

	public static void main(String[] args) {
		criarUsuarioAdmin();

		// resetarMarcacoes();
		// ApuracaoService.get().importarMarcacoes();
		// ApuracaoService.get().apurarMarcacoesPendentes();
	}

}
