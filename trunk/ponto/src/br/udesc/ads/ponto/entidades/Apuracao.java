package br.udesc.ads.ponto.entidades;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class Apuracao {

	private Long id;
	private Colaborador colaborador;
	private LocalDate data;
	private Usuario responsavelAprovacao;
	private LocalDateTime dataAprovacao;
	private String observacoes;
	private BigDecimal horasTrabalhadas;
	private BigDecimal horasFaltantes;
	private BigDecimal horasExcedentes;
	private BigDecimal horasAbonadas;
	private List<Ocorrencia> ocorrencias;

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

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Usuario getResponsavelAprovacao() {
		return responsavelAprovacao;
	}

	public void setResponsavelAprovacao(Usuario responsavelAprovacao) {
		this.responsavelAprovacao = responsavelAprovacao;
	}

	public LocalDateTime getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(LocalDateTime dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public BigDecimal getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public void setHorasTrabalhadas(BigDecimal horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}

	public BigDecimal getHorasFaltantes() {
		return horasFaltantes;
	}

	public void setHorasFaltantes(BigDecimal horasFaltantes) {
		this.horasFaltantes = horasFaltantes;
	}

	public BigDecimal getHorasExcedentes() {
		return horasExcedentes;
	}

	public void setHorasExcedentes(BigDecimal horasExcedentes) {
		this.horasExcedentes = horasExcedentes;
	}

	public BigDecimal getHorasAbonadas() {
		return horasAbonadas;
	}

	public void setHorasAbonadas(BigDecimal horasAbonadas) {
		this.horasAbonadas = horasAbonadas;
	}

	public List<Ocorrencia> getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(List<Ocorrencia> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

}
