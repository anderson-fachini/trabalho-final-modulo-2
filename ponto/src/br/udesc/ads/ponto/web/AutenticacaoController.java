package br.udesc.ads.ponto.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.servicos.UsuarioService;
import br.udesc.ads.ponto.util.Constants;
import br.udesc.ads.ponto.util.JsfUtils;

@ManagedBean(name = "autenticacaoController")
@ViewScoped
public class AutenticacaoController implements Serializable {

	private static final long serialVersionUID = 8338793297349233829L;

	private Usuario usuario, usuarioAutenticado;
	private HttpSession session;

	@PostConstruct
	public void init() {
		usuario = new Usuario();
	}

	@PreDestroy
	public void destroy() {
		// nothin to do
	}

	public void autenticaUsuario() {
		UsuarioService usuarioService = new UsuarioService();
		usuarioAutenticado = usuarioService.buscaUsuarioAutenticado(
				usuario.getNomeUsuario(), usuario.getSenha());

		if (usuarioAutenticado == null) {
			JsfUtils.addMensagemErro("Usuário ou senha iválido");
		} else {
			session = (HttpSession) JsfUtils.getExternalContext().getSession(false);
			session.setAttribute(Constants.SESSION_USER_ATTR, usuarioAutenticado);
			
			JsfUtils.redirectTo(Constants.PAGINA_INDEX);
		}

	}

	public void encerraSessao() {
		session = (HttpSession) JsfUtils.getExternalContext().getSession(false);
		session.setAttribute("usuarioAutenticado", null);
		session.invalidate();

		JsfUtils.redirectTo(Constants.PAGINA_LOGIN);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
