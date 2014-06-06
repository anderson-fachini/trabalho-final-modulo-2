package br.udesc.ads.ponto.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.joda.time.LocalTime;

@Entity
public class Marcacao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Apuracao apuracao;
	
	@Column(columnDefinition = "TIME")
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
