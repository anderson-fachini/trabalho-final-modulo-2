package br.udesc.ads.ponto.entidades;

import java.util.List;

public class Escala {

	private Long id;
	private String nome;
	private List<EscalaMarcacao> marcacoes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<EscalaMarcacao> getMarcacoes() {
		return marcacoes;
	}

	public void setMarcacoes(List<EscalaMarcacao> marcacoes) {
		this.marcacoes = marcacoes;
	}

}
