package br.udesc.ads.ponto.jpaconverters;

import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.LocalDateTime;

@Converter(autoApply = true)
public class LocalDateTimeToDateConverter implements AttributeConverter<LocalDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.toDate();
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Date date) {
		if (date == null) {
			return null;
		}
		return new LocalDateTime(date);
	}

}
