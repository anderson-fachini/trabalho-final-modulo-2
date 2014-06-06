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
		return diaSemana.getId();
	}

	@Override
	public DiaSemana convertToEntityAttribute(Integer id) {
		if (id == null) {
			return null;
		}
		return DiaSemana.fromId(id);
	}

}
