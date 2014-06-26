package br.udesc.ads.ponto.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.relatorios.RelatorioResumoApuracaoPonto;
import br.udesc.ads.ponto.relatorios.ResumoApuracaoPontoResult;
import br.udesc.ads.ponto.relatorios.TipoConfirmacao;
import br.udesc.ads.ponto.servicos.ColaboradorService;
import br.udesc.ads.ponto.util.DataConverter;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "relResApuracoesPonto")
@SessionScoped
public class RelatorioResumoApuracoesPontoController implements Serializable {

	private static final long serialVersionUID = 4588255784004116557L;

	private List<ResumoApuracaoPontoResult> apuracoes;
	private Map<Long, Colaborador> colaboradoresMap = new HashMap<>();
	private List<SelectItem> colaboradores = new ArrayList<SelectItem>();
	private Date maxDate = LocalDate.now().minusDays(1).toDate();
	private Date dataInicial;
	private Date dataFinal;
	private List<SelectItem> tiposConfirmacao = new ArrayList<>();
	private TipoConfirmacao tipoConfirmacaoSelecionado = null;
	private Long colaboradorSelecionado = null;
	private boolean listouRelatorio = false;

	public RelatorioResumoApuracoesPontoController() {
		carregarFiltros();
	}

	public void gerarRelatorio() {
		if (dataInicial.after(dataFinal)) {
			JsfUtils.addMensagemWarning(Messages.getString("msgDataInicialMaiorIgual"));
		} else {
			buscaDadosRelatorio();
			listouRelatorio = true;
		}
	}

	private void buscaDadosRelatorio() {
		Colaborador colab;
		if (colaboradorSelecionado == null) {
			colab = null;
		} else {
			colab = colaboradoresMap.get(colaboradorSelecionado);
		}
		LocalDate dtInicial = LocalDate.fromDateFields(dataInicial);
		LocalDate dtFinal = LocalDate.fromDateFields(dataFinal);

		apuracoes = RelatorioResumoApuracaoPonto.get().consultar(dtInicial, dtFinal, colab, tipoConfirmacaoSelecionado);
	}

	private void carregarFiltros() {
		Usuario usuarioAutenticado = new MenuController().getUsuarioAutenticado();
		Colaborador colaborador = usuarioAutenticado.getColaborador();
		Setor setor = colaborador != null ? colaborador.getSetor() : null;

		colaboradoresMap.clear();
		colaboradores.clear();

		for (Colaborador c : ColaboradorService.get().getColaboradoresAtivosPorSetor(setor)) {
			colaboradoresMap.put(c.getId(), c);
			colaboradores.add(new SelectItem(c.getId(), c.getNome()));
		}

		for (TipoConfirmacao tipo : TipoConfirmacao.values()) {
			tiposConfirmacao.add(new SelectItem(tipo, tipo.getDescricao()));
		}
	}

	public List<ResumoApuracaoPontoResult> getApuracoes() {
		return apuracoes;
	}

	public void setApuracoes(List<ResumoApuracaoPontoResult> apuracoes) {
		this.apuracoes = apuracoes;
	}

	public List<SelectItem> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<SelectItem> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public List<SelectItem> getTiposConfirmacao() {
		return tiposConfirmacao;
	}

	public void setTiposConfirmacao(List<SelectItem> tiposConfirmacao) {
		this.tiposConfirmacao = tiposConfirmacao;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Long getColaboradorSelecionado() {
		return colaboradorSelecionado;
	}

	public void setColaboradorSelecionado(Long colaboradorSelecionado) {
		this.colaboradorSelecionado = colaboradorSelecionado;
	}

	public TipoConfirmacao getTipoConfirmacaoSelecionado() {
		return tipoConfirmacaoSelecionado;
	}

	public void setTipoConfirmacaoSelecionado(TipoConfirmacao tipoConfirmacaoSelecionado) {
		this.tipoConfirmacaoSelecionado = tipoConfirmacaoSelecionado;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public boolean isListouRelatorio() {
		return listouRelatorio;
	}

	public void setListouRelatorio(boolean listouRelatorio) {
		this.listouRelatorio = listouRelatorio;
	}

	public boolean isExistemDadosListar() {
		return apuracoes != null && !apuracoes.isEmpty();
	}

	public String getHoraFormatada(double valor) {
		return DataConverter.doubleParaHoraStr(valor);
	}

	public String dateToStr(LocalDate localDate) {
		return localDate.toString("dd/MM/yyyy");
	}

	public String marcacoesToStr(List<LocalTime> marcacoes) {
		StringBuilder sb = new StringBuilder();
		for (LocalTime time : marcacoes) {
			if (sb.length() > 0) {
				sb.append(" - ");
			}
			sb.append(time.toString("HH:mm"));
		}
		return sb.toString();
	}

	public String getDataInicialFormatada() {
		return DataConverter.formataData(dataInicial);
	}

	public String getDataFinalFormatada() {
		return DataConverter.formataData(dataFinal);
	}

	public String getNomeColaboradorSelecionado() {
		return colaboradoresMap.get(colaboradorSelecionado).getNome();
	}

}
