package br.udesc.ads.ponto.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import org.joda.time.LocalDateTime;

@Entity
public class AjusteBH {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JoinColumn(nullable = false)
	private Colaborador colaborador;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime dataHora;

	private Double saldoAnterior; // Pode ser positivo ou negativo

	private Double valorAjuste; // Pode ser positivo ou negativo

	@Column(length = 500)
	private String observacoes;

	private Usuario responsavel;

	private Apuracao apuracao; // Somente preenchido quando foi um ajuste
								// "autom�tico"

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public Double getSaldoAnterior() {
		return saldoAnterior;
	}

	public void setSaldoAnterior(Double saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}

	public Double getValorAjuste() {
		return valorAjuste;
	}

	public void setValorAjuste(Double valorAjuste) {
		this.valorAjuste = valorAjuste;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public Apuracao getApuracao() {
		return apuracao;
	}

	public void setApuracao(Apuracao apuracao) {
		this.apuracao = apuracao;
	}

}
