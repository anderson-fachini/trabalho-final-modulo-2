package br.udesc.ads.ponto.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.joda.time.LocalTime;

@Entity
public class Abono implements Serializable {

	private static final long serialVersionUID = 1470249173903197215L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(columnDefinition = "TIME")
	private LocalTime horaInicio;

	@Column(columnDefinition = "TIME")
	private LocalTime horaFim;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {}, optional = false)
	@JoinColumn(nullable = false)
	private MotivoAbono motivo;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(nullable = false, updatable = false)
	private Apuracao apuracao;
	
	public Abono() {
	}
	
	public Abono(LocalTime horaInicio, LocalTime horaFim, MotivoAbono motivo) {
		this();
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
		this.motivo = motivo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(LocalTime horaFim) {
		this.horaFim = horaFim;
	}

	public MotivoAbono getMotivo() {
		return motivo;
	}

	public void setMotivo(MotivoAbono motivo) {
		this.motivo = motivo;
	}

	public Apuracao getApuracao() {
		return apuracao;
	}

	public void setApuracao(Apuracao apuracao) {
		this.apuracao = apuracao;
		if (!apuracao.containsAbono(this)) {
			apuracao.addAbono(this);
		}
	}

}
