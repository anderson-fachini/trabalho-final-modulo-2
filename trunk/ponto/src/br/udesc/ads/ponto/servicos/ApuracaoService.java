package br.udesc.ads.ponto.servicos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.LocalDate;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
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

	/**
	 * Busca as apurações de um colaborador em um determinado período.
	 * 
	 * @param dataInicial
	 *            A data inicial de pesquisa
	 * @param dataFinal
	 *            A data final de pesquisa
	 * @param colaborador
	 *            O colaborador a ser filtrado
	 * @return Lista de apurações do colaborador dentro do período especificado.
	 *         Se não encontrar nada retorna a lista vazia.
	 */
	public List<Apuracao> getApuracoesPorPeriodo(LocalDate dataInicial, LocalDate dataFinal, Colaborador colaborador) {
		EntityManager em = Manager.get().getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Apuracao> criteria = cb.createQuery(Apuracao.class);
		Root<Apuracao> root = criteria.from(Apuracao.class);
		Predicate[] filtros = {//
		cb.equal(root.get("colaborador"), colaborador),//
				cb.between(root.<LocalDate> get("data"), dataInicial, dataFinal) //
		};
		criteria.select(root).where(filtros).orderBy(cb.asc(root.get("data")));
		return em.createQuery(criteria).getResultList();
	}

	/**
	 * Busca as apurações confirmadas/não confirmadas de um colaborador em um determinado período.
	 * 
	 * @param dataInicial
	 *            A data inicial de pesquisa
	 * @param dataFinal
	 *            A data final de pesquisa
	 * @param colaborador
	 *            O colaborador a ser filtrado
	 * @param confirmadas
	 *            True para filtrar somente as confirmadas. False para filtrar
	 *            somente as não confirmadas.
	 * @return Lista de apurações do colaborador dentro do período
	 *         especificado. Se não encontrar nada retorna a lista vazia.
	 */
	public List<Apuracao> getApuracoesConfirmadas(LocalDate dataInicial, LocalDate dataFinal, Colaborador colaborador, boolean confirmadas) {
		EntityManager em = Manager.get().getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Apuracao> query = cb.createQuery(Apuracao.class);
		Root<Apuracao> root = query.from(Apuracao.class);
		Path<Object> dataConfirmacao = root.get("dataConfirmacao");
		Predicate[] filtros = { //
		cb.equal(root.get("colaborador"), colaborador), //
				confirmadas ? cb.isNotNull(dataConfirmacao) : cb.isNull(dataConfirmacao), //
				cb.between(root.<LocalDate> get("data"), dataInicial, dataFinal) //
		};
		query.select(root).where(filtros).orderBy(cb.asc(root.get("data")));
		return em.createQuery(query).getResultList();
	}

}
