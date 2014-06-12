package br.udesc.ads.ponto.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CriptografaSenha {
	
	private static MessageDigest md;
	
	static {
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public static String criptografa(String text) {
		StringBuffer hexString = new StringBuffer();
		byte[] byteData = md.digest(text.getBytes());
		
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1) hexString.append(0);
			hexString.append(hex);
		}
		
		return hexString.toString();
	}
	
}
