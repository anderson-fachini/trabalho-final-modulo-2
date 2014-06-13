package br.udesc.ads.ponto.leitora;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class LeitoraPonto {

	private File arquivoMarcacoes;
	private File arquivoConfirmacoes;

	private boolean leuConfirmacoes = false;
	private List<Long> confirmadas = new ArrayList<>();

	public LeitoraPonto(File marcacoes, File confirmacoes) {
		this.arquivoMarcacoes = marcacoes;
		this.arquivoConfirmacoes = confirmacoes;
	}

	private void lerConfirmacoes() throws IOException, FormatoInvalidoException {

		FileReader reader = new FileReader(arquivoConfirmacoes);
		BufferedReader bufReader = new BufferedReader(reader);
		try {
			String linha;
			while ((linha = bufReader.readLine()) != null) {
				long id;
				try {
					id = Long.parseLong(linha);
				} catch (NumberFormatException ex) {
					throw new FormatoInvalidoException(ex);
				}
				confirmadas.add(id);
			}

		} finally {
			bufReader.close();
			reader.close();
		}
	}

	public List<RegistroMarcacao> lerMarcacoes(int quantidade) throws IOException, FormatoInvalidoException {
		if (!leuConfirmacoes) {
			lerConfirmacoes();
			leuConfirmacoes = true;
		}

		LocalDate hoje = LocalDate.now();
		List<RegistroMarcacao> result = new ArrayList<>();

		FileReader reader = new FileReader(arquivoMarcacoes);
		BufferedReader bufReader = new BufferedReader(reader);
		try {
			boolean primeiraLinha = true;
			String linha;
			while ((linha = bufReader.readLine()) != null) {
				String[] tokens = linha.split(";");
				if (tokens.length != 4) {
					throw new FormatoInvalidoException(String.format(
							"Formato inválido no arquivo '%s'. Esperando 4 tokens, veio %d.", arquivoMarcacoes,
							tokens.length));
				}
				if (primeiraLinha && !isLongInteger(tokens[0])) {
					// Pula o header
					primeiraLinha = false;
					continue;
				}
				primeiraLinha = false;
				try {
					if (quantidade == 0) {
						break;
					}
					long id = Long.parseLong(tokens[0]);
					if (confirmadas.contains(id)) {
						continue; // Para não enviar as confirmadas novamente.
					}
					LocalDateTime dataHora = parseLocalDateTime(tokens[2], tokens[3]);
					if (!dataHora.toLocalDate().isBefore(hoje)) {
						continue; // Para não enviar a marcações do dia atual.
					}
					RegistroMarcacao registro = new RegistroMarcacao();
					registro.setId(id);
					registro.setCodFuncionario(Long.parseLong(tokens[1]));
					registro.setMarcacao(dataHora.toDate());
					result.add(registro);
					quantidade--;
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
					throw new FormatoInvalidoException(ex);
				}
			}
		} finally {
			bufReader.close();
			reader.close();
		}
		return result;
	}

	public void confirmarMarcacoes(long idInicial, long idFinal) throws IOException {
		
		boolean breakLine = arquivoConfirmacoes.length() > 0;

		FileWriter writer = new FileWriter(arquivoConfirmacoes, true);
		BufferedWriter bufWriter = new BufferedWriter(writer);
		try {
			for (long l = idInicial; l <= idFinal; ++l) {
				confirmadas.add(l);
				if (breakLine) {
					writer.append("\r\n");
				} else {
					breakLine = true;
				}
				writer.append(String.valueOf(l));
			}
		} finally {
			bufWriter.close();
			writer.close();
		}
	}

	private static LocalDateTime parseLocalDateTime(String data, String hora) {
		String[] d = data.split("/");
		String[] h = hora.split(":");
		return new LocalDateTime(//
				Integer.parseInt(d[2]), Integer.parseInt(d[1]), Integer.parseInt(d[0]),//
				Integer.parseInt(h[0]), Integer.parseInt(h[1]), Integer.parseInt(h[2]));
	}

	private static boolean isLongInteger(String string) {
		try {
			Long.parseLong(string);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public void resetarConfirmacoes() throws IOException {
		if (arquivoConfirmacoes.exists()) {
			arquivoConfirmacoes.delete();
		}
		arquivoConfirmacoes.createNewFile();
		confirmadas.clear();
	}
	
}
