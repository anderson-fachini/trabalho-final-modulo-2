package br.udesc.ads.ponto.util;

public class StringUtils {
	
	public static boolean isValidUserName(String str) {
		return toUserName(str).equals(str);
	}
	
	private static final String SPECIAL_CHARS = "áéíóúâêîôûäëïöüç";
	private static final String COMMON_CHARS = "aeiouaeiouaeoiuc";
	private static final String REGULAR_CHAR_SEQUENCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";

	public static String toUserName(String name) {
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); ++i) {
			char c = name.charAt(i);
			if (c == ' ') {
				break;
			}
			if (REGULAR_CHAR_SEQUENCE.indexOf(c) != -1) {
				sb.append(c);
			} else {
				int idxOf = SPECIAL_CHARS.indexOf(c);
				if (idxOf >= 0) {
					sb.append(COMMON_CHARS.charAt(idxOf));
				}
			}
		}
		return sb.toString().toLowerCase();
	}
	
	public static void main(String[] args) {
		System.out.println(toUserName("André"));
	}

}
