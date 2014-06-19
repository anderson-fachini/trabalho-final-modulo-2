package br.udesc.ads.ponto.servicos;

import java.io.File;

import javax.persistence.EntityManager;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.servicos.impl.ApuradorMarcacoes;
import br.udesc.ads.ponto.servicos.impl.ImportadorMarcacoes;

public class ApuracaoService {

	private static ApuracaoService instance;

	public static synchronized ApuracaoService get() {
		if (instance == null) {
			instance = new ApuracaoService();
		}
		return instance;
	}

	private ApuracaoService() {
	}

	/**
	 * Realiza a importação das marcações do Dispositivo de Captura de Ponto.<br>
	 * Esta operação executa os seguintes passos:
	 * <ul>
	 * <li>Invoca o Webservice do dispositivo e recebe uma lista de registros
	 * marcação;</li>
	 * <li>Converte os registros marcação em objetos Marcacao/Apuracao;</li>
	 * <li>Persiste as Marcacoes/Apuracoes;</li>
	 * </ul>
	 * Realiza o Caso de Uso: UC01.
	 */
	public void importarMarcacoes() {
		new ImportadorMarcacoes().importar();
	}

	/**
	 * Realiza a apuração das marcações importadas.<br>
	 * Esta operação executa os seguintes passos:
	 * <ul>
	 * <li>Carrega as marcações/apurações não apuradas do banco de dados;</li>
	 * <li>Realiza os cálculos de Horas Trabalhadas, Excedentes, Faltantes e
	 * Ocorrências;</li>
	 * <li>Marca as apurações como apuradas;</li>
	 * <li>Persiste as Marcacoes/Apuracoes/Ocorrencias com os valores apurados;</li>
	 * </ul>
	 * Realiza o Caso de Uso: UC01.
	 */
	public void apurarMarcacoesPendentes() {
		new ApuradorMarcacoes().apurarMarcacoesPendentes();
	}

	/**
	 * Realiza uma apuração específica.<br>
	 * Serve para as situações em que uma apuração teve marcações alteradas.<br>
	 * Esta operação executa os seguintes passos:
	 * <ul>
	 * <li>Realiza os cálculos de Horas Trabalhadas, Excedentes, Faltantes e
	 * Ocorrências;</li>
	 * <li>Marca a apuração como apurada;</li>
	 * <li>Persiste a Apuracao com os valores apurados;</li>
	 * </ul>
	 * Realiza o Caso de Uso: UC01.
	 */
	public void apurarMarcacoes(Apuracao apuracao) {
		new ApuradorMarcacoes().apurarMarcacoes(apuracao);
	}

	/**
	 * Realiza a confirmação/aprovação de uma apuração.<br>
	 * Esta operação executa os seguintes passos:
	 * <ul>
	 * <li>Preenche a data da aprovação com a data/hora atuais;</li>
	 * <li>Preenche o responsável da aprovação como o usuário informado;</li>
	 * <li>Persiste o novo estado da apuração;</li>
	 * <li>Caso haja horas excedentes ou faltantes:
	 * <ul>
	 * <li>Efetua o ajuste no Banco de Horas do colaborador;</li>
	 * <li>Registra um histórico de ajuste de Banco de Horas;</li>
	 * </ul>
	 * </li>
	 * <li>Persiste os dados de colaborador e Banco de Horas;</li>
	 * </ul>
	 * Realiza o Caso de Uso: UC04.
	 * 
	 * @param apuracao
	 *            A apuração a ser aprovada/confirmada. Os ajustes de marcações,
	 *            abonos, motivos e observações devem estar já setados neste
	 *            objeto.
	 * @param usuario
	 *            O usuário que está aprovando a apuração.
	 */
	public void aprovarApuracao(Apuracao apuracao, Usuario usuario) {
		new ApuradorMarcacoes().aprovarApuracao(apuracao, usuario);
	}

	// TODO Remover este método depois e criar um Cenário de teste
//	public static void main(String[] args) {
//		
////		importarTudo();
//		
//		EntityManager entityManager = Manager.get().getEntityManager();
//		
//		Apuracao apuracao = entityManager.find(Apuracao.class, 32L);
//		Usuario usuario = entityManager.find(Usuario.class, 1L);
//		
//		ApuracaoService.get().aprovarApuracao(apuracao, usuario);
//	}

//	private static void importarTudo() {
//		System.out.println("Importando colaboradores...");
//		ColaboradorService.get().importarColaboradores(new File("C:\\colaboradores.csv"));
//
//		// TODO Primeiro deletar todas as apurações.
//		System.out.println("Importando marcações...");
//		ApuracaoService.get().importarMarcacoes();
//		System.out.println("Apurando marcações pendentes...");
//		ApuracaoService.get().apurarMarcacoesPendentes();
//	}

}
