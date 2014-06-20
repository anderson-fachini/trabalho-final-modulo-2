package br.udesc.ads.ponto.relatorios;

import br.udesc.ads.ponto.entidades.Setor;

public class SaldoBHSetorResult {

	private Setor setor;
	private double entradasBH;
	private double saidasBH;
	private double ajustesManuaisBH;
	private double saldoInicialPeriodo;
	private double saldoFinalPeriodo;

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public double getEntradasBH() {
		return entradasBH;
	}

	public void setEntradasBH(double entradasBH) {
		this.entradasBH = entradasBH;
	}

	public double getSaidasBH() {
		return saidasBH;
	}

	public void setSaidasBH(double saidasBH) {
		this.saidasBH = saidasBH;
	}

	public double getAjustesManuaisBH() {
		return ajustesManuaisBH;
	}

	public void setAjustesManuaisBH(double ajustesManuaisBH) {
		this.ajustesManuaisBH = ajustesManuaisBH;
	}

	public double getSaldoInicialPeriodo() {
		return saldoInicialPeriodo;
	}

	public void setSaldoInicialPeriodo(double saldoInicialPeriodo) {
		this.saldoInicialPeriodo = saldoInicialPeriodo;
	}

	public double getSaldoFinalPeriodo() {
		return saldoFinalPeriodo;
	}

	public void setSaldoFinalPeriodo(double saldoFinalPeriodo) {
		this.saldoFinalPeriodo = saldoFinalPeriodo;
	}

}
