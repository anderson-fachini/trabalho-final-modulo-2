package br.udesc.ads.ponto.entidades;

public class Config {

	private Escala escalaPadrao;
	private Integer margemMarcacoes; // Em minutos
	private Integer margemHorasFaltas; // Em minutos

	public Escala getEscalaPadrao() {
		return escalaPadrao;
	}

	public void setEscalaPadrao(Escala escalaPadrao) {
		this.escalaPadrao = escalaPadrao;
	}

	public Integer getMargemMarcacoes() {
		return margemMarcacoes;
	}

	public void setMargemMarcacoes(Integer margemMarcacoes) {
		this.margemMarcacoes = margemMarcacoes;
	}

	public Integer getMargemHorasFaltas() {
		return margemHorasFaltas;
	}

	public void setMargemHorasFaltas(Integer margemHorasFaltas) {
		this.margemHorasFaltas = margemHorasFaltas;
	}

}
