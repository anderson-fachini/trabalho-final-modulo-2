package br.udesc.ads.ponto.util;

import org.joda.time.LocalTime;

public class TimeUtils {
	
	public static double toDoubleHoras(LocalTime localTime) {
		double result = localTime.getMillisOfDay();
		result /= 1000 * 60 * 60;
		return result;
	}

}
