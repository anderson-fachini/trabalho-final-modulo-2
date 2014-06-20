package br.udesc.ads.ponto.web;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.joda.time.LocalDate;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;

@ManagedBean(name = "ajustePonto")
@SessionScoped
public class AjustePontoController {
	
	private List<Apuracao> apuracoes;
	private Colaborador colaboradorSelecionado;
	private LocalDate dataInicio;
	private LocalDate dataFim;
		
	public AjustePontoController() {
		
	}
	
	private void buscaApuracoes() {
		//TODO
	}

	public List<Apuracao> getApuracoes() {
		return apuracoes;
	}

	public void setApuracoes(List<Apuracao> apuracoes) {
		this.apuracoes = apuracoes;
	}

	public Colaborador getColaboradorSelecionado() {
		return colaboradorSelecionado;
	}

	public void setColaboradorSelecionado(Colaborador colaboradorSelecionado) {
		this.colaboradorSelecionado = colaboradorSelecionado;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}
	
}
