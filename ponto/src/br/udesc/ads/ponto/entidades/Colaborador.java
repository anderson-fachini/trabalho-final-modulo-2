package br.udesc.ads.ponto.entidades;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Colaborador {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true)
	private Long codigo;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false, unique = true, updatable = false)
	private String cpf;

	@Column(nullable = false)
	private BigDecimal saldoBH;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
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
