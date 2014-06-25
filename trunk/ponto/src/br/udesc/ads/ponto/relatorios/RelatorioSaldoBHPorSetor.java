package br.udesc.ads.ponto.relatorios;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.servicos.ColaboradorService;

public class RelatorioSaldoBHPorSetor {
	
	private static RelatorioSaldoBHPorSetor instance;

	public synchronized static RelatorioSaldoBHPorSetor get() {
		if (instance == null) {
			instance = new RelatorioSaldoBHPorSetor();
		}
		return instance;
	}

	private RelatorioSaldoBHPorSetor() {
	}
	
	/**
	 * Coleta os dados para o Relatório de Saldo de Banco de Horas por Setor.<br>
	 * Caso de Uso: UC05.
	 * 
	 * @param dataInicial
	 *            Data inicial do período (obrigatória).
	 * @param dataFinal
	 *            Data final do período (obrigatória).
	 * @param setores
	 *            A lista de setores a serem consultados (obrigatória).
	 */
	public List<SaldoBHSetorResult> consultar(LocalDate dataInicial, LocalDate dataFinal, List<Setor> setores) {
		List<SaldoBHSetorResult> result = new ArrayList<>();
		for (Setor setor : setores) {
			List<Colaborador> colaboradores = ColaboradorService.get().getColaboradoresAtivosPorSetor(setor);

			List<SaldoBHResult> listaBHs = RelatorioSaldoBH.get().consultar(dataInicial, dataFinal, colaboradores);
			SaldoBHSetorResult item = new SaldoBHSetorResult();
			item.setSetor(setor);
			for (SaldoBHResult bh : listaBHs) {
				item.setEntradasBH(item.getEntradasBH() + bh.getEntradasBH());
				item.setSaidasBH(item.getSaidasBH() + bh.getSaidasBH());
				item.setAjustesManuaisBH(item.getAjustesManuaisBH() + bh.getAjustesManuaisBH());
				item.setSaldoInicialPeriodo(item.getSaldoInicialPeriodo() + bh.getSaldoFinalPeriodo());
				item.setSaldoFinalPeriodo(item.getSaldoFinalPeriodo() + bh.getSaldoFinalPeriodo());
			}
			result.add(item);
		}
		return result;
	}

}
