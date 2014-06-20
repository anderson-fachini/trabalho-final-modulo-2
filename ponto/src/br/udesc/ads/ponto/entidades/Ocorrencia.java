package br.udesc.ads.ponto.entidades;

import br.udesc.ads.ponto.util.Messages;

public enum Ocorrencia {

	HORAS_EXCEDENTES(1, Messages.getString("horasExcedentes")), //
	HORAS_FALTANTES(2, Messages.getString("horasFaltantes")), //
	MARCACOES_FORA_DA_ESCALA(3, Messages.getString("marcacoesForaEscala")), //
	MARCACOES_FALTANTES(4, Messages.getString("marcacoesFaltantes")), //
	MARCACOES_EXCEDENTES(5, Messages.getString("marcacoesExcedentes")), //
	INTERVALO_ALMOCO_INCOMPLETO(6, Messages.getString("intervaloAlmocoIncompleto")), //
	INTERVALO_INTERJORNADAS_INCOMPLETO(7, Messages.getString("intervaloInterjornadasIncompleto")), //
	INTERVALO_INTRAJORNADA_INCOMPLETO(8, Messages.getString("intervaloIntrajornadasIncompleto")), //
	INTERVALO_TRABALHO_EXCEDIDO(9, Messages.getString("intervaloTrabalhoExcedido"));

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
