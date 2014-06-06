package br.udesc.ads.ponto.entidades;

import org.joda.time.LocalTime;

public class EscalaMarcacao {

	private Long id;
	private Integer diaSemana; // 1 (Dom) a 7 (Sab)
	private LocalTime hora;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

}
