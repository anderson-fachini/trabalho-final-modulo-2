package br.udesc.ads.ponto.servicos;

import java.io.File;

public class FeriadoService {

	private static FeriadoService instance;

	public static synchronized FeriadoService get() {
		if (instance == null) {
			instance = new FeriadoService();
		}
		return instance;
	}

	private FeriadoService() {
	}

	/**
	 * Realiza a importação do arquivo de feriados para a base de dados.
	 * 
	 * @param arquivo
	 *            O arquivo a ser importado.
	 */
	public void importarFeriados(File arquivo) {
		new ImportadorFeriados().importar(arquivo);
	}

}
