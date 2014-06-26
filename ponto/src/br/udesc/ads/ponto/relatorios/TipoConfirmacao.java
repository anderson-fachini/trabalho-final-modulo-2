package br.udesc.ads.ponto.relatorios;

import br.udesc.ads.ponto.util.Messages;

public enum TipoConfirmacao {

	CONFIRMADAS(Messages.getString("confirmadas")), //
	PENDENTES(Messages.getString("pendentes")), //
	TODAS(Messages.getString("todas"));
	
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
