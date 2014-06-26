package br.udesc.ads.ponto.relatorios;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.DiaSemana;

public class ResumoApuracaoPontoResult {

	private LocalDate data;
	private DiaSemana diaSemana;
	private List<LocalTime> marcacoes = new ArrayList<>();
	private String detalhes;
	
	public ResumoApuracaoPontoResult() {
	}
	
	public ResumoApuracaoPontoResult(LocalDate data) {
		this.data = data;
		this.diaSemana = DiaSemana.fromLocalDate(data);
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public List<LocalTime> getMarcacoes() {
		return marcacoes;
	}

	public void setMarcacoes(List<LocalTime> marcacoes) {
		this.marcacoes = marcacoes;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

}
