package br.udesc.ads.ponto.services.leitoraponto;

import java.io.IOException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "LeitoraPontoService")
public class LeitoraPontoService {

	private static final String ARQUIVO_MARCACOES = "marcacoes.csv";
	private static final String ARQUIVO_CONFIRMACOES = "confirmacoes.txt";

	private LeitoraPonto leitora = new LeitoraPonto(ARQUIVO_MARCACOES, ARQUIVO_CONFIRMACOES);

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
