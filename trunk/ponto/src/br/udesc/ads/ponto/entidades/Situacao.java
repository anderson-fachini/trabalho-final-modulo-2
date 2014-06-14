package br.udesc.ads.ponto.entidades;

import br.udesc.ads.ponto.util.Messages;

public enum Situacao {

	ATIVO("A", Messages.getString("ativo")), //
	INATIVO("I", Messages.getString("inativo"));

	private String id;
	private String descricao;

	private Situacao(String id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public String getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Situacao fromId(String id) {
		if (id == null) {
			return null;
		}
		for (Situacao s : values()) {
			if (s.getId().equals(id)) {
				return s;
			}
		}
		throw new IllegalArgumentException(String.format("'%s' is not a valid id for '%s'.", id,
				Situacao.class.getSimpleName()));
	}
}
