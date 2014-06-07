package br.udesc.ads.ponto.entidades;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

@Entity
public class Apuracao {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(insertable = true, updatable = false, nullable = false)
	private Colaborador colaborador;

	@Column(columnDefinition = "DATE")
	private LocalDate data;

	private Usuario responsavelAprovacao;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime dataAprovacao;

	private String observacoes;
	private BigDecimal horasTrabalhadas;
	private BigDecimal horasFaltantes;
	private BigDecimal horasExcedentes;
	private BigDecimal horasAbonadas;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "apuracao")
	private List<Marcacao> marcacoes = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "apuracao")
	private List<Abono> abonos = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER, targetClass = Ocorrencia.class)
	// @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<Ocorrencia> ocorrencias = new ArrayList<>();

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

	public void addMarcacao(Marcacao marcacao) {
		this.marcacoes.add(marcacao);
		if (marcacao.getApuracao() != this) {
			marcacao.setApuracao(this);
		}
	}

	public int getMarcacoesSize() {
		return this.marcacoes.size();
	}

	public Marcacao getMarcacao(int index) {
		return this.marcacoes.get(index);
	}

	public boolean containsMarcacao(Marcacao marcacao) {
		return this.marcacoes.contains(marcacao);
	}

	public void addAbono(Abono abono) {
		this.abonos.add(abono);
		if (abono.getApuracao() != this) {
			abono.setApuracao(this);
		}
	}
	
	public int getAbonosSize() {
		return this.abonos.size();
	}
	
	public Abono getAbono(int index) {
		return this.abonos.get(index);
	}

	public boolean containsAbono(Abono abono) {
		return this.abonos.contains(abono);
	}

	public void addOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencias.add(ocorrencia);
	}

	public int getOcorrenciasSize() {
		return this.ocorrencias.size();
	}

	public Ocorrencia getOcorrencia(int index) {
		return this.ocorrencias.get(index);
	}

}
