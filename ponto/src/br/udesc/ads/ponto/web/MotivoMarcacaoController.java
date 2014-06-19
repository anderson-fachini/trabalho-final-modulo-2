package br.udesc.ads.ponto.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import br.udesc.ads.ponto.entidades.MotivoMarcacao;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.servicos.MotivoMarcacaoService;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "motivosMarcacao")
@SessionScoped
public class MotivoMarcacaoController implements Serializable {

	private static final long serialVersionUID = 8017837336286762675L;
	
	private boolean popupOpened = false;
	
	private String popupTitle;
	private String situacao;
	private String descricaoOriginal;
	
	private MotivoMarcacao motivoMarcacaoSelecionado;
	
	private List<MotivoMarcacao> motivosMarcacao;
	
	private List<SelectItem> situacoes;
	
	public MotivoMarcacaoController() {
		resetarDados();
		buscaTodosMotivosMarcacao();
		carregaSituacoes();
	}
	
	private void resetarDados() {
		popupTitle = Messages.getString("novoMotivoAjustePonto");
		situacao = Situacao.ATIVO.getId();
		descricaoOriginal = "";
		
		motivoMarcacaoSelecionado = new MotivoMarcacao();
		motivoMarcacaoSelecionado.setSituacao(Situacao.ATIVO);
	}
	
	public void salvarMotivoMarcacao() {
		boolean temErro = false;
		
		if (!motivoMarcacaoSelecionado.getDescricao().equals(descricaoOriginal)) {
			if (MotivoMarcacaoService.get().checaExisteMotivoMarcacaoPelaDescicao(motivoMarcacaoSelecionado.getDescricao())) {
				temErro = true;
				JsfUtils.addMensagemWarning(Messages.getString("msgMotivoAjustePontoJaExiste"));
			}
		}
		
		if (!temErro) {		
			if (!situacao.equals(motivoMarcacaoSelecionado.getSituacao().getId())) {
				motivoMarcacaoSelecionado.setSituacao(Situacao.fromId(situacao));
			}
				
			MotivoMarcacaoService.get().persisteMotivoMarcacao(motivoMarcacaoSelecionado);
			
			JsfUtils.addMensagemInfo(Messages.getString("msgMotivoAjustePontoSalvoSucesso"));
			
			buscaTodosMotivosMarcacao();			
		}
	}
	
	public void editarMotivoMarcacao(MotivoMarcacao motivoMarcacao) {
		popupTitle = Messages.getString("editarMotivoAjustePonto");
		motivoMarcacaoSelecionado = motivoMarcacao;
		situacao = motivoMarcacao.getSituacao().getId();
		descricaoOriginal = motivoMarcacao.getDescricao();
		
		popupOpened = true;
	}
	
	private void buscaTodosMotivosMarcacao() {
		motivosMarcacao = MotivoMarcacaoService.get().getMotivosMarcacao();
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

	public MotivoMarcacao getMotivoMarcacaoSelecionado() {
		return motivoMarcacaoSelecionado;
	}

	public void setMotivoMarcacaoSelecionado(MotivoMarcacao motivoMarcacaoSelecionado) {
		this.motivoMarcacaoSelecionado = motivoMarcacaoSelecionado;
	}

	public List<MotivoMarcacao> getMotivosMarcacao() {
		return motivosMarcacao;
	}

	public void setMotivosMarcacao(List<MotivoMarcacao> motivosMarcacao) {
		this.motivosMarcacao = motivosMarcacao;
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
