package br.udesc.ads.ponto.jpaconverters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.udesc.ads.ponto.entidades.DiaSemana;

@Converter(autoApply = true)
public class DiaSemanaToIntConverter implements AttributeConverter<DiaSemana, Integer> {

	@Override
	public Integer convertToDatabaseColumn(DiaSemana diaSemana) {
		if (diaSemana == null) {
			return null;
		}
		return diaSemana.ordinal();
	}

	@Override
	public DiaSemana convertToEntityAttribute(Integer ordinal) {
		if (ordinal == null) {
			return null;
		}
		DiaSemana[] values = DiaSemana.values();
		if (ordinal >= values.length) {
			throw new RuntimeException(String.format("'%d' is not a valid value for %s.", ordinal,
					DiaSemana.class.getSimpleName()));
		}
		return values[ordinal];
	}

}
