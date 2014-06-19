package br.udesc.ads.ponto.servicos.impl;

import org.joda.time.LocalTime;

public class Intervalo {

	private final LocalTime inicio;
	private final LocalTime fim;
	private final TipoIntervalo tipo;

	public Intervalo(LocalTime inicio, LocalTime fim, TipoIntervalo tipo) {
		this.inicio = inicio;
		this.fim = fim;
		this.tipo = tipo;
	}

	public int getMillis() {
		return getFim().getMillisOfDay() - getInicio().getMillisOfDay();
	}

	public int getMinutos() {
		return getMillis() / 1000 / 60;
	}

	public boolean isTrabalhado() {
		return getTipo() == TipoIntervalo.TRABALHADO;
	}

	public boolean isNaoTrabalhado() {
		return getTipo() == TipoIntervalo.NAO_TRABALHADO;
	}

	public boolean isContidoEm(Intervalo referencia) {
		if (this.getInicio().isBefore(referencia.getInicio())) {
			return false;
		}
		if (this.getFim().isAfter(referencia.getFim())) {
			return false;
		}
		return true;
	}

	public boolean isSobrepostoEm(Intervalo referencia) {
		if (this.getFim().isBefore(referencia.getInicio())) {
			return false;
		}
		if (this.getInicio().isAfter(referencia.getFim())) {
			return false;
		}
		return true;
	}

	public LocalTime getFim() {
		return fim;
	}

	public LocalTime getInicio() {
		return inicio;
	}

	public TipoIntervalo getTipo() {
		return tipo;
	}
	
	@Override
	public String toString() {
		return tipo.toString() + ": " + inicio.toString() + " - " + fim.toString();
	}
}
