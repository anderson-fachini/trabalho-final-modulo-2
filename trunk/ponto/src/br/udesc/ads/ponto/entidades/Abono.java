package br.udesc.ads.ponto.entidades;

import java.time.LocalTime;

public class Abono {

	private Long id;
	private LocalTime horaInicio;
	private LocalTime horaFim;
	private MotivoAbono motivo;
	private Apuracao apuracao;

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
	}

}
