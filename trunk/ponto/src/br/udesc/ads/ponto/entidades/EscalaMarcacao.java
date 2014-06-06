package br.udesc.ads.ponto.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.joda.time.LocalTime;

@Entity
public class EscalaMarcacao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(columnDefinition = "INT")
	private DiaSemana diaSemana;

	@Column(columnDefinition = "TIME")
	private LocalTime hora;

	public EscalaMarcacao() {
	}
	
	public EscalaMarcacao(DiaSemana diaSemana, LocalTime hora) {
		this();
		this.diaSemana = diaSemana;
		this.hora = hora;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

}
