package br.udesc.ads.ponto.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "colaborador_id", "data" }))
public class Apuracao implements Serializable {

	private static final long serialVersionUID = -9070252170202962578L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {}, optional = false)
	@JoinColumn(insertable = true, updatable = false, nullable = false)
	private Colaborador colaborador;

	@Column(columnDefinition = "DATE")
	private LocalDate data;

	private Usuario responsavelConfirmacao;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime dataConfirmacao;

	private String observacoes;

	@Column(columnDefinition = "TIME")
	private LocalTime horasTrabalhadas;

	@Column(columnDefinition = "TIME")
	private LocalTime horasFaltantes;

	@Column(columnDefinition = "TIME")
	private LocalTime horasExcedentes;

	@Column(columnDefinition = "TIME")
	private LocalTime horasAbonadas;

	private Boolean inconsistente;

	private Boolean apurada = Boolean.FALSE;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "apuracao")
	@OrderBy("hora")
	private List<Marcacao> marcacoes = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "apuracao")
	private List<Abono> abonos = new ArrayList<>();

	// TODO Ajustar os mapeamentos para que o EclipseLink utilize o OcorrenciaToIntConverter para persistir esse cara.
	@ElementCollection(fetch = FetchType.EAGER)
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

	public Usuario getResponsavelConfirmacao() {
		return responsavelConfirmacao;
	}

	public void setResponsavelConfirmacao(Usuario responsavelConfirmacao) {
		this.responsavelConfirmacao = responsavelConfirmacao;
	}

	public LocalDateTime getDataConfirmacao() {
		return dataConfirmacao;
	}

	public void setDataConfirmacao(LocalDateTime dataConfirmacao) {
		this.dataConfirmacao = dataConfirmacao;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public LocalTime getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public void setHorasTrabalhadas(LocalTime horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}

	public LocalTime getHorasFaltantes() {
		return horasFaltantes;
	}

	public void setHorasFaltantes(LocalTime horasFaltantes) {
		this.horasFaltantes = horasFaltantes;
	}

	public boolean temHorasFaltantes() {
		return this.horasFaltantes != null && this.horasFaltantes.getMillisOfDay() > 0;
	}

	public LocalTime getHorasExcedentes() {
		return horasExcedentes;
	}

	public void setHorasExcedentes(LocalTime horasExcedentes) {
		this.horasExcedentes = horasExcedentes;
	}

	public boolean temHorasExcedentes() {
		return this.horasExcedentes != null && this.horasExcedentes.getMillisOfDay() > 0;
	}

	public LocalTime getHorasAbonadas() {
		return horasAbonadas;
	}

	public void setHorasAbonadas(LocalTime horasAbonadas) {
		this.horasAbonadas = horasAbonadas;
	}

	public Boolean getInconsistente() {
		return inconsistente;
	}

	public void setInconsistente(Boolean inconsistente) {
		this.inconsistente = inconsistente;
	}

	public Boolean getApurada() {
		return apurada;
	}

	public void setApurada(Boolean apurada) {
		this.apurada = apurada;
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
	
	public List<LocalTime> getSequenciaMarcacoes() {
		List<LocalTime> result = new ArrayList<>();
		for (int i = 0; i < this.getMarcacoesSize(); ++i) {
			Marcacao mar = this.getMarcacao(i);
			if (!mar.isExcluida()) {
				result.add(mar.getHora());
			}
		}
		Collections.sort(result);
		return result;
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

	public List<Marcacao> getMarcacoes() {
		return new ArrayList<Marcacao>(marcacoes);
	}
	
	public List<Ocorrencia> getOcorrencias() {
		return new ArrayList<Ocorrencia>(ocorrencias);
	}

}
