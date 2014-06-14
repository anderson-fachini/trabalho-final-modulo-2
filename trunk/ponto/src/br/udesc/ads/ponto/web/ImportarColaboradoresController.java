package br.udesc.ads.ponto.web;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

import br.udesc.ads.ponto.servicos.ColaboradorService;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "importarColaboradoresController")
@ViewScoped
public class ImportarColaboradoresController implements Serializable { 
	
	private static final long serialVersionUID = -7072318450107718147L;

	/**
	 * Método chamado ao submeter o arquivo de importação de colaboradores
	 * @param e
	 */
	public void fileListener(FileEntryEvent e) {
		FileEntry fe = (FileEntry) e.getComponent();
		FileEntryResults results = fe.getResults();
		
		for (FileEntryResults.FileInfo i : results.getFiles()) {
			ColaboradorService.get().importarColaboradores(i.getFile());
		}
		
		JsfUtils.addMensagemInfo(Messages.getString("msgColaboradoresImportadosSucesso"));
	}
}
