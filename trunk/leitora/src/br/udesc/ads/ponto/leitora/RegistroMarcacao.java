package br.udesc.ads.ponto.leitora;

import java.util.Date;

public class RegistroMarcacao {

	private Long id;
	private Long codFuncionario;
	private Date marcacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCodFuncionario() {
		return codFuncionario;
	}

	public void setCodFuncionario(Long codFuncionario) {
		this.codFuncionario = codFuncionario;
	}

	public Date getMarcacao() {
		return marcacao;
	}

	public void setMarcacao(Date marcacao) {
		this.marcacao = marcacao;
	}

	@Override
	public String toString() {
		return "RegistroMarcacao [id=" + id + ", codFuncionario=" + codFuncionario + ", marcacao=" + marcacao + "]";
	}

}
