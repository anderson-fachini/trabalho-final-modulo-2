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

import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.relatorios.RelatorioSaldoBH;
import br.udesc.ads.ponto.relatorios.SaldoBHResult;
import br.udesc.ads.ponto.servicos.ColaboradorService;
import br.udesc.ads.ponto.util.DataConverter;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "relSaldoBh")
@SessionScoped
public class RelatorioSaldoBhController implements Serializable {

	private static final long serialVersionUID = -8102191811460863725L;

	private List<SaldoBHResult> saldosBh;
	private Map<Long, Colaborador> colaboradoresMap;
	private List<SelectItem> colaboradores;

	private Date maxDate = LocalDate.now().minusDays(1).toDate();
	private Date dataInicial;
	private Date dataFinal;

	private Long colaboradorSelecionado;

	private boolean listouRelatorio = false;

	public RelatorioSaldoBhController() {
		colaboradorSelecionado = 0L;

		buscaColaboradores();
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
		List<Colaborador> colabsRelatorio = new ArrayList<Colaborador>();

		if (colaboradorSelecionado == 0) {
			colabsRelatorio.addAll(colaboradoresMap.values());
		} else {
			colabsRelatorio.add(colaboradoresMap.get(colaboradorSelecionado));
		}
		LocalDate dtInicial = LocalDate.fromDateFields(dataInicial);
		LocalDate dtFinal = LocalDate.fromDateFields(dataFinal);
		saldosBh = RelatorioSaldoBH.get().consultar(dtInicial, dtFinal, colabsRelatorio);
	}

	private void buscaColaboradores() {
		Usuario usuarioAutenticado = new MenuController().getUsuarioAutenticado();
		Colaborador colaborador = usuarioAutenticado.getColaborador();
		Setor setor = null;

		if (colaborador != null) {
			setor = colaborador.getSetor();
		}

		colaboradoresMap = new HashMap<Long, Colaborador>();

		colaboradores = new ArrayList<SelectItem>();
		if (colaborador == null) {
			colaboradores.add(new SelectItem(0L, "Todos"));
		} else {
			colaboradores.add(new SelectItem(0L, "Todos de '" + setor.getNome() + "'"));
		}

		for (Colaborador c : ColaboradorService.get().getColaboradoresAtivosPorSetor(setor)) {
			colaboradoresMap.put(c.getId(), c);
			colaboradores.add(new SelectItem(c.getId(), c.getNome()));
		}
	}

	public List<SaldoBHResult> getSaldosBh() {
		return saldosBh;
	}

	public void setSaldosBh(List<SaldoBHResult> saldosBh) {
		this.saldosBh = saldosBh;
	}

	public List<SelectItem> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<SelectItem> colaboradores) {
		this.colaboradores = colaboradores;
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
		return saldosBh != null && !saldosBh.isEmpty();
	}

	public String getHoraFormatada(double valor) {
		return DataConverter.doubleParaHoraStr(valor);
	}

	public String getDataInicialFormatada() {
		return DataConverter.formataData(dataInicial);
	}

	public String getDataFinalFormatada() {
		return DataConverter.formataData(dataFinal);
	}

}
