package br.udesc.ads.ponto.jpaconverters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.udesc.ads.ponto.entidades.Situacao;

@Converter(autoApply = true)
public class SituacaoToStringConverter implements AttributeConverter<Situacao, String> {

	@Override
	public String convertToDatabaseColumn(Situacao sit) {
		if (sit == null) {
			return null;
		}
		return sit.getId();
	}

	@Override
	public Situacao convertToEntityAttribute(String id) {
		if (id == null) {
			return null;
		}
		return Situacao.fromId(id);
	}

}
