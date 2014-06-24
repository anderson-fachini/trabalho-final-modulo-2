package br.udesc.ads.ponto.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ApuracaoPonto implements Serializable {
	
	private static final long serialVersionUID = 1981594807157118273L;

	private String diaMes;
	
	private String diaSemana;
	
	private List<String> marcacoes;
	
	private List<String> ocorrencias;
	
	public ApuracaoPonto() {
		marcacoes = new ArrayList<String>();
		ocorrencias = new ArrayList<String>();
	}

	public String getDiaMes() {
		return diaMes;
	}

	public void setDiaMes(String diaMes) {
		this.diaMes = diaMes;
	}

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public List<String> getMarcacoes() {
		return marcacoes;
	}

	public void setMarcacoes(List<String> marcacoes) {
		this.marcacoes = marcacoes;
	}
	
	public void addMarcacao(String marcacao) {
		marcacoes.add(marcacao);
	}

	public List<String> getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(List<String> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}
	
	public void addOcorrencia(String ocorrencia) {
		ocorrencias.add(ocorrencia);
	}
	
}
