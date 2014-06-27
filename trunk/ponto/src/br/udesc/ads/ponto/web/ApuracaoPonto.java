package br.udesc.ads.ponto.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ApuracaoPonto implements Serializable {
	
	private static final long serialVersionUID = 1981594807157118273L;
	
	private Long id;

	private String diaMes;
	
	private String diaSemana;
	
	private List<String> marcacoes;
	
	private List<String> ocorrencias;
	
	private List<String> abonos;
	
	private boolean inconsistente;
	
	private boolean exigeConfirmacao;
	
	private boolean podeAbonar;
	
	public ApuracaoPonto() {
		marcacoes = new ArrayList<String>();
		ocorrencias = new ArrayList<String>();
		abonos = new ArrayList<String>();
	}

	public String getDiaMes() {
		return diaMes;
	}

	public void setDiaMes(String diaMes) {
		this.diaMes = diaMes;
	}

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public List<String> getMarcacoes() {
		return marcacoes;
	}

	public void setMarcacoes(List<String> marcacoes) {
		this.marcacoes = marcacoes;
	}
	
	public void addMarcacao(String marcacao) {
		marcacoes.add(marcacao);
	}

	public List<String> getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(List<String> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}
	
	public void addOcorrencia(String ocorrencia) {
		ocorrencias.add(ocorrencia);
	}

	public boolean isInconsistente() {
		return inconsistente;
	}

	public void setInconsistente(boolean inconsistente) {
		this.inconsistente = inconsistente;
	}

	public List<String> getAbonos() {
		return abonos;
	}

	public void setAbonos(List<String> abonos) {
		this.abonos = abonos;
	}
	
	public void addAbono(String abono) {
		abonos.add(abono);
	}

	public boolean isExigeConfirmacao() {
		return exigeConfirmacao;
	}

	public void setExigeConfirmacao(boolean exigeConfirmacao) {
		this.exigeConfirmacao = exigeConfirmacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isPodeAbonar() {
		return podeAbonar;
	}

	public void setPodeAbonar(boolean podeAbonar) {
		this.podeAbonar = podeAbonar;
	}
	
}
