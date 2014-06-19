package br.udesc.ads.ponto.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Colaborador implements Serializable {

	private static final long serialVersionUID = 8527466841547829665L;

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
	private Double saldoBH = 0.00;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	private Setor setor;

	@Column(nullable = false)
	private Situacao situacao = Situacao.ATIVO;

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

	public Double getSaldoBH() {
		return saldoBH;
	}

	public void setSaldoBH(Double saldoBH) {
		this.saldoBH = saldoBH;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((saldoBH == null) ? 0 : saldoBH.hashCode());
		result = prime * result + System.identityHashCode(setor);
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Colaborador)) {
			return false;
		}
		Colaborador other = (Colaborador) obj;
		if (!equal(id, other.id)) {
			return false;
		}
		if (!equal(codigo, other.codigo)) {
			return false;
		}
		if (!equal(nome, other.nome)) {
			return false;
		}
		if (!equal(cpf, other.cpf)) {
			return false;
		}
		if (!equal(saldoBH, other.saldoBH)) {
			return false;
		}
		if (setor != other.setor) {
			return false;
		}
		if (situacao != other.situacao) {
			return false;
		}
		return true;
	}

	private boolean equal(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		}
		return o1.equals(o2);
	}

}
