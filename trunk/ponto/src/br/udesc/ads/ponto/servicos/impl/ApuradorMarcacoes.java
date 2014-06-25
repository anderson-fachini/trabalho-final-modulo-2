package br.udesc.ads.ponto.servicos.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.Abono;
import br.udesc.ads.ponto.entidades.AjusteBH;
import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Config;
import br.udesc.ads.ponto.entidades.DiaSemana;
import br.udesc.ads.ponto.entidades.Escala;
import br.udesc.ads.ponto.entidades.Ocorrencia;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.servicos.ColaboradorService;
import br.udesc.ads.ponto.servicos.FeriadoService;
import br.udesc.ads.ponto.util.TimeUtils;

public class ApuradorMarcacoes {

	private final EntityManager entityManager;

	public ApuradorMarcacoes() {
		this.entityManager = Manager.get().getEntityManager();
	}

	public void apurarMarcacoesPendentes() {
		// Carrega:
		List<Apuracao> apuracoes = getApuracoesPendentes();
		if (apuracoes.isEmpty()) {
			// Se não há nenhuma marcação pendente, ignora.
			return;
		}
		
		// Descobre faltantes:
		incluirMarcacoesFaltantes(apuracoes);

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

	private void incluirMarcacoesFaltantes(List<Apuracao> apuracoes) {
		LocalDate dataInicial = getDataUltimaApurada();
		if (dataInicial != null) {
			dataInicial = dataInicial.plusDays(1);
		} else {
			// Se não há apuração anterior, assume a data da primeira apuração desse lote
			dataInicial = apuracoes.get(0).getData();
		}
		LocalDate dataFinal = apuracoes.get(apuracoes.size() - 1).getData();
		List<Apuracao> faltantes = descobrirDatasSemMarcacao(dataInicial, dataFinal, apuracoes);
		apuracoes.addAll(faltantes);
		ordenarPorDataEColaborador(apuracoes);
	}

	private LocalDate getDataUltimaApurada() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<LocalDate> query = cb.createQuery(LocalDate.class);
		Root<Apuracao> root = query.from(Apuracao.class);
		Predicate condicao = cb.equal(root.get("apurada"), Boolean.TRUE);
		query.select(root.<LocalDate> get("data")).where(condicao).orderBy(cb.desc(root.get("data")));
		List<LocalDate> result = entityManager.createQuery(query).setMaxResults(1).getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	private void ordenarPorDataEColaborador(List<Apuracao> apuracoes) {
		Collections.sort(apuracoes, new Comparator<Apuracao>() {

			@Override
			public int compare(Apuracao o1, Apuracao o2) {
				int res = o1.getData().compareTo(o2.getData());
				if (res != 0) {
					return res;
				}
				return o1.getColaborador().getCodigo().compareTo(o2.getColaborador().getCodigo());
			}

		});
	}

	private List<Apuracao> descobrirDatasSemMarcacao(LocalDate dataInicial, LocalDate dataFinal, List<Apuracao> apuracoes) {
		List<Apuracao> result = new ArrayList<>();
		Map<ChaveApuracao, Apuracao> mapa = getMapaApuracoes(apuracoes);
		List<Colaborador> colaboradores = ColaboradorService.get().getColaboradoresAtivos();
		for (LocalDate data = dataInicial; !data.isAfter(dataFinal); data = data.plusDays(1)) {
			if (DiaSemana.fromLocalDate(data).isFinalDeSemana()) {
				continue;
			}
			if (FeriadoService.get().existeFeriado(data)) {
				continue;
			}
			for (Colaborador col : colaboradores) {
				ChaveApuracao chave = new ChaveApuracao(data, col.getCodigo());
				if (mapa.containsKey(chave)) {
					continue;
				}
				Apuracao faltante = new Apuracao();
				faltante.setColaborador(col);
				faltante.setData(data);
				faltante.setApurada(false);
				result.add(faltante);
			}
		}
		return result;
	}

	private Map<ChaveApuracao, Apuracao> getMapaApuracoes(List<Apuracao> apuracoes) {
		Map<ChaveApuracao, Apuracao> result = new HashMap<>();
		for (Apuracao a : apuracoes) {
			result.put(new ChaveApuracao(a.getData(), a.getColaborador().getCodigo()), a);
		}
		return result;
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

	public void processarApuracao(Apuracao apuracao) {
		boolean calculou = calcularHoras(apuracao);
		apuracao.setInconsistente(!calculou);
		resolverOcorrencias(apuracao);
		apuracao.setExigeConfirmacao(apuracao.getInconsistente() || apuracao.getOcorrenciasSize() > 0);
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

		List<LocalTime> marcacoes = apuracao.getSequenciaMarcacoes();
		if (marcacoes.size() < 4) {
			apuracao.addOcorrencia(Ocorrencia.MARCACOES_FALTANTES);
		} else if (marcacoes.size() > 4) {
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
	 * <li>entre 2 intervalos trabalhados que, somados, ultrapassam o intervalo máximo de trabalho contíguo;</li>
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
		List<LocalTime> marcacoesOntem = apuracao.getSequenciaMarcacoes();
		if (marcacoesOntem.isEmpty()) {
			// Não veio ontem.
			return false;
		}
		LocalTime ultimaOntem = marcacoesOntem.get(marcacoesOntem.size() - 1);
		int descansoOntem = MILLIS_POR_DIA - ultimaOntem.getMillisOfDay();
		return descansoOntem + descansoHoje < min;
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
		DiaSemana diaSemana = DiaSemana.fromLocalDate(apuracao.getData());
		Intervalo almocoPadrao = getIntervaloAlmocoPadrao(diaSemana);
		if (almocoPadrao == null) {
			// Pode ter algum dia sem almoço na escala padrão (ex: sábado).
			return false;
		}
		int min = Manager.get().getConfig().getIntervaloMinimoAlmoco();
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

	private Intervalo getIntervaloAlmocoPadrao(DiaSemana diaSemana) throws AssertionError {
		Escala escala = Manager.get().getConfig().getEscalaPadrao();
		int minAlmoco = Manager.get().getConfig().getIntervaloMinimoAlmoco();

		List<Intervalo> intervalos = getIntervalos(escala, diaSemana);
		if (intervalos.isEmpty()) {
			return null; // Situação normal de um sábado/domingo.
		}
		if (intervalos.size() % 2 == 0) {
			throw new AssertionError("Quantidade de intervalos da escala padrão deveria ser ímpar.");
		}
		List<Intervalo> folgas = filtrarPorTipo(intervalos, TipoIntervalo.NAO_TRABALHADO);
		for (Iterator<Intervalo> it = folgas.iterator(); it.hasNext();) {
			if (it.next().getMinutos() < minAlmoco) {
				it.remove();
			}
		}
		if (folgas.isEmpty()) {
			return null;
		}
		LocalTime meioDia = new LocalTime(12, 0, 0);
		for (Intervalo folga : folgas) {
			if (!folga.getInicio().isAfter(meioDia) && folga.getFim().isAfter(meioDia)) {
				return folga;
			}
		}
		if (folgas.size() == 1) {
			return folgas.get(0);
		}
		throw new RuntimeException("Não foi possível determinar o intervalo padrão de almoço de " + diaSemana.getDescricao() + ".");
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

	private boolean calcularHoras(Apuracao apuracao) {
		List<LocalTime> marcacoes = apuracao.getSequenciaMarcacoes();
		int qtdMarcacoes = marcacoes.size();
		if (qtdMarcacoes % 2 != 0) {
			// Não é possível calcular com marcações ímpares.
			return false;
		}
		int tempoPadraoTrab = getTempoPadraoTrabalho(apuracao.getData());
		int trabalhadas = calcularTempoTrabalhado(marcacoes);

		int abonadas = calcularHorasAbonadas(apuracao);
		int excedentes = calcularHorasExcedentes(tempoPadraoTrab, trabalhadas + abonadas);
		int faltantes = calcularHorasFaltantes(tempoPadraoTrab, trabalhadas + abonadas);
		if (excedentes == 0 && faltantes == 0) {
			trabalhadas = tempoPadraoTrab;
		}
		apuracao.setHorasTrabalhadas(LocalTime.fromMillisOfDay(trabalhadas));
		apuracao.setHorasExcedentes(LocalTime.fromMillisOfDay(excedentes));
		apuracao.setHorasFaltantes(LocalTime.fromMillisOfDay(faltantes));
		apuracao.setHorasAbonadas(LocalTime.fromMillisOfDay(abonadas));
		return true;
	}

	private int calcularHorasAbonadas(Apuracao apuracao) {
		int result = 0;
		for (int i = 0; i < apuracao.getAbonosSize(); ++i) {
			Abono abono = apuracao.getAbono(i);
			LocalTime inicio = TimeUtils.ajustarPrecisao(abono.getHoraInicio());
			LocalTime fim = TimeUtils.ajustarPrecisao(abono.getHoraFim());
			Intervalo intervalo = new Intervalo(inicio, fim, TipoIntervalo.TRABALHADO);
			result += intervalo.getMillis();
		}
		return result;
	}

	private int calcularHorasFaltantes(int tempoPadraoTrab, int trabalhadas) {
		int margem = Manager.get().getConfig().getMargemHorasFaltantes() * 60 * 1000;
		if (tempoPadraoTrab - trabalhadas <= margem) {
			return 0;
		}
		return tempoPadraoTrab - trabalhadas;
	}

	private int calcularHorasExcedentes(int tempoPadraoTrab, int trabalhadas) {
		int margem = Manager.get().getConfig().getMargemHorasExcedentes() * 60 * 1000;
		if (trabalhadas - tempoPadraoTrab <= margem) {
			return 0;
		}
		return trabalhadas - tempoPadraoTrab;
	}

	private int getTempoPadraoTrabalho(LocalDate data) {
		if (FeriadoService.get().existeFeriado(data)) {
			return 0;
		}
		DiaSemana diaSemana = DiaSemana.fromLocalDate(data);
		Config config = Manager.get().getConfig();
		List<LocalTime> marcacoes = config.getEscalaPadrao().getSequenciaMarcacoes(diaSemana);
		return calcularTempoTrabalhado(marcacoes);
	}

	private int calcularTempoTrabalhado(List<LocalTime> marcacoes) {
		List<Intervalo> intervalos = filtrarPorTipo(getIntervalos(marcacoes), TipoIntervalo.TRABALHADO);
		int result = 0;
		for (Intervalo intervalo : intervalos) {
			result += intervalo.getMillis();
		}
		return result;
	}

	private List<Apuracao> getApuracoesPendentes() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Apuracao> query = cb.createQuery(Apuracao.class);
		Root<Apuracao> root = query.from(Apuracao.class);
		Path<Object> apurada = root.get("apurada");
		query.select(root).where(cb.equal(apurada, Boolean.FALSE)).orderBy(cb.asc(root.get("data")));
		return entityManager.createQuery(query).getResultList();
	}

	public void aprovarApuracao(Apuracao apuracao, Usuario usuario) {

		LocalDateTime agora = LocalDateTime.now();
		apuracao.setDataConfirmacao(agora);
		apuracao.setResponsavelConfirmacao(usuario);

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			if (apuracao.temHorasExcedentes() || apuracao.temHorasFaltantes()) {
				Colaborador colaborador = apuracao.getColaborador();

				AjusteBH ajusteBH = new AjusteBH();
				ajusteBH.setApuracao(apuracao);
				ajusteBH.setColaborador(colaborador);
				ajusteBH.setDataHora(agora);
				ajusteBH.setResponsavel(usuario);
				ajusteBH.setSaldoAnterior(colaborador.getSaldoBH());
				LocalTime excedentes = apuracao.getHorasExcedentes();
				LocalTime faltantes = apuracao.getHorasFaltantes();
				ajusteBH.setValorAjuste(TimeUtils.toDoubleHoras(excedentes) - TimeUtils.toDoubleHoras(faltantes));
				ajusteBH.setObservacoes("Processo automático de aprovação de ponto.");
				entityManager.persist(ajusteBH);

				colaborador.setSaldoBH(colaborador.getSaldoBH() + ajusteBH.getValorAjuste());
				entityManager.merge(colaborador);
			}
			// Lista de abonos e marcações serão persistidos também em cascata
			entityManager.merge(apuracao);

			transaction.commit();
		} catch (Throwable ex) {
			transaction.rollback();
			throw ex;
		}
	}

}
