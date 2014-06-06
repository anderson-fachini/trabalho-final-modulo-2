package br.udesc.ads.ponto.jpaconverters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.udesc.ads.ponto.entidades.Ocorrencia;

@Converter(autoApply = true)
public class OcorrenciaToIntConverter implements AttributeConverter<Ocorrencia, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Ocorrencia ocorrencia) {
		if (ocorrencia == null) {
			return null;
		}
		return ocorrencia.getId();
	}

	@Override
	public Ocorrencia convertToEntityAttribute(Integer id) {
		if (id == null) {
			return null;
		}
		return Ocorrencia.fromId(id);
	}

}
