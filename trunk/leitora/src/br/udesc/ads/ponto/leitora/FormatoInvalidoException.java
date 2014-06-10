package br.udesc.ads.ponto.leitora;

public class FormatoInvalidoException extends Exception {

	private static final long serialVersionUID = 1L;

	public FormatoInvalidoException(String message) {
		super(message);
	}

	public FormatoInvalidoException(Throwable cause) {
		super(cause);
	}

}
