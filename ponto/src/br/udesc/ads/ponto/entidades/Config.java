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
	private Integer margemHorasFaltantes; // Em minutos
	private Integer margemHorasExcedentes; // Em minutos
	private Integer intervaloMinimoAlmoco; // Em minutos
	private Integer intervaloMaximoTrabalho; // Em minutos
	private Integer intervaloMinimoInterjornadas; // Em minutos
	private Integer intervaloMinimoIntrajornada; // Em minutos

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

	public Integer getMargemHorasFaltantes() {
		return margemHorasFaltantes;
	}

	public void setMargemHorasFaltantes(Integer margemHorasFaltas) {
		this.margemHorasFaltantes = margemHorasFaltas;
	}

	public Integer getMargemHorasExcedentes() {
		return margemHorasExcedentes;
	}

	public void setMargemHorasExcedentes(Integer margemHorasExcedentes) {
		this.margemHorasExcedentes = margemHorasExcedentes;
	}

	public Integer getIntervaloMinimoAlmoco() {
		return intervaloMinimoAlmoco;
	}

	public void setIntervaloMinimoAlmoco(Integer intervaloMinimoAlmoco) {
		this.intervaloMinimoAlmoco = intervaloMinimoAlmoco;
	}

	public Integer getIntervaloMaximoTrabalho() {
		return intervaloMaximoTrabalho;
	}

	public void setIntervaloMaximoTrabalho(Integer intervaloMaximoTrabalho) {
		this.intervaloMaximoTrabalho = intervaloMaximoTrabalho;
	}

	public Integer getIntervaloMinimoInterjornadas() {
		return intervaloMinimoInterjornadas;
	}

	public void setIntervaloMinimoInterjornadas(Integer intervaloMinimoInterjornadas) {
		this.intervaloMinimoInterjornadas = intervaloMinimoInterjornadas;
	}

	public Integer getIntervaloMinimoIntrajornada() {
		return intervaloMinimoIntrajornada;
	}

	public void setIntervaloMinimoIntrajornada(Integer intervaloMinimoIntrajornada) {
		this.intervaloMinimoIntrajornada = intervaloMinimoIntrajornada;
	}

}
