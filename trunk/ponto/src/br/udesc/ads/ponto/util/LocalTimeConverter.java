package br.udesc.ads.ponto.util;

import java.text.SimpleDateFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalTime;

@FacesConverter("br.udesc.ponto.LocalTimeConverter")
public class LocalTimeConverter implements Converter {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		String[] split = value.split(":");
		int horas = Integer.parseInt(split[0]);
		int minutos = Integer.parseInt(split[1]);
		
		try {
			LocalTime l = new LocalTime(horas, minutos, 0);
			return l;
		} catch (IllegalFieldValueException e) {
			JsfUtils.addMensagemErro(component.getClientId(), Messages.getString("msgInformeHoraValida"));
			e.printStackTrace();
		}
		
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value instanceof LocalTime) {
			return sdf.format( ((LocalTime)value).toDateTimeToday().toDate() );
		} else {
			return (String) value;
		}
	}

}
