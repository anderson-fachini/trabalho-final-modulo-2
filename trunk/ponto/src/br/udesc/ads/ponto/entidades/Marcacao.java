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
public class Marcacao implements Serializable {

	private static final long serialVersionUID = 3492459317717648128L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(nullable = false, updatable = false)
	private Apuracao apuracao;

	@Column(columnDefinition = "TIME")
	private LocalTime hora;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	private MotivoMarcacao motivo;

	private Boolean digitada = Boolean.FALSE;
	
	private Boolean excluida = Boolean.FALSE;
	
	public Marcacao() {
	}
	
	public Marcacao(LocalTime hora) {
		this();
		this.hora = hora;
	}

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
		if (!apuracao.containsMarcacao(this)) {
			apuracao.addMarcacao(this);
		}
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
