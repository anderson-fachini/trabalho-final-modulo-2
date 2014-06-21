package br.udesc.ads.ponto.relatorios;

import br.udesc.ads.ponto.entidades.Colaborador;

public class ResumoHorasTrabalhadasResult {

	private Colaborador colaborador;
	private double horasTrabalhadas;
	private double horasExcedentes;
	private double horasFaltantes;
	private double horasAbonadas;

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public double getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public void setHorasTrabalhadas(double horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}

	public double getHorasExcedentes() {
		return horasExcedentes;
	}

	public void setHorasExcedentes(double horasExcedentes) {
		this.horasExcedentes = horasExcedentes;
	}

	public double getHorasFaltantes() {
		return horasFaltantes;
	}

	public void setHorasFaltantes(double horasFaltantes) {
		this.horasFaltantes = horasFaltantes;
	}

	public double getHorasAbonadas() {
		return horasAbonadas;
	}

	public void setHorasAbonadas(double horasAbonadas) {
		this.horasAbonadas = horasAbonadas;
	}

}
