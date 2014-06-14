package br.udesc.ads.ponto.servicos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Config;
import br.udesc.ads.ponto.entidades.DiaSemana;
import br.udesc.ads.ponto.entidades.Escala;
import br.udesc.ads.ponto.entidades.Ocorrencia;
import br.udesc.ads.ponto.manager.Manager;

public class ApuradorMarcacoes {

	private final EntityManager entityManager;
	private Map<DiaSemana, Integer> tempoTrabEscalaPadrao = new HashMap<>();

	public ApuradorMarcacoes() {
		this.entityManager = Manager.get().getEntityManager();
	}

	public void apurarMarcacoesPendentes() {
		// Carrega:
		List<Apuracao> apuracoes = getApuracoesNaoProcessadas();

		// Processa:
		for (Apuracao apuracao : apuracoes) {
			processarApuracao(apuracao);
		}

		// Persiste:
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			for (Apuracao apuracao : apuracoes) {
				entityManager.merge(apuracao);
			}
			transaction.commit();
		} catch (Throwable ex) {
			transaction.rollback();
			throw ex;
		}
	}

	public void apurarMarcacoes(Apuracao apuracao) {
		// Processa:
		processarApuracao(apuracao);

		// Persiste:
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			entityManager.merge(apuracao);
			transaction.commit();
		} catch (Throwable ex) {
			transaction.rollback();
			throw ex;
		}
	}

	private void processarApuracao(Apuracao apuracao) {
		calcularHoras(apuracao);
		resolverOcorrencias(apuracao);
		apuracao.setApurada(true);
	}

	private void resolverOcorrencias(Apuracao apuracao) {
		if (apuracao.getHorasExcedentes() != null) {
			if (apuracao.getHorasExcedentes().getMillisOfDay() > 0) {
				apuracao.addOcorrencia(Ocorrencia.HORAS_EXCEDENTES);
			}
		}
		if (apuracao.getHorasFaltantes() != null) {
			if (apuracao.getHorasFaltantes().getMillisOfDay() > 0) {
				apuracao.addOcorrencia(Ocorrencia.HORAS_FALTANTES);
			}
		}
		if (isMarcacoesForaDaEscalaPadrao(apuracao)) {
			apuracao.addOcorrencia(Ocorrencia.MARCACOES_FORA_DA_ESCALA);
		}
		if (apuracao.getSequenciaMarcacoes().size() < 4) {
			apuracao.addOcorrencia(Ocorrencia.MARCACOES_FALTANTES);

		} else if (apuracao.getSequenciaMarcacoes().size() > 4) {
			apuracao.addOcorrencia(Ocorrencia.MARCACOES_EXCEDENTES);
		}
		if (isIntervaloAlmocoIncompleto(apuracao)) {
			apuracao.addOcorrencia(Ocorrencia.INTERVALO_ALMOCO_INCOMPLETO);
		}
		if (isIntervaloInterJornadasIncompleto(apuracao)) {
			apuracao.addOcorrencia(Ocorrencia.INTERVALO_INTERJORNADAS_INCOMPLETO);
		}
		if (isIntervaloIntraJornadaIncompleto(apuracao)) {
			apuracao.addOcorrencia(Ocorrencia.INTERVALO_INTRAJORNADA_INCOMPLETO);
		}
		if (isIntervaloTrabalhoExcedido(apuracao)) {
			apuracao.addOcorrencia(Ocorrencia.INTERVALO_TRABALHO_EXCEDIDO);
		}
	}

	private boolean isIntervaloTrabalhoExcedido(Apuracao apuracao) {
		int max = Manager.get().getConfig().getIntervaloMaximoTrabalho();
		List<Intervalo> intervalos = getIntervalos(apuracao);
		for (Intervalo inter : intervalos) {
			if (inter.isTrabalhado()) {
				if (inter.getMinutos() > max) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * É considerado como incompleto se for:
	 * <ul>
	 * <li>menor que o mínimo;</li>
	 * <li>entre 2 intervalos trabalhados que, somados, ultrapassam o intervalo
	 * máximo de trabalho contíguo;</li>
	 * </ul>
	 */
	private boolean isIntervaloIntraJornadaIncompleto(Apuracao apuracao) {
		int minDescanso = Manager.get().getConfig().getIntervaloMinimoIntrajornada();
		int maxTrabalho = Manager.get().getConfig().getIntervaloMaximoTrabalho();
		List<Intervalo> intervalos = getIntervalos(apuracao);
		for (int i = 0; i < intervalos.size(); ++i) {
			Intervalo inter = intervalos.get(i);
			if (inter.isTrabalhado()) {
				continue;
			}
			if (inter.getMinutos() >= minDescanso) {
				continue;
			}
			if (i == 0 || i == intervalos.size() - 1) {
				continue;
			}
			Intervalo anterior = intervalos.get(i - 1);
			Intervalo posterior = intervalos.get(i + 1);
			if (anterior.isNaoTrabalhado() || posterior.isNaoTrabalhado()) {
				continue;
			}
			if (anterior.getMinutos() + posterior.getMinutos() <= maxTrabalho) {
				continue;
			}
			return true;
		}
		return false;
	}

	private static final int MILLIS_POR_DIA = 24 * 60 * 60 * 1000;

	private boolean isIntervaloInterJornadasIncompleto(Apuracao apuracao) {
		int min = Manager.get().getConfig().getIntervaloMinimoInterjornadas();
		min *= 60 * 1000; // Converte em millis
		List<LocalTime> marcacoesHoje = apuracao.getSequenciaMarcacoes();
		if (marcacoesHoje.isEmpty()) {
			// Se o cara nem veio, então já era.
			return false;
		}
		LocalTime primeiraHoje = marcacoesHoje.get(0);
		int descansoHoje = primeiraHoje.getMillisOfDay();
		if (descansoHoje >= min) {
			// Se o cara já chegou bem tarde, nem precisa olhar o dia anterior.
			return false;
		}
		Apuracao ontem = buscarApuracaoDiaAnterior(apuracao);
		if (ontem == null) {
			// Não veio trabalhar ontem (normal se for sábado/domingo/feriado).
			return false;
		}
		List<LocalTime> marcacoesOntem = ontem.getSequenciaMarcacoes();
		if (marcacoesOntem.isEmpty()) {
			// Não veio ontem.
			return false;
		}
		LocalTime ultimaOntem = marcacoesOntem.get(marcacoesOntem.size() - 1);
		int descansoOntem = MILLIS_POR_DIA - ultimaOntem.getMillisOfDay();
		return descansoOntem + descansoHoje >= min;
	}

	private Apuracao buscarApuracaoDiaAnterior(Apuracao apuracao) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Apuracao> query = cb.createQuery(Apuracao.class);
		Root<Apuracao> root = query.from(Apuracao.class);
		Predicate condicaoColaborador = cb.equal(root.get("colaborador"), apuracao.getColaborador());
		Predicate condicaoData = cb.equal(root.get("data"), apuracao.getData().minusDays(1));
		query.select(root).where(cb.and(condicaoColaborador, condicaoData));
		List<Apuracao> results = entityManager.createQuery(query).getResultList();
		if (results.isEmpty()) {
			return null;
		}
		if (results.size() > 1) {
			throw new AssertionError("Não poderia haver mais de uma apuração por dia/colaborador.");
		}
		return results.get(0);
	}

	private boolean isIntervaloAlmocoIncompleto(Apuracao apuracao) {
		int min = Manager.get().getConfig().getIntervaloMinimoAlmoco();
		DiaSemana diaSemana = DiaSemana.fromLocalDate(apuracao.getData());
		Intervalo almocoPadrao = getIntervaloAlmocoPadrao(diaSemana);
		List<Intervalo> intervalos = getIntervalos(apuracao);
		intervalos = filtrarPorTipo(intervalos, TipoIntervalo.NAO_TRABALHADO);
		List<Intervalo> pausasAlmoco = getIntervalosContidosEm(almocoPadrao, intervalos);
		if (pausasAlmoco.isEmpty()) {
			pausasAlmoco = getIntervalosSobrepostosEm(almocoPadrao, intervalos);
		}
		if (pausasAlmoco.isEmpty()) {
			// Não teve intervalo de almoço
			return false;
		}
		for (Intervalo pausa : pausasAlmoco) {
			if (pausa.getMinutos() >= min) {
				return false;
			}
		}
		return true;
	}

	private List<Intervalo> filtrarPorTipo(List<Intervalo> lista, TipoIntervalo tipo) {
		List<Intervalo> result = new ArrayList<>();
		for (Intervalo i : lista) {
			if (i.getTipo() == tipo) {
				result.add(i);
			}
		}
		return result;
	}

	private List<Intervalo> getIntervalosSobrepostosEm(Intervalo referencia, List<Intervalo> lista) {
		List<Intervalo> result = new ArrayList<>();
		for (Intervalo inter : lista) {
			if (inter.isSobrepostoEm(referencia)) {
				result.add(inter);
			}
		}
		return result;
	}

	private List<Intervalo> getIntervalosContidosEm(Intervalo referencia, List<Intervalo> lista) {
		List<Intervalo> result = new ArrayList<>();
		for (Intervalo inter : lista) {
			if (inter.isContidoEm(referencia)) {
				result.add(inter);
			}
		}
		return result;
	}

	// TODO A Regra que define o almoço é esta mesma?
	private Intervalo getIntervaloAlmocoPadrao(DiaSemana diaSemana) throws AssertionError {
		Escala escala = Manager.get().getConfig().getEscalaPadrao();
		List<Intervalo> intervEscala = getIntervalos(escala, diaSemana);
		if (intervEscala.size() % 2 == 0) {
			throw new AssertionError("Quantidade de intervalos da escala padr�o deveria ser �mpar.");
		}
		return intervEscala.get(intervEscala.size() / 2);
	}

	private List<Intervalo> getIntervalos(Apuracao apuracao) {
		return getIntervalos(apuracao.getSequenciaMarcacoes());
	}

	private List<Intervalo> getIntervalos(Escala escala, DiaSemana diaSemana) {
		return getIntervalos(escala.getSequenciaMarcacoes(diaSemana));
	}

	private List<Intervalo> getIntervalos(List<LocalTime> marcacoes) {
		List<Intervalo> result = new ArrayList<>();
		TipoIntervalo tipo = TipoIntervalo.TRABALHADO;
		for (int i = 1; i < marcacoes.size(); ++i) {
			LocalTime inicio = marcacoes.get(i - 1);
			LocalTime fim = marcacoes.get(i);
			result.add(new Intervalo(inicio, fim, tipo));
			if (tipo == TipoIntervalo.TRABALHADO) {
				tipo = TipoIntervalo.NAO_TRABALHADO;
			} else {
				tipo = TipoIntervalo.TRABALHADO;
			}
		}
		return result;
	}

	private boolean isMarcacoesForaDaEscalaPadrao(Apuracao apuracao) {
		Config config = Manager.get().getConfig();
		int margem = config.getMargemMarcacoes();
		DiaSemana diaSemana = DiaSemana.fromLocalDate(apuracao.getData());
		List<LocalTime> escala = config.getEscalaPadrao().getSequenciaMarcacoes(diaSemana);
		List<LocalTime> marcacoes = apuracao.getSequenciaMarcacoes();
		for (LocalTime marcacao : marcacoes) {
			if (!estaNaEscala(marcacao, escala, margem)) {
				return true;
			}
		}
		return false;
	}

	private boolean estaNaEscala(LocalTime marcacao, List<LocalTime> escala, int margem) {
		margem *= 60 * 1000; // Converte a margem para millis
		for (LocalTime esperado : escala) {
			int delta = marcacao.getMillisOfDay() - esperado.getMillisOfDay();
			if (delta < 0) {
				delta = -delta;
			}
			if (delta <= margem) {
				return true;
			}
		}
		return false;
	}

	private void calcularHoras(Apuracao apuracao) {
		List<LocalTime> marcacoes = apuracao.getSequenciaMarcacoes();
		int qtdMarcacoes = marcacoes.size();
		if (qtdMarcacoes % 2 != 0) {
			// Não é possível calcular com marcações ímpares.
			return;
		}
		// Utiliza o mapa para fazer este cálculo uma só vez para cada dia da
		// semana:
		DiaSemana diaSemana = DiaSemana.fromLocalDate(apuracao.getData());
		Integer tempoPadraoTrab = tempoTrabEscalaPadrao.get(diaSemana);
		if (tempoPadraoTrab == null) {
			tempoPadraoTrab = calcularTempoTrabalhoEscalaPadrao(diaSemana);
			tempoTrabEscalaPadrao.put(diaSemana, tempoPadraoTrab);
		}

		// É possóvel calcular as horas se se a quantidade de marcações for par.
		int trabalhadas = calcularTempoTrabalhado(marcacoes);

		// TODO Confimar: Pode ter hora extra de 1 segundo. É isso mesmo?
		int excedentes = trabalhadas - tempoPadraoTrab;
		if (excedentes < 0) {
			excedentes = 0;
		}

		int margem = Manager.get().getConfig().getMargemHorasFaltas() * 60 * 1000;
		int faltantes = tempoPadraoTrab - trabalhadas - margem;
		if (faltantes < 0) {
			faltantes = 0;
		}

		// TODO Calcular as horas abonadas!

		apuracao.setHorasTrabalhadas(LocalTime.fromMillisOfDay(trabalhadas));
		apuracao.setHorasExcedentes(LocalTime.fromMillisOfDay(excedentes));
		apuracao.setHorasFaltantes(LocalTime.fromMillisOfDay(faltantes));
	}

	// TODO Melhoria: Refatorar para trabalhar com objetos Intervalo.
	// Retorno em millis
	private int calcularTempoTrabalhado(List<LocalTime> marcacoes) {
		int somaPares = 0;
		int somaImpares = 0;
		for (int i = 0; i < marcacoes.size(); i++) {
			int millis = marcacoes.get(i).getMillisOfDay();
			// Atenção: i=0 significa marcação 1 (Ímpar)
			if (i % 2 == 0) {
				somaImpares += millis;
			} else {
				somaPares += millis;
			}
		}
		return somaPares - somaImpares;
	}

	private int calcularTempoTrabalhoEscalaPadrao(DiaSemana diaSemana) {
		Config config = Manager.get().getConfig();
		List<LocalTime> marcacoes = config.getEscalaPadrao().getSequenciaMarcacoes(diaSemana);
		return calcularTempoTrabalhado(marcacoes);
	}

	private List<Apuracao> getApuracoesNaoProcessadas() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Apuracao> query = cb.createQuery(Apuracao.class);
		Root<Apuracao> root = query.from(Apuracao.class);
		Path<Object> apurada = root.get("apurada");
		query.select(root).where(cb.or(cb.isNull(apurada), cb.equal(apurada, "false")));
		return entityManager.createQuery(query).getResultList();
	}

	public static void main(String[] args) {

		// Apuracao apu = new Apuracao();
		//
		// apu.addMarcacao(new Marcacao(new LocalTime(8, 10)));
		// apu.addMarcacao(new Marcacao(new LocalTime(9, 10)));
		// apu.addMarcacao(new Marcacao(new LocalTime(12, 15)));
		// apu.addMarcacao(new Marcacao(new LocalTime(13, 13)));
		//
		// System.out.println(new
		// ApuradorMarcacoes().calcularTempoTrabalhado(apu));

	}

}
