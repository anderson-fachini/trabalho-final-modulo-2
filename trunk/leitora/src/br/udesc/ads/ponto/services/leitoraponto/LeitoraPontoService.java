package br.udesc.ads.ponto.services.leitoraponto;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.joda.time.LocalDateTime;

@WebService(serviceName = "LeitoraPontoService")
public class LeitoraPontoService {

	@WebMethod(operationName = "lerMarcacoes", action = "lerMarcacoes")
	public List<RegistroMarcacao> lerMarcacoes(@WebParam(name = "quantidade") int quantidade) {
		
		// TODO Implementar oficial

		RegistroMarcacao registro = new RegistroMarcacao();
		registro.setId(1L);
		registro.setCodFuncionario(1234L);
		registro.setMarcacao(LocalDateTime.now());
		
		List<RegistroMarcacao> result = new ArrayList<>();
		result.add(registro);

		return result;
	}

	@WebMethod(operationName = "confirmarLeitura", action = "confirmarLeitura")
	public void confirmarLeitura(@WebParam(name = "idInicial") long idInicial, @WebParam(name = "idFinal") long idFinal) {

		// TODO Implementar
	}

}
