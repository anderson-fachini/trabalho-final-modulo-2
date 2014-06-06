package br.udesc.ads.ponto.entidades;

import java.math.BigDecimal;

public class Colaborador {

	private Long id;
	private Long codigo;
	private String nome;
	private String cpf;
	private BigDecimal saldoBH;
	private Setor setor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public BigDecimal getSaldoBH() {
		return saldoBH;
	}

	public void setSaldoBH(BigDecimal saldoBH) {
		this.saldoBH = saldoBH;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

}
