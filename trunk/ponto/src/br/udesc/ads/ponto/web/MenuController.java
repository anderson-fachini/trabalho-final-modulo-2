package br.udesc.ads.ponto.web;

import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;

import br.udesc.ads.ponto.entidades.PerfilUsuario;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.util.Constants;
import br.udesc.ads.ponto.util.JsfUtils;

@ManagedBean(name = "menuController")
public class MenuController {
	
	Usuario usuarioAutenticado;
	
	public MenuController() {
		HttpSession session = (HttpSession) JsfUtils.getExternalContext().getSession(false);
		usuarioAutenticado = (Usuario) session.getAttribute(Constants.SESSION_USER_ATTR);
	}
	
	public Usuario getUsuarioAutenticado() {
		return usuarioAutenticado;
	}
	
	public String getNomeUsuarioAutenticado() {
		return usuarioAutenticado.getNomeUsuario();
	}
	
	public boolean isUsuarioAutenticadoGerente() {
		return usuarioAutenticado.getPerfil() == PerfilUsuario.GERENTE;
	}
	
	public boolean isUsuarioAutenticadoFuncionarioRH() {
		return usuarioAutenticado.getPerfil() == PerfilUsuario.FUNCIONARIO_RH;
	}
	
	public boolean isUsuarioAutenticadoDono() {
		return usuarioAutenticado.getPerfil() == PerfilUsuario.DONO_EMPRESA;
	}
	
	public String getClassePerfil() {		
		switch (usuarioAutenticado.getPerfil()) {
		case GERENTE:
			return "perfil-gerente";
		case FUNCIONARIO_RH:
			return "perfil-func-rh";
		default:
			return "perfil-dono";
		}
	}
}
