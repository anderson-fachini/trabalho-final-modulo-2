package br.udesc.ads.ponto.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

@ManagedBean(name = "importarColaboradoresController")
@ViewScoped
public class ImportarColaboradoresController {
	
	public void fileListener(FileEntryEvent e) {
		FileEntry fe = (FileEntry) e.getComponent();
		FileEntryResults results = fe.getResults();
		
		for (FileEntryResults.FileInfo i : results.getFiles()) {
			System.out.println(i.getFileName());
//			i.getFile();
			
		}
	}
}
