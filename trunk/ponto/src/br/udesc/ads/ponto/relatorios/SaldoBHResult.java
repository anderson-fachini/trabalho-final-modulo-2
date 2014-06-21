package br.udesc.ads.ponto.relatorios;

import java.io.Serializable;

import br.udesc.ads.ponto.entidades.Colaborador;

public class SaldoBHResult implements Serializable {

	private static final long serialVersionUID = 6325962788420839259L;
	
	private Colaborador colaborador;
	private double entradasBH;
	private double saidasBH;
	private double ajustesManuaisBH;
	private double saldoInicialPeriodo;
	private double saldoFinalPeriodo;

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
