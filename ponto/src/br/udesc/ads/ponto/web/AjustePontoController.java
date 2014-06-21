package br.udesc.ads.ponto.web;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Marcacao;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.servicos.ApuracaoService;
import br.udesc.ads.ponto.servicos.ColaboradorService;
import br.udesc.ads.ponto.util.DataConverter;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "ajustePonto")
@SessionScoped
public class AjustePontoController implements Serializable {
	
	private static final long serialVersionUID = 6585193392870549078L;
	
	private List<Apuracao> apuracoes;
	private List<SelectItem> colaboradores;
	private Map<Long, Colaborador> colaboradoresMapa;
	
	private Long codColabSelecionado;
	private Date dataInicial;
	private Date dataFinal;
	private Date maxDate = new Date(System.currentTimeMillis());
	
	private boolean apenasExcecoes = true;
	private boolean buscouApuracoes = false;
		
	public AjustePontoController() {
		buscaListaColaboradores();
	}
	
	public void buscaApuracoes() {
		boolean temErros = validaBusca();
		
		if (!temErros) {
			apuracoes = ApuracaoService.get().getApuracoesPorPeriodo(LocalDate.fromDateFields(dataInicial), 
																	 LocalDate.fromDateFields(dataFinal), 
																	 colaboradoresMapa.get(codColabSelecionado));
			buscouApuracoes = true;
		}
	}
	
	private boolean validaBusca() {
		boolean temErros = false;
		
		if (codColabSelecionado == 0) {
			temErros = true;
			JsfUtils.addMensagemWarning(Messages.getString("msgSelecionarColaborador"));
		}
		
		if (dataInicial.after(dataFinal)) {
			temErros = true;
			JsfUtils.addMensagemWarning(Messages.getString("msgDataInicialMaiorIgual"));
		}
		
		return temErros;
	}
	
	private void buscaListaColaboradores() {
		Usuario usuarioAutenticado = new MenuController().getUsuarioAutenticado();
		Setor setor = usuarioAutenticado.getColaborador().getSetor();
		
		colaboradores = new ArrayList<SelectItem>();
		colaboradores.add(new SelectItem(0L, "-- Selecione --"));
		
		colaboradoresMapa = new HashMap<Long, Colaborador>();
		
		for (Colaborador c : ColaboradorService.get().getColaboradoresPorSetor(setor)) {
			colaboradores.add(new SelectItem(c.getId(), c.getNome()));
			colaboradoresMapa.put(c.getId(), c);
		}
	}

	public List<Apuracao> getApuracoes() {
		return apuracoes;
	}

	public void setApuracoes(List<Apuracao> apuracoes) {
		this.apuracoes = apuracoes;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicio) {
		this.dataInicial = dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFim) {
		this.dataFinal = dataFim;
	}

	public List<SelectItem> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<SelectItem> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public Long getCodColabSelecionado() {
		return codColabSelecionado;
	}

	public void setCodColabSelecionado(Long codColabSelecionado) {
		this.codColabSelecionado = codColabSelecionado;
	}

	public boolean isApenasExcecoes() {
		return apenasExcecoes;
	}

	public void setApenasExcecoes(boolean apenasExcecoes) {
		this.apenasExcecoes = apenasExcecoes;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	
	public boolean isBuscouApuracoes() {
		return buscouApuracoes;
	}

	public void setBuscouApuracoes(boolean buscouApuracoes) {
		this.buscouApuracoes = buscouApuracoes;
	}

	public String getDataFormatadaDiaMes(LocalDate data) {
		return DataConverter.formataData(data.toDate(), DataConverter.formatoDDMM);
	}
	
	public String getDiaSemanaData(LocalDate data) {
		return DataConverter.formataData(data.toDate(), DataConverter.formatoDiaSemanaExtenso);
	}
	
	public String getHoraFormatada(LocalTime hora) {
		return DataConverter.formataData(hora.toDateTimeToday().toDate(), DataConverter.formatoHHMM);
	}
	
}