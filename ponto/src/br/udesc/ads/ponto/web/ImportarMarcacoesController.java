package br.udesc.ads.ponto.web;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.udesc.ads.ponto.servicos.ApuracaoService;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "importarMarcacoesController")
@ViewScoped
public class ImportarMarcacoesController implements Serializable {

	private static final long serialVersionUID = 4440216532172458253L;
	
	public String importarMarcacoes() {
		
		ApuracaoService.get().importarMarcacoes();
		ApuracaoService.get().apurarMarcacoesPendentes();
		
		JsfUtils.addMensagemInfo(Messages.getString("msgMarcacoesImportadasSucesso"));
		
		return "";
	}

}
