package br.udesc.ads.ponto.entidades;

public enum DiaSemana {

	DOMINGO(1, "Domingo"), //
	SEGUNDA_FEIRA(2, "Segunda-feira"), //
	TERCA_FEIRA(3, "Terça-feira"), //
	QUARTA_FEIRA(4, "Quarta-feira"), //
	QUINTA_FEIRA(5, "Quinta-feira"), //
	SEXTA_FEIRA(6, "Sexta-feira"), //
	SABADO(7, "Sábado");

	private int id;
	private String descricao;

	private DiaSemana(int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return id + " - " + descricao;
	}

	public static DiaSemana fromId(int id) {
		for (DiaSemana ds : values()) {
			if (ds.getId() == id) {
				return ds;
			}
		}
		throw new IllegalArgumentException(String.format("'%d' is not a valid id for '%s'.", id,
				DiaSemana.class.getSimpleName()));
	}

}
