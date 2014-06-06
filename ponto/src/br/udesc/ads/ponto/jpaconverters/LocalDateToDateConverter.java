package br.udesc.ads.ponto.jpaconverters;

import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateToDateConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return localDate.toDate();
	}

	@Override
	public LocalDate convertToEntityAttribute(Date date) {
		if (date == null) {
			return null;
		}
		return new LocalDate(date);
	}

}
