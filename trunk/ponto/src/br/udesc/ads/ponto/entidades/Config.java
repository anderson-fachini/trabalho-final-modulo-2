package br.udesc.ads.ponto.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Config {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Escala escalaPadrao;
	private Integer margemMarcacoes; // Em minutos
	private Integer margemHorasFaltas; // Em minutos

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
