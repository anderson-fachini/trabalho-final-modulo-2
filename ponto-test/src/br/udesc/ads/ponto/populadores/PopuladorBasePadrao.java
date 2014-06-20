package br.udesc.ads.ponto.populadores;

import br.udesc.ads.ponto.entidades.PerfilUsuario;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.servicos.UsuarioService;
import br.udesc.ads.ponto.util.CriptografaSenha;

public class PopuladorBasePadrao {

	public static void criarUsuarioAdmin() {
		Usuario usuario = new Usuario();
		usuario.setNomeUsuario("admin");
		usuario.setSenha(CriptografaSenha.criptografa("admin"));
		usuario.setPerfil(PerfilUsuario.DONO_EMPRESA);
		usuario.setSituacao(Situacao.ATIVO);
		UsuarioService.get().persisteUsuario(usuario);
	}
	
	public static void main(String[] args) {
		criarUsuarioAdmin();
	}
	
}
