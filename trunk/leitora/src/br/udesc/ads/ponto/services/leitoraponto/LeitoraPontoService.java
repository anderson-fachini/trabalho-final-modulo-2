package br.udesc.ads.ponto.services.leitoraponto;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import br.udesc.ads.ponto.leitora.FormatoInvalidoException;
import br.udesc.ads.ponto.leitora.LeitoraPonto;
import br.udesc.ads.ponto.leitora.RegistroMarcacao;

@WebService(serviceName = "LeitoraPontoService")
public class LeitoraPontoService {

	private static final String ARQUIVO_MARCACOES = "marcacoes.csv";
	private static final String ARQUIVO_CONFIRMACOES = "confirmacoes.txt";

	private LeitoraPonto leitora;

	public LeitoraPontoService() {
		String url = getClass().getResource(LeitoraPontoService.class.getSimpleName() + ".class").getPath();
		File dir = new File(url).getParentFile();
		File marcacoes = new File(dir, ARQUIVO_MARCACOES);
		File confirmacoes = new File(dir, ARQUIVO_CONFIRMACOES);
		leitora = new LeitoraPonto(marcacoes, confirmacoes);
	}

	@WebMethod(operationName = "lerMarcacoes", action = "lerMarcacoes")
	public List<RegistroMarcacao> lerMarcacoes(@WebParam(name = "quantidade") int quantidade) {

		try {
			return leitora.lerMarcacoes(quantidade);

		} catch (IOException | FormatoInvalidoException e) {
			// TODO Vamos fazer um tratamento de exceção mais elegante?
			e.printStackTrace();
			return null;
		}
	}

	@WebMethod(operationName = "confirmarLeitura", action = "confirmarLeitura")
	public void confirmarLeitura(@WebParam(name = "idInicial") long idInicial, @WebParam(name = "idFinal") long idFinal) {

		try {
			leitora.confirmarMarcacoes(idInicial, idFinal);
		} catch (IOException e) {
			// TODO Vamos fazer um tratamento de exceção mais elegante?
			e.printStackTrace();
		}
	}
	
}
