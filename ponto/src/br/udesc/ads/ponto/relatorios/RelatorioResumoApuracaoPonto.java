package br.udesc.ads.ponto.relatorios;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.DiaSemana;
import br.udesc.ads.ponto.entidades.Feriado;
import br.udesc.ads.ponto.servicos.ApuracaoService;
import br.udesc.ads.ponto.servicos.FeriadoService;
import br.udesc.ads.ponto.util.Messages;

public class RelatorioResumoApuracaoPonto {

	private static RelatorioResumoApuracaoPonto instance;

	public synchronized static RelatorioResumoApuracaoPonto get() {
		if (instance == null) {
			instance = new RelatorioResumoApuracaoPonto();
		}
		return instance;
	}

	private RelatorioResumoApuracaoPonto() {
	}

	public List<ResumoApuracaoPontoResult> consultar(LocalDate dtInicial, LocalDate dtFinal, Colaborador colab, TipoConfirmacao confirmacao) {
		
		boolean todas = confirmacao == TipoConfirmacao.TODAS;
		boolean apenasPendentes = confirmacao == TipoConfirmacao.PENDENTES;
		boolean apenasConfirmadas = confirmacao == TipoConfirmacao.CONFIRMADAS;
		
		List<ResumoApuracaoPontoResult> result = new ArrayList<>();

		Map<LocalDate, Apuracao> mapa = getApuracoesPorData(dtInicial, dtFinal, colab);

		for (LocalDate d = dtInicial; !d.isAfter(dtFinal); d = d.plusDays(1)) {
			if (mapa.containsKey(d)) {
				Apuracao apuracao = mapa.get(d);
				if (todas || apenasPendentes && apuracao.isPendente() || apenasConfirmadas && !apuracao.isPendente()) {
					ResumoApuracaoPontoResult item = new ResumoApuracaoPontoResult(d);
					item.setMarcacoes(apuracao.getSequenciaMarcacoes());
					item.setDetalhes(getOcorrenciasAsString(apuracao));
					result.add(item);
				}
			} else if (!apenasPendentes) {
				Feriado feriado = FeriadoService.get().buscarPorData(d);
				if (feriado != null) {
					ResumoApuracaoPontoResult item = new ResumoApuracaoPontoResult(d);
					item.setDetalhes(Messages.getString("feriado") + ": " + feriado.getNome());
					result.add(item);

				} else if (DiaSemana.fromLocalDate(d).isFinalDeSemana()) {
					ResumoApuracaoPontoResult item = new ResumoApuracaoPontoResult(d);
					item.setDetalhes(Messages.getString("descansoSemanalRemunerado"));
					result.add(item);
				}
			}
		}
		return result;
	}

	private Map<LocalDate, Apuracao> getApuracoesPorData(LocalDate dtInicial, LocalDate dtFinal, Colaborador colab) {
		List<Apuracao> apuracoes = ApuracaoService.get().getApuracoesPorPeriodo(dtInicial, dtFinal, colab);
		Map<LocalDate, Apuracao> mapa = new LinkedHashMap<>();
		for (Apuracao a : apuracoes) {
			mapa.put(a.getData(), a);
		}
		return mapa;
	}

	private String getOcorrenciasAsString(Apuracao apuracao) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < apuracao.getOcorrenciasSize(); ++i) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(apuracao.getOcorrencia(i).getDescricao());
		}
		return sb.toString();
	}

}
