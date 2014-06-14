package br.udesc.ads.ponto.entidades;

import br.udesc.ads.ponto.util.Messages;

public enum PerfilUsuario {

	DONO_EMPRESA("D", Messages.getString("donoEmpresa")), //
	GERENTE("G", Messages.getString("gerente")), //
	FUNCIONARIO_RH("R", Messages.getString("funcionarioRh"));

	private String id;
	private String descricao;

	private PerfilUsuario(String id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public String getId() {
		return id;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public static PerfilUsuario fromId(String id) {
		if (id == null) {
			return null;
		}
		for (PerfilUsuario pu : values()) {
			if (pu.getId().equals(id)) {
				return pu;
			}
		}
		throw new IllegalArgumentException(String.format("'%s' is not a valid id for '%s'.", id,
				PerfilUsuario.class.getSimpleName()));
	}

}
