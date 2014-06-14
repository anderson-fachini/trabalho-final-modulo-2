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
		ColaboradorService cs = new ColaboradorService();
		
		for (FileEntryResults.FileInfo i : results.getFiles()) {
			cs.importarColaboradores(i.getFile());
		}
		
		JsfUtils.addMensagemInfo(Messages.getString("msgColaboradoresImportadosSucesso"));
	}
}
