package br.udesc.ads.ponto.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import br.udesc.ads.ponto.entidades.MotivoAbono;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.servicos.MotivoAbonoService;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "motivosAbono")
@SessionScoped
public class MotivoAbonoController implements Serializable {

	private static final long serialVersionUID = 8017837336286762675L;
	
	private boolean popupOpened = false;
	
	private String popupTitle;
	private String situacao;
	private String descricaoOriginal;
	
	private MotivoAbono motivoAbonoSelecionado;
	
	private List<MotivoAbono> motivosAbono;
	
	private List<SelectItem> situacoes;
	
	public MotivoAbonoController() {
		resetarDados();
		buscaTodosMotivosAbono();
		carregaSituacoes();
	}
	
	private void resetarDados() {
		popupTitle = Messages.getString("novoMotivoAbono");
		situacao = Situacao.ATIVO.getId();
		descricaoOriginal = "";
		
		motivoAbonoSelecionado = new MotivoAbono();
		motivoAbonoSelecionado.setSituacao(Situacao.ATIVO);
	}
	
	public void salvarMotivoAbono() {
		boolean temErro = false;
		
		if (!motivoAbonoSelecionado.getDescricao().equals(descricaoOriginal)) {
			if (MotivoAbonoService.get().checaExisteMotivoAbonoPelaDescicao(motivoAbonoSelecionado.getDescricao())) {
				temErro = true;
				JsfUtils.addMensagemWarning(Messages.getString("msgMotivoAbonoJaExiste"));
			}
		}
		
		if (!temErro) {		
			if (!situacao.equals(motivoAbonoSelecionado.getSituacao().getId())) {
				motivoAbonoSelecionado.setSituacao(Situacao.fromId(situacao));
			}
				
			MotivoAbonoService.get().persisteMotivoAbono(motivoAbonoSelecionado);
			
//			JsfUtils.addMensagemInfo(Messages.getString("msgMotivoAbonoSalvoSucesso"));
			togglePopupOpened();
			
			buscaTodosMotivosAbono();			
		}
	}
	
	public void editarMotivoAbono(MotivoAbono motivoAbono) {
		popupTitle = Messages.getString("editarMotivoAbono");
		motivoAbonoSelecionado = motivoAbono;
		situacao = motivoAbono.getSituacao().getId();
		descricaoOriginal = motivoAbono.getDescricao();
		
		popupOpened = true;
	}
	
	private void buscaTodosMotivosAbono() {
		motivosAbono = MotivoAbonoService.get().getMotivosAbono();
	}
	
	private void carregaSituacoes() {
		situacoes = new ArrayList<SelectItem>();
		
		for (Situacao s : Situacao.values()) {
			situacoes.add(new SelectItem(s.getId(), s.getDescricao()));
		}
	}
	
	public void togglePopupOpened() {
		popupOpened = !popupOpened;
		resetarDados();
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

	public MotivoAbono getMotivoAbonoSelecionado() {
		return motivoAbonoSelecionado;
	}

	public void setMotivoAbonoSelecionado(MotivoAbono motivoAbonoSelecionado) {
		this.motivoAbonoSelecionado = motivoAbonoSelecionado;
	}

	public List<MotivoAbono> getMotivosAbono() {
		return motivosAbono;
	}

	public void setMotivosAbono(List<MotivoAbono> motivosAbono) {
		this.motivosAbono = motivosAbono;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public List<SelectItem> getSituacoes() {
		return situacoes;
	}

	public void setSituacoes(List<SelectItem> situacoes) {
		this.situacoes = situacoes;
	}

}
