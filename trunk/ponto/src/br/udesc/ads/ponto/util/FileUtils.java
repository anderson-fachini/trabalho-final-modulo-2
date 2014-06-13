package br.udesc.ads.ponto.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static List<String> fileToString(File file) throws IOException {
		List<String> result = new ArrayList<>();
		FileReader reader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(reader);
		try {
			String line;
			while ((line = bufReader.readLine()) != null) {
				result.add(line);
			}

		} finally {
			bufReader.close();
			reader.close();
		}
		return result;
	}
	
}



