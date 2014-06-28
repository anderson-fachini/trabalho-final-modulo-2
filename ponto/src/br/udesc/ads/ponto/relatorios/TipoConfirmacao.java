package br.udesc.ads.ponto.relatorios;

import br.udesc.ads.ponto.util.Messages;

public enum TipoConfirmacao {

	TODAS(Messages.getString("todas")), //
	CONFIRMADAS(Messages.getString("confirmadas")), //
	PENDENTES(Messages.getString("pendentes"));

	private String descricao;

	private TipoConfirmacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
}
