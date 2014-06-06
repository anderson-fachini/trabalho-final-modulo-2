package br.udesc.ads.ponto.entidades;

public enum PerfilUsuario {

	DONO_EMPRESA("D", "Dono da empresa"), //
	GERENTE("G", "Gerente"), //
	FUNCIONARIO_RH("R", "Funcionário do RH");

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
