package br.udesc.ads.ponto.web;

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
public class ImportarColaboradoresController {
	
	public void fileListener(FileEntryEvent e) {
		FileEntry fe = (FileEntry) e.getComponent();
		FileEntryResults results = fe.getResults();
		ColaboradorService cs = new ColaboradorService();
		
		for (FileEntryResults.FileInfo i : results.getFiles()) {
			cs.importarColaboradores(i.getFile());
		}
		
		JsfUtils.addMensagemInfo(Messages.getString("msgColaboradoresImportadosSucesso"));
	}
}
