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

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Marcacao;
import br.udesc.ads.ponto.entidades.Ocorrencia;
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
	private List<ApuracaoPonto> apuracoesPonto;
	private List<SelectItem> colaboradores;
	private Map<Long, Colaborador> colaboradoresMapa;
	
	private Long codColabSelecionado;
	private Date dataInicial;
	private Date dataFinal;
	private Date maxDate = new Date(System.currentTimeMillis());
	
	private boolean apenasExcecoes = true;
	private boolean buscouApuracoes = false;
	
	private String quebraLinha = "<br />";
		
	public AjustePontoController() {
		buscaListaColaboradores();
	}
	
	public List<String> getSituacoes(List<Ocorrencia> ocorrencias) {
		
		
		return null;
	}
	
	public void buscaApuracoes() {
		boolean temErros = validaBusca();
		
		if (!temErros) {
			apuracoes = ApuracaoService.get().getApuracoesPorPeriodo(LocalDate.fromDateFields(dataInicial), 
																	 LocalDate.fromDateFields(dataFinal), 
																	 colaboradoresMapa.get(codColabSelecionado));
			buscouApuracoes = true;
			
			converteApuracoesParaApuracaoPonto();
		}
	}
	
	private void converteApuracoesParaApuracaoPonto() {
		apuracoesPonto = new ArrayList<ApuracaoPonto>();
		
		ApuracaoPonto ap;
		for (Apuracao a : apuracoes) {
			ap = new ApuracaoPonto();
			ap.setDiaMes(DataConverter.formataData(a.getData().toDate(), DataConverter.formatoDDMM));
			ap.setDiaSemana(DataConverter.formataData(a.getData().toDate(), DataConverter.formatoDiaSemanaExtenso));
			
			for (Marcacao m : a.getMarcacoes()) {
				ap.addMarcacao(DataConverter.formataData(m.getHora().toDateTimeToday().toDate(), DataConverter.formatoHHMM));
			}
			
			String formatoOcorrencia = "%s - %s";
			
			if (a.getHorasTrabalhadas().getMillisOfDay() > 0) {
				ap.addOcorrencia(String.format(formatoOcorrencia,
						DataConverter.formataData(a.getHorasTrabalhadas().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
						Messages.getString("horasNormais")));
			}
			
			if (a.getHorasAbonadas().getMillisOfDay() > 0) {
				ap.addOcorrencia(String.format(formatoOcorrencia,
						DataConverter.formataData(a.getHorasAbonadas().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
						Messages.getString("horasAbonadas")));
			}
			
			for (Ocorrencia o : a.getOcorrencias()) {
				if (o.getId() == 0) {
					continue;
					// TODO - remover isso quando for ajustada a apuração
				}
				
				switch (o) {
					case HORAS_EXCEDENTES: {
						ap.addOcorrencia(String.format(formatoOcorrencia, 
								DataConverter.formataData(a.getHorasExcedentes().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
								o.getDescricao()));
						break;
					}
					case HORAS_FALTANTES: {
						ap.addOcorrencia(String.format(formatoOcorrencia, 
								DataConverter.formataData(a.getHorasFaltantes().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
								o.getDescricao()));
						break;
					}
					
					default: {
						ap.addOcorrencia(o.getDescricao());
					}
				}
			}
			
			apuracoesPonto.add(ap);
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
		Colaborador colaborador = usuarioAutenticado.getColaborador(); 
		Setor setor = null;
		
		if (colaborador != null) {
			setor = colaborador.getSetor();
		}
		
		colaboradores = new ArrayList<SelectItem>();
		colaboradores.add(new SelectItem(0L, "-- Selecione --"));
		
		colaboradoresMapa = new HashMap<Long, Colaborador>();
		
		for (Colaborador c : ColaboradorService.get().getColaboradoresPorSetor(setor)) {
			colaboradores.add(new SelectItem(c.getId(), c.getNome()));
			colaboradoresMapa.put(c.getId(), c);
		}
	}

	public List<ApuracaoPonto> getApuracoesPonto() {
		return apuracoesPonto;
	}

	public void setApuracoesPonto(List<ApuracaoPonto> apuracoesPonto) {
		this.apuracoesPonto = apuracoesPonto;
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

	public String getQuebraLinha() {
		return quebraLinha;
	}

	public void setQuebraLinha(String quebraLinha) {
		this.quebraLinha = quebraLinha;
	}
	
}