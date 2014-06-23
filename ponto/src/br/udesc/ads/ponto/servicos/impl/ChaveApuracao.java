package br.udesc.ads.ponto.servicos.impl;

import org.joda.time.LocalDate;

public class ChaveApuracao {

	public final LocalDate data;
	public final long codColaborador;

	public ChaveApuracao(LocalDate data, long codColaborador) {
		this.data = data;
		this.codColaborador = codColaborador;
	}

	public LocalDate getData() {
		return data;
	}

	public long getCodColaborador() {
		return codColaborador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (codColaborador ^ (codColaborador >>> 32));
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ChaveApuracao)) {
			return false;
		}
		ChaveApuracao other = (ChaveApuracao) obj;
		if (codColaborador != other.codColaborador) {
			return false;
		}
		if (data == null) {
			return other.data == null;
		}
		return data.equals(other.data);
	}

}
