package br.udesc.ads.ponto.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.udesc.ads.ponto.entidades.PerfilUsuario;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.entidades.Usuario;

@ManagedBean(name = "cadastroUsuarios")
@SessionScoped
public class CadastroUsuariosController {
	
	private Usuario usuarioSelecionado;
	
	private List<Usuario> usuarios;
	
	public CadastroUsuariosController() {
		usuarioSelecionado = new Usuario();
		usuarios = new ArrayList<Usuario>();
		
		Usuario usr = new Usuario();
		usr.setNomeUsuario("anderson");
		usr.setPerfil(PerfilUsuario.GERENTE);
		usr.setSituacao(Situacao.ATIVO);
		
		usuarios.add(usr);
		
		usr = new Usuario();
		usr.setNomeUsuario("fachini");
		usr.setPerfil(PerfilUsuario.FUNCIONARIO_RH);
		usr.setSituacao(Situacao.INATIVO);
		
		usuarios.add(usr);
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}
