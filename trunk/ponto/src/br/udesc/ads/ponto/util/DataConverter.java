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
	 * Converte um valor double para a sua representação em horas em formato String. Mesmo que a quantidade de horas
	 * seja maior que 24 retorna o valor em horas (e não em dias).
	 * 
	 * @param number
	 *            Número que representa uma quantidade de horas
	 * @return Representação em horas do número
	 */
	public static String doubleParaHoraStr(double number) {
		int horas = (int) number;
		int minutos = (int) Math.round((number - horas) * 60);

		if (minutos < 0) {
			minutos *= -1;
		}
		if (horas < 0) {
			// Quando ele estava formatando com o '-', ele estava deixando sempre só com 2 dígitos.
			return String.format("%03d:%02d", horas, minutos);
		} else {
			return String.format("%02d:%02d", horas, minutos);
		}
	}

	/**
	 * Converte um valor string para sua representação em horas proporcionais em double.<br>
	 * O formato esperado é H:MM, HH:MM ou HHH:MM (assim por diante).<br>
	 * São também aceitos valores negativos (neste caso, com um '-' no início).<br>
	 * O limite de minutos é 59. O limite é horas é o limite do int.
	 * @param str O valor no formato especificado.
	 * @return A quantidade de horas (parte inteira) com os minutos divididos proporcionalmente (parte fracionada do número).
	 */
	public static double strParaHoraDouble(String str) {
		String[] tokens = str.split(":");
		if (tokens.length != 2 || tokens[1].length() != 2) {
			throw new IllegalArgumentException("Formato inválido. Esperado formato H:MM, HH:MM ou HHH:MM...");
		}
		try {
			int horas = Integer.parseInt(tokens[0]);
			int minutos = Integer.parseInt(tokens[1]);
			if (minutos >= 60) {
				throw new IllegalArgumentException("Formato inválido. Esperado formato H:MM, HH:MM ou HHH:MM...");
			}
			double minParcial = (double) minutos / 60.0;
			if (horas < 0) {
				return horas - minParcial;
			} else {
				return horas + minParcial;
			}
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Formato inválido. Esperado formato H:MM, HH:MM ou HHH:MM...");
		}
	}

	/**
	 * Formata um objeto Date em String no formato dd/mm/aaaa
	 * 
	 * @param data
	 * @return
	 */
	public static String formataData(Date data) {
		return sdf.format(data);
	}

	/**
	 * Formata um objeto Date em String de acordo com o formato especificado
	 * 
	 * @param data
	 * @param formato
	 * @return
	 */
	public static String formataData(Date data, String formato) {
		return new SimpleDateFormat(formato).format(data);
	}

}
