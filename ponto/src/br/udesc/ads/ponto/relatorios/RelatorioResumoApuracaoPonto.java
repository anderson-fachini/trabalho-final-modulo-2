package br.udesc.ads.ponto.relatorios;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.DiaSemana;
import br.udesc.ads.ponto.servicos.ApuracaoService;
import br.udesc.ads.ponto.servicos.FeriadoService;

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
		List<ResumoApuracaoPontoResult> result = new ArrayList<>();
		
		Map<LocalDate, Apuracao> mapa = getApuracoesPorData(dtInicial, dtFinal, colab, confirmacao);
		
		for (LocalDate d = dtInicial; !d.isAfter(dtFinal); d = d.plusDays(1)) {
			if (FeriadoService.get().existeFeriado(d)) {
				continue;
			}
			ResumoApuracaoPontoResult item = new ResumoApuracaoPontoResult();
			item.setData(d);
			DiaSemana diaSemana = DiaSemana.fromLocalDate(d);
			item.setDiaSemana(diaSemana);
			
			if (mapa.containsKey(d)) {
				Apuracao apuracao = mapa.get(d);
				item.setMarcacoes(apuracao.getSequenciaMarcacoes());
				item.setSituacoes(getOcorrenciasAsString(apuracao));
			} else {
				if (diaSemana.isFinalDeSemana()) {
					// TODO Utilizar o messages
					item.setSituacoes("Descanso Semanal Remunerado");
				} else {
					throw new AssertionError("Está faltando apuração do dia " + d + " pro colaborador " + colab.getCodigo() + "!");
				}
			}
			result.add(item);
		}
		return result;
	}

	private Map<LocalDate, Apuracao> getApuracoesPorData(LocalDate dtInicial, LocalDate dtFinal, Colaborador colab, TipoConfirmacao confirmacao) {
		List<Apuracao> apuracoes;
		if (confirmacao == TipoConfirmacao.TODAS) {
			apuracoes = ApuracaoService.get().getApuracoesPorPeriodo(dtInicial, dtFinal, colab);
		} else {
			boolean apenasConfirmadas = confirmacao == TipoConfirmacao.CONFIRMADAS;
			apuracoes = ApuracaoService.get().getApuracoesConfirmadas(dtInicial, dtFinal, colab, apenasConfirmadas);
		}
		Map<LocalDate, Apuracao> mapa = new IdentityHashMap<>();
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
