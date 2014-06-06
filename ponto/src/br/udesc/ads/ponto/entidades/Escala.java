package br.udesc.ads.ponto.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Escala {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<EscalaMarcacao> marcacoes = new ArrayList<>();

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

	public void addMarcacao(EscalaMarcacao marcacao) {
		this.marcacoes.add(marcacao);
	}

	public int getMarcacoesSize() {
		return this.marcacoes.size();
	}

	public EscalaMarcacao getMarcacao(int index) {
		return this.marcacoes.get(index);
	}

}
