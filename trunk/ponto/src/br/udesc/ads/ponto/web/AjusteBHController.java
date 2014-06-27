package br.udesc.ads.ponto.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.servicos.ColaboradorService;
import br.udesc.ads.ponto.util.DataConverter;
import br.udesc.ads.ponto.util.JsfUtils;
import br.udesc.ads.ponto.util.Messages;

@ManagedBean(name = "ajusteBH")
@SessionScoped
public class AjusteBHController implements Serializable {

	// TODO Melhorar o campo de Novo Saldo para evitar erros de digitação (e criar uma dica do formato);

	private static final long serialVersionUID = 3078044484313825302L;

	private Map<Long, Colaborador> colaboradoresMap = new HashMap<>();
	private List<SelectItem> colaboradores = new ArrayList<>();
	private Long idColabSelecionado = 0L;
	private String saldoAtual = "";
	private String novoSaldo = "";
	private String motivoAjuste = "";

	public AjusteBHController() {
		popularView();
	}

	private void popularView() {
		Usuario usuarioAutenticado = new MenuController().getUsuarioAutenticado();
		Colaborador colaborador = usuarioAutenticado.getColaborador();
		Setor setor = colaborador != null ? colaborador.getSetor() : null;

		colaboradoresMap.clear();
		colaboradores.clear();

		for (Colaborador c : ColaboradorService.get().getColaboradoresAtivosPorSetor(setor)) {
			if (colaborador != null) {
				if (c.getId().equals(colaborador.getId())) {
					// Para o gerente não poder ajustar o próprio saldo
					continue;
				}
			}
			colaboradoresMap.put(c.getId(), c);
			colaboradores.add(new SelectItem(c.getId(), c.getNome()));
		}
	}

	public void selecionouColaborador(AjaxBehaviorEvent event) {
		atualizarTela();
	}

	private void atualizarTela() {
		if (idColabSelecionado == null || idColabSelecionado.equals(0L)) {
			saldoAtual = "";
			novoSaldo = "";
			motivoAjuste = "";
		} else {
			Colaborador colab = colaboradoresMap.get(idColabSelecionado);
			saldoAtual = DataConverter.doubleParaHoraStr(colab.getSaldoBH());
			novoSaldo = saldoAtual;
			motivoAjuste = "";
		}
	}

	public void salvar() {
		if (idColabSelecionado == null || idColabSelecionado.equals(0L)) {
			JsfUtils.addMensagemErro(Messages.getString("msgSelecionarColaborador"));
		} else {
			try {
				double saldo = DataConverter.strParaHoraDouble(novoSaldo);
				Colaborador colab = colaboradoresMap.get(idColabSelecionado);
				double valorAjuste = saldo - colab.getSaldoBH();
				if (Math.abs(valorAjuste) < 0.01) {
					JsfUtils.addMensagemErro(Messages.getString("msgSaldoBHNaoPodeSerIgual"));
				} else {
					Usuario responsavel = new MenuController().getUsuarioAutenticado();
					ColaboradorService.get().ajustarBancoHoras(colab, valorAjuste, responsavel, motivoAjuste);
					atualizarTela();
					JsfUtils.addMensagemInfo(Messages.getString("msgAjusteBHSalvoSucesso"));
				}
			} catch (Throwable ex) {
				JsfUtils.addMensagemErro("Erro ao salvar:\n" + ex.getMessage());
			}
		}
	}

	public List<SelectItem> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<SelectItem> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public Long getIdColabSelecionado() {
		return idColabSelecionado;
	}

	public void setIdColabSelecionado(Long idColabSelecionado) {
		this.idColabSelecionado = idColabSelecionado;
	}

	public String getSaldoAtual() {
		return saldoAtual;
	}

	public void setSaldoAtual(String saldoAtual) {
		this.saldoAtual = saldoAtual;
	}

	public String getNovoSaldo() {
		return novoSaldo;
	}

	public void setNovoSaldo(String novoSaldo) {
		this.novoSaldo = novoSaldo;
	}

	public String getMotivoAjuste() {
		return motivoAjuste;
	}

	public void setMotivoAjuste(String motivoAjuste) {
		this.motivoAjuste = motivoAjuste;
	}

}
