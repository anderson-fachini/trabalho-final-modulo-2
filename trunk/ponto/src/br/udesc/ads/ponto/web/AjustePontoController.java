package br.udesc.ads.ponto.web;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.Abono;
import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Marcacao;
import br.udesc.ads.ponto.entidades.MotivoAbono;
import br.udesc.ads.ponto.entidades.Ocorrencia;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.servicos.ApuracaoService;
import br.udesc.ads.ponto.servicos.ColaboradorService;
import br.udesc.ads.ponto.servicos.MotivoAbonoService;
import br.udesc.ads.ponto.util.DataConverter;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "ajustePonto")
@SessionScoped
public class AjustePontoController implements Serializable {
	
	private static final long serialVersionUID = 6585193392870549078L;

	private List<ApuracaoPonto> apuracoesPonto;
	private List<SelectItem> colaboradores;
	private Map<Long, Colaborador> colaboradoresMapa;
	private Map<Long, Apuracao> apuracoesMap;
	private List<MotivoAbono> motivosAbono;
	
	private Long codColabSelecionado;
	private Long idMotivoAbonoAdicionar;
	
	private Date dataInicial;
	private Date dataFinal;
	private Date maxDate = new Date(System.currentTimeMillis());
	private Date horaInicioAbono;
	private Date horaFimAbono;
	
	private Apuracao apuracaoSelecionada;
	private Abono abonoAdicionar;
	
	private boolean apenasExcecoes = true;
	private boolean buscouApuracoes = false;
	private boolean popupAprovarApuracaoOpened = false;
	private boolean popupAbonarApuracaoOpened = false;
		
	public AjustePontoController() {
		buscaListaColaboradores();
		motivosAbono = MotivoAbonoService.get().getMotivosAbono();
	}
	
	public List<String> getSituacoes(List<Ocorrencia> ocorrencias) {
		
		
		return null;
	}
	
	public void abonarApuracaoSetaSelecionada(Long id) {
		togglePopupAbonarApuracaoOpened();
		apuracaoSelecionada = apuracoesMap.get(id);
	}
	
	public void abonarApuracaoSelecionadaSalvar() {
//		Abono abono = new Abono();
//		abono.setHoraInicio(LocalTime.fromDateFields(horaInicioAbono));
//		abono.setHoraFim(LocalTime.fromDateFields(horaFimAbono));
//		abono.setApuracao(apuracaoSelecionada);
//		abono.setMotivo(motivoAbonoSelecionado);
//		
//		apuracaoSelecionada.addAbono(abono);
//		
//		apuracoesMap.put(apuracaoSelecionada.getId(), apuracaoSelecionada);
//		
//		geraListaApuracoesPonto();
//		
//		togglePopupAbonarApuracaoOpened();
	}
	
	public void aprovarApuracaoSetaSelecionada(Long id) {		
		togglePopupAprovarApuracaoOpened();
		apuracaoSelecionada = apuracoesMap.get(id);
	}
	
	public void aprovarApuracaoSelecionadaSalvar() {
		apuracaoSelecionada.setDataConfirmacao(new LocalDateTime());
		apuracaoSelecionada.setResponsavelConfirmacao(new MenuController().getUsuarioAutenticado());
		apuracaoSelecionada.setExigeConfirmacao(false);
		
		apuracoesMap.put(apuracaoSelecionada.getId(), apuracaoSelecionada);
		
		geraListaApuracoesPonto();
		
		togglePopupAprovarApuracaoOpened();
	}
	
	public void buscaApuracoes() {
		boolean temErros = validaBusca();
		
		if (!temErros) {
			List<Apuracao> apuracoes = ApuracaoService.get().getApuracoesPorPeriodo(LocalDate.fromDateFields(dataInicial), 
																	 LocalDate.fromDateFields(dataFinal), 
																	 colaboradoresMapa.get(codColabSelecionado));
			
			apuracoesMap = new LinkedHashMap<Long, Apuracao>();
			for (Apuracao apuracao : apuracoes) {
				if (apenasExcecoes && !apuracao.getExigeConfirmacao())
					continue;
				
				apuracoesMap.put(apuracao.getId(), apuracao);
			}
			
			buscouApuracoes = true;
			
			geraListaApuracoesPonto();
		}
	}
	
	private void geraListaApuracoesPonto() {
		apuracoesPonto = new ArrayList<ApuracaoPonto>();
		
		String formatoOcorrencia = "%s - %s";
		String formatoAbono = "%s - %s: %s";
		ApuracaoPonto apuracaoPonto;
		
		for (Apuracao apuracao : apuracoesMap.values()) {
			apuracoesMap.put(apuracao.getId(), apuracao);
			
			apuracaoPonto = new ApuracaoPonto();
			apuracaoPonto.setId(apuracao.getId());
			apuracaoPonto.setDiaMes(DataConverter.formataData(apuracao.getData().toDate(), DataConverter.formatoDDMM));
			apuracaoPonto.setDiaSemana(DataConverter.formataData(apuracao.getData().toDate(), DataConverter.formatoDiaSemanaExtenso));
			apuracaoPonto.setExigeConfirmacao(apuracao.getExigeConfirmacao());
			
			for (Marcacao marcacao : apuracao.getMarcacoes()) {
				apuracaoPonto.addMarcacao(DataConverter.formataData(marcacao.getHora().toDateTimeToday().toDate(), DataConverter.formatoHHMM));
			}
			
			LocalTime horasTrabalhadas = apuracao.getHorasTrabalhadas(); 
			if (horasTrabalhadas != null && horasTrabalhadas.getMillisOfDay() > 0) {
				apuracaoPonto.addOcorrencia(String.format(formatoOcorrencia,
						DataConverter.formataData(apuracao.getHorasTrabalhadas().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
						Messages.getString("horasNormais")));
			}
			
			LocalTime horasAbonadas = apuracao.getHorasAbonadas(); 
			if (horasAbonadas != null && horasAbonadas.getMillisOfDay() > 0) {
				apuracaoPonto.addOcorrencia(String.format(formatoOcorrencia,
						DataConverter.formataData(apuracao.getHorasAbonadas().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
						Messages.getString("horasAbonadas")));
			}
			
			List<Ocorrencia> ocorrencias = apuracao.getOcorrencias();
			
			if (isOcorrenciasPrecisamAjuste(ocorrencias)) {
				apuracaoPonto.setPrecisaAjustar(true);
			}
			
			for (Ocorrencia ocorrencia : ocorrencias) {				
				switch (ocorrencia) {
					case HORAS_EXCEDENTES: {
						apuracaoPonto.addOcorrencia(String.format(formatoOcorrencia, 
								DataConverter.formataData(apuracao.getHorasExcedentes().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
								ocorrencia.getDescricao()));
						break;
					}
					case HORAS_FALTANTES: {
						apuracaoPonto.addOcorrencia(String.format(formatoOcorrencia, 
								DataConverter.formataData(apuracao.getHorasFaltantes().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
								ocorrencia.getDescricao()));
						break;
					}					
					default: {
						apuracaoPonto.addOcorrencia(ocorrencia.getDescricao());
					}
				}
			}
			
			for (Abono abono : apuracao.getAbonos()) {
				apuracaoPonto.addAbono(String.format(formatoAbono, 
						DataConverter.formataData(abono.getHoraInicio().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
						DataConverter.formataData(abono.getHoraFim().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
						abono.getMotivo().getDescricao()));
			}
			
			apuracoesPonto.add(apuracaoPonto);
		}
	}
	
	private boolean isOcorrenciasPrecisamAjuste(List<Ocorrencia> ocorrencias) {
		return ocorrencias.contains(Ocorrencia.HORAS_FALTANTES) ||
			   ocorrencias.contains(Ocorrencia.MARCACOES_FALTANTES) ||
			   ocorrencias.contains(Ocorrencia.MARCACOES_EXCEDENTES);
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
		
		for (Colaborador c : ColaboradorService.get().getColaboradoresAtivosPorSetor(setor)) {
			colaboradores.add(new SelectItem(c.getId(), c.getNome()));
			colaboradoresMapa.put(c.getId(), c);
		}
	}
	
	public void togglePopupAbonarApuracaoOpened() {
		popupAbonarApuracaoOpened = !popupAbonarApuracaoOpened;
		
		apuracaoSelecionada = new Apuracao();
	}
	
	public void togglePopupAprovarApuracaoOpened() {
		popupAprovarApuracaoOpened = !popupAprovarApuracaoOpened;
		
		apuracaoSelecionada = new Apuracao();
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

	public Apuracao getApuracaoSelecionada() {
		return apuracaoSelecionada;
	}

	public void setApuracaoSelecionada(Apuracao apuracaoSelecionada) {
		this.apuracaoSelecionada = apuracaoSelecionada;
	}

	public boolean isPopupAprovarApuracaoOpened() {
		return popupAprovarApuracaoOpened;
	}

	public void setPopupAprovarApuracaoOpened(boolean popupAprovarApuracaoOpened) {
		this.popupAprovarApuracaoOpened = popupAprovarApuracaoOpened;
	}

	public List<MotivoAbono> getMotivosAbono() {
		return motivosAbono;
	}

	public void setMotivosAbono(List<MotivoAbono> motivosAbono) {
		this.motivosAbono = motivosAbono;
	}

	public Date getDataInicioAbono() {
		return horaInicioAbono;
	}

	public void setDataInicioAbono(Date dataInicioAbono) {
		this.horaInicioAbono = dataInicioAbono;
	}

	public Date getDataFimAbono() {
		return horaFimAbono;
	}

	public void setDataFimAbono(Date dataFimAbono) {
		this.horaFimAbono = dataFimAbono;
	}

	public boolean isPopupAbonarApuracaoOpened() {
		return popupAbonarApuracaoOpened;
	}

	public void setPopupAbonarApuracaoOpened(boolean popupAbonaApuracaoOpened) {
		this.popupAbonarApuracaoOpened = popupAbonaApuracaoOpened;
	}

	public void addAbonoMarcacaoSelecionada() {
		
	}

	public Abono getAbonoAdicionar() {
		return abonoAdicionar;
	}

	public void setAbonoAdicionar(Abono abonoAdicionar) {
		this.abonoAdicionar = abonoAdicionar;
	}

	public Long getIdMotivoAbonoAdicionar() {
		return idMotivoAbonoAdicionar;
	}

	public void setIdMotivoAbonoAdicionar(Long idAbonoAdicionar) {
		this.idMotivoAbonoAdicionar = idAbonoAdicionar;
	}
	
}