package br.udesc.ads.ponto.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import br.udesc.ads.ponto.entidades.PerfilUsuario;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.servicos.SetorService;
import br.udesc.ads.ponto.servicos.UsuarioService;
import br.udesc.ads.ponto.util.CriptografaSenha;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "cadastroUsuarios")
@SessionScoped
public class CadastroUsuariosController implements Serializable {
	
	private static final long serialVersionUID = 5613229720100801008L;

	private Usuario usuarioSelecionado;
	
	private boolean popupOpened = false;
	
	private String popupTitle;	
	private String confirmarSenha;
	private String perfilUsuario;
	private String situacaoUsuario;
	private String senhaOriginal;
	
	private List<SelectItem> perfisUsuario;
	private List<SelectItem> situacoesUsuario;	
	private List<Usuario> usuarios;
	private List<Setor> setores;
	
	public CadastroUsuariosController() {
		resetaDados();
		
		usuarioSelecionado = new Usuario();
		buscaTodosUsuarios();
		setores = SetorService.get().getSetores();
		
		carregaPerfisUsuario();
		carregaSituacoesUsuario();
	}
	
	private void resetaDados() {
		confirmarSenha = "";
		popupTitle = Messages.getString("novoUsuario");
		perfilUsuario = PerfilUsuario.GERENTE.getId();
		situacaoUsuario = Situacao.ATIVO.getId();
		senhaOriginal = "";
	}
	
	public void salvarUsuario() {
		boolean temErros = validaFormCadastroUsuario();
		
		if (!temErros) {
			verificaAlterouAtributosUsuario();
			UsuarioService.get().persisteUsuario(usuarioSelecionado);
			
			buscaTodosUsuarios();
		}
	}
	
	private boolean validaFormCadastroUsuario() {
		boolean temErros = false;
		
		if (UsuarioService.get().checaUsuarioExistePorNome(usuarioSelecionado.getNomeUsuario())) {
			temErros = true;
			JsfUtils.addMensagemWarning(Messages.getString("msgUsuarioJaExiste"));
		}
		if (usuarioSelecionado.getId() == null && usuarioSelecionado.getSenha().length() == 0) {
			temErros = true;
			JsfUtils.addMensagemWarning(Messages.getString("msgInformeSenha"));
		}
		if (!usuarioSelecionado.getSenha().equals(confirmarSenha)) {
			temErros = true;
			JsfUtils.addMensagemWarning(Messages.getString("msgSenhasNaoConferem"));
		}
		
		return temErros;
	}
	
	private void verificaAlterouAtributosUsuario() {		
		if (!confirmarSenha.isEmpty()) {			
			String senhaCriptografada = CriptografaSenha.criptografa(confirmarSenha);
			
			if (senhaCriptografada != senhaOriginal) {
				usuarioSelecionado.setSenha(senhaCriptografada);
			}
		}
		
		if (usuarioSelecionado.getPerfil().getId() != perfilUsuario) {
			usuarioSelecionado.setPerfil(PerfilUsuario.fromId(perfilUsuario));
		}
		
		if (usuarioSelecionado.getSituacao().getId() != situacaoUsuario) {
			usuarioSelecionado.setSituacao(Situacao.fromId(situacaoUsuario));
		}
	}
	
	public void editarUsuario(Usuario usuario) {
		popupTitle = Messages.getString("editarUsuario");
		
		usuarioSelecionado = usuario;
		senhaOriginal = usuarioSelecionado.getSenha();		
		usuarioSelecionado.setSenha("");
		
		perfilUsuario = usuarioSelecionado.getPerfil().getId();
		situacaoUsuario = usuarioSelecionado.getSituacao().getId();
		popupOpened = true;
	}
	
	public void togglePopupOpened() {
		popupOpened = !popupOpened;
		resetaDados();
	}
	
	private void carregaPerfisUsuario() {
		perfisUsuario = new ArrayList<SelectItem>();
		
		for (PerfilUsuario p : PerfilUsuario.values()) {
			perfisUsuario.add(new SelectItem(p.getId(), p.getDescricao()));
		}
	}
	
	private void carregaSituacoesUsuario() {
		situacoesUsuario = new ArrayList<SelectItem>();
		
		for (Situacao s : Situacao.values()) {
			situacoesUsuario.add(new SelectItem(s.getId(), s.getDescricao()));
		}
	}
	
	private void buscaTodosUsuarios() {
		usuarios = UsuarioService.get().getTodosUsuarios();
	}
	
	public List<SelectItem> getPerfisUsuario() {
		return perfisUsuario;
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

	public boolean isPopupOpened() {
		return popupOpened;
	}

	public void setPopupOpened(boolean popupOpened) {
		this.popupOpened = popupOpened;
	}

	public String getPopupTitle() {
		return popupTitle;
	}

	public void setPopupTitle(String popupTitle) {
		this.popupTitle = popupTitle;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	public String getPerfilUsuario() {
		return perfilUsuario;
	}

	public void setPerfilUsuario(String perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}

	public List<SelectItem> getSituacoesUsuario() {
		return situacoesUsuario;
	}

	public void setSituacoesUsuario(List<SelectItem> situacoesUsuario) {
		this.situacoesUsuario = situacoesUsuario;
	}

	public String getSituacaoUsuario() {
		return situacaoUsuario;
	}

	public void setSituacaoUsuario(String situacaoUsuario) {
		this.situacaoUsuario = situacaoUsuario;
	}

	public List<Setor> getSetores() {
		return setores;
	}

	public void setSetores(List<Setor> setores) {
		this.setores = setores;
	}

}
