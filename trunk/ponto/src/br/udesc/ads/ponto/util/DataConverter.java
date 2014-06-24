package br.udesc.ads.ponto.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe com funções utilitárias para conversão de dados 
 * 
 * @author anderson
 */
public class DataConverter {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * Formatação de data no formato dd/mm. Ex.: 25/12
	 */
	public static final String formatoDDMM = "dd/MM";
	
	/**
	 * Formatação de hora em formato hh:mm (0-24). Ex.: 18:30
	 */
	public static final String formatoHHMM = "kk:mm";
	
	/**
	 * Dia da semana por extenso. Ex.: Segunda, Sábado, Domingo
	 */
	public static final String formatoDiaSemanaExtenso = "EEEE";
	
	/**
	 * Converte um valor double para a sua representação em horas em formato String.
	 * Mesmo que a quantidade de horas seja maior que 24 retorna o valor em horas (e não em dias).
	 * @param number Número que representa uma quantidade de horas
	 * @return Representação em horas do número
	 */
	public static String doubleParaHoraStr(double number) {
		int horas = (int)number;
		int minutos = (int)Math.round((number - horas) * 60);
		
		if (minutos < 0)
			minutos *= -1;
		
		return String.format("%02d:%02d", horas, minutos);
	}
	
	/**
	 * Formata um objeto Date em String no formato dd/mm/aaaa
	 * @param data
	 * @return
	 */
	public static String formataData(Date data) {
		return sdf.format(data);
	}
	
	/**
	 * Formata um objeto Date em String de acordo com o formato especificado
	 * @param data
	 * @param formato
	 * @return
	 */
	public static String formataData(Date data, String formato) {
		return new SimpleDateFormat(formato).format(data);
	}
}
