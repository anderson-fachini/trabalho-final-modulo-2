package br.udesc.ads.ponto.services.leitoraponto;

import org.joda.time.LocalDateTime;

public class RegistroMarcacao {

	private Long id;
	private Long codFuncionario;
	private LocalDateTime marcacao;

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

	public LocalDateTime getMarcacao() {
		return marcacao;
	}

	public void setMarcacao(LocalDateTime marcacao) {
		this.marcacao = marcacao;
	}

	@Override
	public String toString() {
		return "RegistroMarcacao [id=" + id + ", codFuncionario=" + codFuncionario + ", marcacao=" + marcacao + "]";
	}

}
