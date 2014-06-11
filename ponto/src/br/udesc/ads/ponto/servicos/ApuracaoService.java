package br.udesc.ads.ponto.servicos;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Usuario;

public class ApuracaoService {

	/**
	 * Realiza a importa��o das marca��es do Dispositivo de Captura de Ponto.<br>
	 * Esta opera��o executa os seguintes passos:
	 * <ul>
	 * <li>Invoca o Webservice do dispositivo e recebe uma lista de registros
	 * marca��o;</li>
	 * <li>Converte os registros marca��o em objetos Marcacao/Apuracao;</li>
	 * <li>Persiste as Marcacoes/Apuracoes;</li>
	 * </ul>
	 * Realiza o Caso de Uso: UC01.
	 */
	public void importarMarcacoes() {

		ImportadorMarcacoes importador = new ImportadorMarcacoes();
		importador.importar();
	}

	/**
	 * Realiza a apura��o das marca��es importadas.<br>
	 * Esta opera��o executa os seguintes passos:
	 * <ul>
	 * <li>Carrega as marca��es/apura��es n�o apuradas do banco de dados;</li>
	 * <li>Realiza os c�lculos de Horas Trabalhadas, Excedentes, Faltantes e
	 * Ocorr�ncias;</li>
	 * <li>Marca as apura��es como apuradas;</li>
	 * <li>Persiste as Marcacoes/Apuracoes/Ocorrencias com os valores apurados;</li>
	 * </ul>
	 * Realiza o Caso de Uso: UC01.
	 */
	public void apurarMarcacoes() {

		// TODO Implementar
	}

	/**
	 * Realiza a confirma��o/aprova��o de uma apura��o.<br>
	 * Esta opera��o executa os seguintes passos:
	 * <ul>
	 * <li>Preenche a data da aprova��o com a data/hora atuais;</li>
	 * <li>Preenche o respons�vel da aprova��o como o usu�rio informado;</li>
	 * <li>Persiste o novo estado da apura��o;</li>
	 * <li>Caso haja horas excedentes ou faltantes:
	 * <ul>
	 * <li>Efetua o ajuste no Banco de Horas do colaborador;</li>
	 * <li>Registra um hist�rico de ajuste de Banco de Horas;</li>
	 * <li>Persiste os dados de colaborador e Banco de Horas;</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * Realiza o Caso de Uso: UC04.
	 * 
	 * @param apuracao
	 *            A apura��o a ser aprovada/confirmada. Os ajustes de marca��es,
	 *            abonos, motivos e observa��es devem estar j� setados neste
	 *            objeto.
	 * @param usuario
	 *            O usu�rio que est� aprovando a apura��o.
	 */
	public void aprovarApuracao(Apuracao apuracao, Usuario usuario) {

		// TODO Implementar
	}
	
	
	// TODO Remover este m�todo depois e criar um Cen�rio de teste
	public static void main(String[] args) {
		
		// TODO Primeiro deletar todas as apura��es.
		
		new ApuracaoService().importarMarcacoes();
	}

}
