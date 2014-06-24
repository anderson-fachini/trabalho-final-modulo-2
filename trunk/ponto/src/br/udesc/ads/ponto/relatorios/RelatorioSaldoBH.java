package br.udesc.ads.ponto.relatorios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.AjusteBH;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.manager.Manager;

public class RelatorioSaldoBH {

	private static RelatorioSaldoBH instance;

	public synchronized static RelatorioSaldoBH get() {
		if (instance == null) {
			instance = new RelatorioSaldoBH();
		}
		return instance;
	}

	private RelatorioSaldoBH() {
	}

	/**
	 * Coleta os dados para o Relatório de Saldo de Banco de Horas.<br>
	 * Caso de Uso: UC09.
	 * 
	 * @param dataInicial
	 *            Data inicial do período (obrigatória).
	 * @param dataFinal
	 *            Data final do período (obrigatória).
	 * @param colaboradores
	 *            A lista de colaboradores a serem consultados (obrigatória).
	 */
	public List<SaldoBHResult> consultar(LocalDate dataInicial, LocalDate dataFinal, List<Colaborador> colaboradores) {
		List<SaldoBHResult> result = new ArrayList<>();
		for (Colaborador colaborador : colaboradores) {
			List<AjusteBH> ajustes = getAjustesBHPorPeriodo(dataInicial, dataFinal, colaborador);
			SaldoBHResult item = new SaldoBHResult();
			item.setColaborador(colaborador);
			if (ajustes.isEmpty()) {
				item.setSaldoInicialPeriodo(getSaldoBHColaboradorNaData(dataInicial, colaborador));
			} else {
				item.setSaldoInicialPeriodo(ajustes.get(0).getSaldoAnterior());
			}
			for (AjusteBH ajuste : ajustes) {
				double valorAjuste = ajuste.getValorAjuste();
				if (ajuste.getApuracao() == null) {
					// Quando não tem apuração é porque foi um ajuste manual
					item.setAjustesManuaisBH(item.getAjustesManuaisBH() + valorAjuste);
				} else {
					if (valorAjuste < 0) {
						item.setSaidasBH(item.getSaidasBH() + (-valorAjuste));
					} else {
						item.setEntradasBH(item.getEntradasBH() + valorAjuste);
					}
				}
			}
			item.setSaldoFinalPeriodo(item.getSaldoInicialPeriodo() + item.getEntradasBH() - item.getSaidasBH());
			result.add(item);
		}
		ordenarPorNome(result);
		return result;
	}

	private void ordenarPorNome(List<SaldoBHResult> list) {
		Collections.sort(list, new Comparator<SaldoBHResult>() {

			@Override
			public int compare(SaldoBHResult o1, SaldoBHResult o2) {
				String nome1 = o1.getColaborador().getNome();
				String nome2 = o2.getColaborador().getNome();
				return nome1.compareToIgnoreCase(nome2);
			}
		});
	}

	private double getSaldoBHColaboradorNaData(LocalDate data, Colaborador colaborador) {

		LocalDateTime dataHora = data.toLocalDateTime(new LocalTime(0, 0, 0));

		EntityManager em = Manager.get().getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AjusteBH> query = cb.createQuery(AjusteBH.class);
		Root<AjusteBH> root = query.from(AjusteBH.class);
		Predicate colabEqual = cb.equal(root.get("colaborador"), colaborador);
		Predicate dataGT = cb.greaterThan(root.get("dataHora").as(LocalDateTime.class), dataHora);
		query.select(root).where(cb.and(colabEqual, dataGT)).orderBy(cb.asc(root.get("dataHora")));
		List<AjusteBH> list = em.createQuery(query).getResultList();
		if (list.isEmpty()) {
			// Se não houve nenhum ajuste depois dessa data, então o saldo atual
			// é o saldo dessa data.
			return colaborador.getSaldoBH();
		} else {
			return list.get(0).getSaldoAnterior();
		}
	}

	private List<AjusteBH> getAjustesBHPorPeriodo(LocalDate dataInicial, LocalDate dataFinal, Colaborador colaborador) {

		LocalDateTime dateTimeInicial = dataInicial.toLocalDateTime(new LocalTime(0, 0, 0));
		LocalDateTime dateTimeFinal = dataFinal.toLocalDateTime(new LocalTime(23, 59, 59, 999));

		EntityManager em = Manager.get().getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AjusteBH> query = cb.createQuery(AjusteBH.class);
		Root<AjusteBH> root = query.from(AjusteBH.class);
		Predicate colabEqual = cb.equal(root.get("colaborador"), colaborador);
		Predicate dataBetween = cb.between(root.get("dataHora").as(LocalDateTime.class), dateTimeInicial, dateTimeFinal);
		query.select(root).where(cb.and(colabEqual, dataBetween)).orderBy(cb.asc(root.get("dataHora")));
		return em.createQuery(query).getResultList();
	}

}
