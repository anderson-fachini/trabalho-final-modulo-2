package br.udesc.ads.ponto.relatorios;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.servicos.ApuracaoService;
import br.udesc.ads.ponto.util.TimeUtils;

public class RelatorioResumoHorasTrabalhadas {

	private static RelatorioResumoHorasTrabalhadas instance;

	public synchronized static RelatorioResumoHorasTrabalhadas get() {
		if (instance == null) {
			instance = new RelatorioResumoHorasTrabalhadas();
		}
		return instance;
	}

	private RelatorioResumoHorasTrabalhadas() {
	}

	/**
	 * Coleta os dados para o Relatório de Resumo de Horas Trabalhas.<br>
	 * Caso de Uso: UC08.
	 * 
	 * @param dataInicial
	 *            Data inicial do período (obrigatória).
	 * @param dataFinal
	 *            Data final do período (obrigatória).
	 * @param colaboradores
	 *            A lista de colaboradores a serem consultados (obrigatória).
	 */
	public List<ResumoHorasTrabalhadasResult> consultar(LocalDate dataInicial, LocalDate dataFinal, List<Colaborador> colaboradores) {
		List<ResumoHorasTrabalhadasResult> result = new ArrayList<>();
		for (Colaborador colaborador : colaboradores) {
			List<Apuracao> apuracoes = ApuracaoService.get().getApuracoesConfirmadas(dataInicial, dataFinal, colaborador, true);
			ResumoHorasTrabalhadasResult item = new ResumoHorasTrabalhadasResult();
			item.setColaborador(colaborador);
			for (Apuracao apuracao : apuracoes) {
				item.setHorasTrabalhadas(item.getHorasTrabalhadas() + TimeUtils.toDoubleHoras(apuracao.getHorasTrabalhadas()));
				item.setHorasExcedentes(item.getHorasExcedentes() + TimeUtils.toDoubleHoras(apuracao.getHorasExcedentes()));
				item.setHorasFaltantes(item.getHorasFaltantes() + TimeUtils.toDoubleHoras(apuracao.getHorasFaltantes()));
				item.setHorasAbonadas(item.getHorasAbonadas() + TimeUtils.toDoubleHoras(apuracao.getHorasAbonadas()));
			}
			result.add(item);
		}
		return result;
	}

}
