package br.udesc.ads.ponto.web;

import java.io.Serializable;

public class MarcacaoPonto implements Serializable {

	private static final long serialVersionUID = 313262235544679639L;

	private Long id;
	
	private Long apuracao;
	
	private String hora;
	
	private Long motivo = 0L;
	
	private Boolean digitada = Boolean.FALSE;
	
	private Boolean excluida = Boolean.FALSE;
	
	private String origem;
	
	public MarcacaoPonto() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApuracao() {
		return apuracao;
	}

	public void setApuracao(Long apuracao) {
		this.apuracao = apuracao;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Long getMotivo() {
		return motivo;
	}

	public void setMotivo(Long motivo) {
		this.motivo = motivo;
	}

	public Boolean getDigitada() {
		return digitada;
	}

	public void setDigitada(Boolean digitada) {
		this.digitada = digitada;
	}

	public Boolean getExcluida() {
		return excluida;
	}

	public void setExcluida(Boolean excluida) {
		this.excluida = excluida;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}
}
