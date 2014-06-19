package br.udesc.ads.ponto.web;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

import br.udesc.ads.ponto.servicos.FeriadoService;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "importarFeriadosController")
@ViewScoped
public class ImportarFeriadosController implements Serializable { 

	private static final long serialVersionUID = 6795388206141407679L;

	/**
	 * Método chamado ao submeter o arquivo de importação de feriados
	 * @param e
	 */
	public void fileListener(FileEntryEvent e) {
		FileEntry fe = (FileEntry) e.getComponent();
		FileEntryResults results = fe.getResults();
		
		for (FileEntryResults.FileInfo i : results.getFiles()) {
			FeriadoService.get().importarFeriados(i.getFile());
		}
		
		JsfUtils.addMensagemInfo(Messages.getString("msgFeriadosImportadosSucesso"));
	}
}
