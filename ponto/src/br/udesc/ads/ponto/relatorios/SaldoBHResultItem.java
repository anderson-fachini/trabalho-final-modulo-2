package br.udesc.ads.ponto.relatorios;

import br.udesc.ads.ponto.entidades.Colaborador;

public class SaldoBHResultItem {

	private Colaborador colaborador;
	private double entradasBH;
	private double saidasBH;
	private double saldoInicialPeriodo;

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
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

	public double getSaldoInicialPeriodo() {
		return saldoInicialPeriodo;
	}

	public void setSaldoInicialPeriodo(double saldoInicialPeriodo) {
		this.saldoInicialPeriodo = saldoInicialPeriodo;
	}

	public double getSaldoFinalPeriodo() {
		return saldoInicialPeriodo + entradasBH - saidasBH;
	}

}
