package br.udesc.ads.ponto.entidades;

public enum Ocorrencia {

	HORAS_EXCEDENTES(1, "Horas excedentes"), //
	HORAS_FALTANTES(2, "Horas faltantes"), //
	MARCACOES_FORA_DA_ESCALA(3, "Marcações fora da escala"), //
	MARCACOES_FALTANTES(4, "Marcações faltantes"), //
	MARCACOES_EXCEDENTES(5, "Marcações excedentes"), //
	INTERVALO_ALMOCO_INCOMPLETO(6, "Intervalo de almoço incompleto"), //
	INTERVALO_INTERJORNADAS_INCOMPLETO(7, "Intervalo interjornadas incompleto"), //
	INTERVALO_INTRAJORNADA_INCOMPLETO(8, "Intervalo intrajornada incompleto"), //
	INTERVALO_TRABALHO_EXCEDIDO(9, "Intervalo de trabalho excedido");

	private int id;
	private String descricao;

	private Ocorrencia(int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Ocorrencia fromId(int id) {
		for (Ocorrencia oco : values()) {
			if (oco.getId() == id) {
				return oco;
			}
		}
		throw new IllegalArgumentException(String.format("'%d' is not a valid id for '%s'.", id,
				Ocorrencia.class.getSimpleName()));
	}
}
