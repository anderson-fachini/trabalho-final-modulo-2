package br.udesc.ads.ponto.jpaconverters;

import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

@Converter(autoApply = true)
public class LocalTimeToDateConverter implements AttributeConverter<LocalTime, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalTime localTime) {
		if (localTime == null) {
			return null;
		}
		return localTime.toDateTimeToday().toDate();
	}

	@Override
	public LocalTime convertToEntityAttribute(Date date) {
		if (date == null) {
			return null;
		}
		return new LocalDateTime(date).toLocalTime();
	}

}
