package br.udesc.ads.ponto.entidades;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

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
		throw new IllegalArgumentException(String.format("'%d' is not a valid id for '%s'.", id, DiaSemana.class.getSimpleName()));
	}

	public static DiaSemana[] getDiasUteisPadrao() {
		return new DiaSemana[] { //
		SEGUNDA_FEIRA, //
				TERCA_FEIRA, //
				QUARTA_FEIRA, //
				QUINTA_FEIRA, //
				SEXTA_FEIRA };
	}

	public static DiaSemana fromLocalDate(LocalDate date) {
		switch (date.getDayOfWeek()) {
		case DateTimeConstants.SUNDAY:
			return DOMINGO;
		case DateTimeConstants.MONDAY:
			return SEGUNDA_FEIRA;
		case DateTimeConstants.TUESDAY:
			return TERCA_FEIRA;
		case DateTimeConstants.WEDNESDAY:
			return QUARTA_FEIRA;
		case DateTimeConstants.THURSDAY:
			return QUINTA_FEIRA;
		case DateTimeConstants.FRIDAY:
			return SEXTA_FEIRA;
		case DateTimeConstants.SATURDAY:
			return SABADO;
		default:
			return null;
		}
	}

}
