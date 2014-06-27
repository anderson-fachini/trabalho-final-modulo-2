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
import br.udesc.ads.ponto.relatorios.RelatorioSaldoBHPorSetor;
import br.udesc.ads.ponto.relatorios.SaldoBHSetorResult;
import br.udesc.ads.ponto.servicos.SetorService;
import br.udesc.ads.ponto.util.DataConverter;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "relSaldoBhSetor")
@SessionScoped
public class RelatorioSaldoBhSetorController implements Serializable {

	private static final long serialVersionUID = 4688803327731861791L;

	private List<SaldoBHSetorResult> saldosBh;
	private Map<Long, Setor> setoresMap = new HashMap<>();
	private List<SelectItem> setores = new ArrayList<SelectItem>();
	private Date maxDate = LocalDate.now().toDate();
	private Date dataInicial;
	private Date dataFinal;
	private Long setorSelecionado;
	private boolean listouRelatorio = false;

	public RelatorioSaldoBhSetorController() {
		setorSelecionado = 0L;
		buscaSetores();
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
		List<Setor> setoresRelatorio = new ArrayList<>();

		if (setorSelecionado == 0) {
			setoresRelatorio.addAll(setoresMap.values());
		} else {
			setoresRelatorio.add(setoresMap.get(setorSelecionado));
		}

		LocalDate dtInicial = LocalDate.fromDateFields(dataInicial);
		LocalDate dtFinal = LocalDate.fromDateFields(dataFinal);
		saldosBh = RelatorioSaldoBHPorSetor.get().consultar(dtInicial, dtFinal, setoresRelatorio);
	}

	private void buscaSetores() {
		Usuario usuarioAutenticado = new MenuController().getUsuarioAutenticado();
		Colaborador colaborador = usuarioAutenticado.getColaborador();
		Setor setor = colaborador != null ? colaborador.getSetor() : null;

		setoresMap.clear();
		setores.clear();
		if (setor == null) {
			setores.add(new SelectItem(0L, "Todos"));
			for (Setor s : SetorService.get().getSetores()) {
				setoresMap.put(s.getId(), s);
				setores.add(new SelectItem(s.getId(), s.getNome()));
			}
		} else {
			setoresMap.put(setor.getId(), setor);
			setores.add(new SelectItem(setor.getId(), setor.getNome()));
		}

	}

	public List<SaldoBHSetorResult> getSaldosBh() {
		return saldosBh;
	}

	public void setSaldosBh(List<SaldoBHSetorResult> saldosBh) {
		this.saldosBh = saldosBh;
	}

	public List<SelectItem> getSetores() {
		return setores;
	}

	public void setSetores(List<SelectItem> setores) {
		this.setores = setores;
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

	public Long getSetorSelecionado() {
		return setorSelecionado;
	}

	public void setSetorSelecionado(Long setorSelecionado) {
		this.setorSelecionado = setorSelecionado;
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
