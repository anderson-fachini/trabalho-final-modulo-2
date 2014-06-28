package br.udesc.ads.ponto.web;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

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
import br.udesc.ads.ponto.entidades.MotivoMarcacao;
import br.udesc.ads.ponto.entidades.Ocorrencia;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.servicos.ApuracaoService;
import br.udesc.ads.ponto.servicos.ColaboradorService;
import br.udesc.ads.ponto.servicos.MotivoAbonoService;
import br.udesc.ads.ponto.servicos.MotivoMarcacaoService;
import br.udesc.ads.ponto.util.DataConverter;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "ajustePonto")
@SessionScoped
public class AjustePontoController implements Serializable {
	
	private static final String FORMATO_ABONO = "%s - %s: %s";
	private static final String FORMATO_OCORRENCIA = "%s - %s";
	private static final String FORMATO_SELECIONE_ITEM = "-- %s --";

	private static final long serialVersionUID = 6585193392870549078L;

	private List<ApuracaoPonto> apuracoesPonto;
	private List<MarcacaoPonto> marcacoes;
	private List<MarcacaoPonto> marcacoesOriginais;
	private List<SelectItem> colaboradores;
	private List<SelectItem> motivosAbono;
	private List<SelectItem> motivosMarcacaoPonto;
	private Map<Long, Colaborador> colaboradoresMapa;
	private Map<Long, Apuracao> apuracoesMap;
	private Map<Long, MotivoAbono> motivosAbonoMap;
	private Map<Long, MotivoMarcacao> motivosMarcacaoMap;
	
	private Set<Apuracao> apuracoesConfirmadas;
	
	private Long codColabSelecionado;
	private Long idMotivoAbonoAdicionar;
	private Long idMotivoMarcacao;
	
	private Date dataInicial;
	private Date dataFinal;
	private Date maxDate = new Date(System.currentTimeMillis());
	private Date horaInicioAbono;
	private Date horaFimAbono;
	private Date horaMarcacao;
	
	private Apuracao apuracaoSelecionada;
	
	private boolean apenasExcecoes = true;
	private boolean buscouApuracoes = false;
	private boolean popupAprovarApuracaoOpened = false;
	private boolean popupAbonarApuracaoOpened = false;
	private boolean popupAlterarMarcacoesOpened = false;
		
	public AjustePontoController() {
		idMotivoAbonoAdicionar = 0L;
		idMotivoMarcacao = 0L;
		horaInicioAbono = new Date();
		horaFimAbono = new Date();
		horaMarcacao = new Date();
		
		buscaListaColaboradores();
		carregaListasMotivoAbono();	
		buscaMotivosMarcacaoPonto();
	}
	
	private void carregaListasMotivoAbono() {
		motivosAbonoMap = new HashMap<Long, MotivoAbono>();
		
		motivosAbono = new ArrayList<SelectItem>();
		motivosAbono.add(new SelectItem(0L, String.format(FORMATO_SELECIONE_ITEM, Messages.getString("selecione"))));
		
		boolean apenasAtivos = true;
		for (MotivoAbono motivo : MotivoAbonoService.get().getMotivosAbono(apenasAtivos)) {
			motivosAbono.add(new SelectItem(motivo.getId(), motivo.getDescricao()));
			motivosAbonoMap.put(motivo.getId(), motivo);
		}
	}
	
	public void confirmarAjustes() {
		Usuario usuario = new MenuController().getUsuarioAutenticado();
		
		Iterator<Apuracao> iterator = apuracoesConfirmadas.iterator(); 
		while (iterator.hasNext()) {
			ApuracaoService.get().aprovarApuracao(iterator.next(), usuario);
		}
		
		JsfUtils.addMensagemInfo(Messages.getString("msgApuracoesConfirmadasSucesso"));
		
		buscaApuracoes();
	}
	
	private void buscaMotivosMarcacaoPonto() {
		motivosMarcacaoPonto = new ArrayList<SelectItem>();
		motivosMarcacaoPonto.add(new SelectItem(0L, String.format(FORMATO_SELECIONE_ITEM, Messages.getString("selecione"))));
		
		motivosMarcacaoMap = new HashMap<Long, MotivoMarcacao>();
		
		List<MotivoMarcacao> motivos = MotivoMarcacaoService.get().getMotivosMarcacao();
		for (MotivoMarcacao motivo : motivos) {
			motivosMarcacaoPonto.add(new SelectItem(motivo.getId(), motivo.getDescricao()));
			motivosMarcacaoMap.put(motivo.getId(), motivo);
		}
	}
	
	public void alterarMarcacoesSetaApuracaoSelecionada(Long id) {
		apuracaoSelecionada = apuracoesMap.get(id);
		
		marcacoesOriginais = new ArrayList<MarcacaoPonto>();
		for (Marcacao marcacao : apuracaoSelecionada.getMarcacoes()) {
			if (!marcacao.isExcluida()) {
				MarcacaoPonto marcacaoPonto = new MarcacaoPonto();
				marcacaoPonto.setId(marcacao.getId());
				marcacaoPonto.setHora(DataConverter.formataData(marcacao.getHora().toDateTimeToday().toDate(), 
						DataConverter.formatoHHMM));
				marcacaoPonto.setDigitada(marcacao.isDigitada());
				marcacaoPonto.setExcluida(marcacao.isExcluida());
				
				MotivoMarcacao motivo = marcacao.getMotivo();
				marcacaoPonto.setMotivo(motivo != null ? motivo.getId() : 0L);
				marcacaoPonto.setOrigem(marcacao.isDigitada() ? Messages.getString("digitada") : Messages.getString("eletronica"));
				
				marcacoesOriginais.add(marcacaoPonto);
			}
		}
		
		geraListaMarcacoes();
		
		togglePopupAlterarMarcacoesOpened();
	}
	
	private void geraListaMarcacoes() {
		marcacoes = new ArrayList<MarcacaoPonto>();
		
		for (MarcacaoPonto marcacao : marcacoesOriginais) {
			if (!marcacao.getExcluida()) {
				marcacoes.add(marcacao);
			}
		}
	}
	
	public void excluiMarcacao(MarcacaoPonto marcacaoPonto) {
		if (marcacaoPonto.getMotivo().equals(0L)) {
			JsfUtils.addMensagemWarning(Messages.getString("msgInformeMotivoAjuste"));
		} else {
			if (marcacaoPonto.getId() == null) {
				marcacoesOriginais.remove(marcacaoPonto);
			} else {
				for (MarcacaoPonto marcacao : marcacoesOriginais) {
					if (marcacao.getId().equals(marcacaoPonto.getId())) {
						marcacao.setExcluida(true);
						marcacao.setAlterada(true);
						break;
					}
				}
			}
			
			geraListaMarcacoes();
		}
	}
	
	public void incluirMarcacao() {
		if (idMotivoMarcacao.equals(0L)) {
			JsfUtils.addMensagemWarning(Messages.getString("msgInformeMotivoAjuste"));
		} else {
			MarcacaoPonto marcacao = new MarcacaoPonto();
			marcacao.setDigitada(true);
			marcacao.setExcluida(false);
			marcacao.setHoraDate(horaMarcacao);
			marcacao.setHora(DataConverter.formataData(horaMarcacao, DataConverter.formatoHHMM));
			marcacao.setMotivo(idMotivoMarcacao);
			marcacao.setOrigem(Messages.getString("digitada"));
			marcacao.setAlterada(true);
			
			marcacoesOriginais.add(marcacao);
			
			idMotivoMarcacao = 0L;
			geraListaMarcacoes();
		}
	}
	
	public void salvaAlteracaoMarcacoes() {		
		for (MarcacaoPonto marcacaoPonto : marcacoesOriginais) {
			if (marcacaoPonto.getAlterada()) {
				// significa que ela é nova
				if (marcacaoPonto.getId() == null) {
					Marcacao marcacao = new Marcacao();
					marcacao.setDigitada(marcacaoPonto.getDigitada());
					marcacao.setExcluida(marcacaoPonto.getExcluida());
					marcacao.setHora(LocalTime.fromDateFields(marcacaoPonto.getHoraDate()));
					marcacao.setMotivo(motivosMarcacaoMap.get(marcacaoPonto.getMotivo()));
					
					apuracaoSelecionada.addMarcacao(marcacao);
				} else {
					for (Marcacao marcacao : apuracaoSelecionada.getMarcacoes()) {
						if (marcacao.getId().equals(marcacaoPonto.getId())) {
							marcacao.setExcluida(true);
							marcacao.setMotivo(motivosMarcacaoMap.get(marcacaoPonto.getMotivo()));
						}
					}
				}
			}
		}
		
		ApuracaoService.get().apurarMarcacoes(apuracaoSelecionada);
		
		apuracoesMap.put(apuracaoSelecionada.getId(), apuracaoSelecionada);
		
		geraListaApuracoesPonto();
		
		togglePopupAlterarMarcacoesOpened();
	}
	
	public void abonarApuracaoSetaSelecionada(Long id) {
		togglePopupAbonarApuracaoOpened();
		apuracaoSelecionada = apuracoesMap.get(id);
	}
	
	public void abonarApuracaoSelecionadaSalvar() {
		boolean temErros = validaAbonarApuracao();
		
		if (!temErros) {	
			Abono abono = new Abono();
			abono.setHoraInicio(LocalTime.fromDateFields(horaInicioAbono));
			abono.setHoraFim(LocalTime.fromDateFields(horaFimAbono));
			// Não pode setar a apuração, senão o abono aparece duplicado
			//abono.setApuracao(apuracaoSelecionada);
			abono.setMotivo(motivosAbonoMap.get(idMotivoAbonoAdicionar));
			
			apuracaoSelecionada.addAbono(abono);
			
			ApuracaoService.get().apurarMarcacoes(apuracaoSelecionada);
			
			apuracoesMap.put(apuracaoSelecionada.getId(), apuracaoSelecionada);
			
			geraListaApuracoesPonto();
			
			togglePopupAbonarApuracaoOpened();
		}
	}
	
	private boolean validaAbonarApuracao() {
		boolean temErros = false;
		
		if (horaInicioAbono.equals(horaFimAbono) || horaInicioAbono.after(horaFimAbono)) {
			temErros = true;
			JsfUtils.addMensagemWarning(Messages.getString("msgHoraInicialDeveSerMenor"));
		}
		
		if (idMotivoAbonoAdicionar == 0L) {
			temErros = true;
			JsfUtils.addMensagemWarning(Messages.getString("msgSelecionarMotivoAbono"));
		}
		
		return temErros;
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
		apuracoesConfirmadas.add(apuracaoSelecionada);
		
		geraListaApuracoesPonto();
		
		togglePopupAprovarApuracaoOpened();
	}
	
	public void buscaApuracoes() {
		boolean temErros = validaBusca();
		
		if (!temErros) {
			apuracoesConfirmadas = new HashSet<Apuracao>();
			
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
		
		ApuracaoPonto apuracaoPonto;
		
		for (Apuracao apuracao : apuracoesMap.values()) {
			apuracoesMap.put(apuracao.getId(), apuracao);
			
			apuracaoPonto = new ApuracaoPonto();
			apuracaoPonto.setId(apuracao.getId());
			apuracaoPonto.setDiaMes(DataConverter.formataData(apuracao.getData().toDate(), DataConverter.formatoDDMM));
			apuracaoPonto.setDiaSemana(DataConverter.formataData(apuracao.getData().toDate(), DataConverter.formatoDiaSemanaExtenso));
			apuracaoPonto.setExigeConfirmacao(apuracao.getExigeConfirmacao());
			apuracaoPonto.setPodeAbonar(apuracao.getOcorrencias().contains(Ocorrencia.HORAS_FALTANTES));
			apuracaoPonto.setInconsistente(apuracao.getInconsistente());
			
			for (Marcacao marcacao : apuracao.getMarcacoes()) {
				apuracaoPonto.addMarcacao(DataConverter.formataData(marcacao.getHora().toDateTimeToday().toDate(), DataConverter.formatoHHMM));
			}
			
			LocalTime horasTrabalhadas = apuracao.getHorasTrabalhadas(); 
			if (horasTrabalhadas != null && horasTrabalhadas.getMillisOfDay() > 0) {
				apuracaoPonto.addOcorrencia(String.format(FORMATO_OCORRENCIA,
						DataConverter.formataData(apuracao.getHorasTrabalhadas().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
						Messages.getString("horasTrabalhadas")));
			}
			
			LocalTime horasAbonadas = apuracao.getHorasAbonadas(); 
			if (horasAbonadas != null && horasAbonadas.getMillisOfDay() > 0) {
				apuracaoPonto.addOcorrencia(String.format(FORMATO_OCORRENCIA,
						DataConverter.formataData(apuracao.getHorasAbonadas().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
						Messages.getString("horasAbonadas")));
			}
			
			List<Ocorrencia> ocorrencias = apuracao.getOcorrencias();
			
			for (Ocorrencia ocorrencia : ocorrencias) {				
				switch (ocorrencia) {
					case HORAS_EXCEDENTES: {
						apuracaoPonto.addOcorrencia(String.format(FORMATO_OCORRENCIA, 
								DataConverter.formataData(apuracao.getHorasExcedentes().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
								ocorrencia.getDescricao()));
						break;
					}
					case HORAS_FALTANTES: {
						apuracaoPonto.addOcorrencia(String.format(FORMATO_OCORRENCIA, 
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
				apuracaoPonto.addAbono(String.format(FORMATO_ABONO, 
						DataConverter.formataData(abono.getHoraInicio().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
						DataConverter.formataData(abono.getHoraFim().toDateTimeToday().toDate(), DataConverter.formatoHHMM),
						abono.getMotivo().getDescricao()));
			}
			
			apuracoesPonto.add(apuracaoPonto);
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
		
		for (Colaborador c : ColaboradorService.get().getColaboradoresAtivosPorSetor(setor)) {
			colaboradores.add(new SelectItem(c.getId(), c.getNome()));
			colaboradoresMapa.put(c.getId(), c);
		}
	}
	
	public void togglePopupAbonarApuracaoOpened() {
		popupAbonarApuracaoOpened = !popupAbonarApuracaoOpened;
		
		apuracaoSelecionada = new Apuracao();
	}
	
	public void togglePopupAlterarMarcacoesOpened() {
		popupAlterarMarcacoesOpened = !popupAlterarMarcacoesOpened;
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

	public List<SelectItem> getMotivosAbono() {
		return motivosAbono;
	}

	public void setMotivosAbono(List<SelectItem> motivosAbono) {
		this.motivosAbono = motivosAbono;
	}

	public boolean isPopupAbonarApuracaoOpened() {
		return popupAbonarApuracaoOpened;
	}

	public void setPopupAbonarApuracaoOpened(boolean popupAbonaApuracaoOpened) {
		this.popupAbonarApuracaoOpened = popupAbonaApuracaoOpened;
	}

	public void addAbonoMarcacaoSelecionada() {
		
	}

	public Long getIdMotivoAbonoAdicionar() {
		return idMotivoAbonoAdicionar;
	}

	public void setIdMotivoAbonoAdicionar(Long idAbonoAdicionar) {
		this.idMotivoAbonoAdicionar = idAbonoAdicionar;
	}

	public Date getHoraInicioAbono() {
		return horaInicioAbono;
	}

	public void setHoraInicioAbono(Date horaInicioAbono) {
		this.horaInicioAbono = horaInicioAbono;
	}

	public Date getHoraFimAbono() {
		return horaFimAbono;
	}

	public void setHoraFimAbono(Date horaFimAbono) {
		this.horaFimAbono = horaFimAbono;
	}

	public boolean isPopupAlterarMarcacoesOpened() {
		return popupAlterarMarcacoesOpened;
	}

	public void setPopupAlterarMarcacoesOpened(boolean popupAlterarMarcacoesOpened) {
		this.popupAlterarMarcacoesOpened = popupAlterarMarcacoesOpened;
	}

	public List<MarcacaoPonto> getMarcacoes() {
		return marcacoes;
	}

	public void setMarcacoes(List<MarcacaoPonto> marcacoes) {
		this.marcacoes = marcacoes;
	}

	public List<SelectItem> getMotivosMarcacaoPonto() {
		return motivosMarcacaoPonto;
	}

	public void setMotivosMarcacaoPonto(List<SelectItem> motivosMarcacaoPonto) {
		this.motivosMarcacaoPonto = motivosMarcacaoPonto;
	}

	public Long getIdMotivoMarcacao() {
		return idMotivoMarcacao;
	}

	public void setIdMotivoMarcacao(Long idMotivoMarcacao) {
		this.idMotivoMarcacao = idMotivoMarcacao;
	}

	public Date getHoraMarcacao() {
		return horaMarcacao;
	}

	public void setHoraMarcacao(Date horaMarcacao) {
		this.horaMarcacao = horaMarcacao;
	}
	
}