package br.udesc.ads.ponto.entidades;

import org.joda.time.LocalTime;

public class Marcacao {

	private Long id;
	private Apuracao apuracao;
	private LocalTime hora;
	private MotivoMarcacao motivo;
	private Boolean digitada;
	private Boolean excluida;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Apuracao getApuracao() {
		return apuracao;
	}

	public void setApuracao(Apuracao apuracao) {
		this.apuracao = apuracao;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public MotivoMarcacao getMotivo() {
		return motivo;
	}

	public void setMotivo(MotivoMarcacao motivo) {
		this.motivo = motivo;
	}

	public Boolean isDigitada() {
		return digitada;
	}

	public void setDigitada(Boolean digitada) {
		this.digitada = digitada;
	}

	public Boolean isExcluida() {
		return excluida;
	}

	public void setExcluida(Boolean excluida) {
		this.excluida = excluida;
	}

}
