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
	 * Converte um valor double para a sua representação em horas em formato String.
	 * Mesmo que a quantidade de horas seja maior que 24 retorna o valor em horas (e não em dias).
	 * @param number Número que representa uma quantidade de horas
	 * @return Representação em horas do número
	 */
	public static String doubleParaHoraStr(double number) {
		int horas = (int)number;
		int minutos = (int)Math.round((number - horas) * 60);
		
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
}
