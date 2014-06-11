package br.udesc.ads.ponto.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Representa um registro de marcação importado da leitora de ponto.<br>
 * Este objeto é intermediário: nasce e morre durante a operação de importação.
 * Seu objetivo é garantir que as informações não sejam perdidas em caso de
 * interrupção abrupta ou erro na importação.
 * 
 * @author Samuel
 * 
 */
@Entity
public class MarcacaoLida {

	@Id
	private Long id;

	@Column(nullable = false, updatable = false)
	private Long codFuncionario;

	@Column(columnDefinition = "DATE", nullable = false, updatable = false)
	private LocalDate data;

	@Column(columnDefinition = "TIME", nullable = false, updatable = false)
	private LocalTime hora;

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

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof MarcacaoLida)) {
			return false;
		}
		MarcacaoLida other = (MarcacaoLida) obj;
		return equal(this.id, other.id) //
				&& equal(this.codFuncionario, other.codFuncionario) //
				&& equal(this.data, other.data) //
				&& equal(this.hora, other.hora);
	}

	private boolean equal(Object obj1, Object obj2) {
		if (obj1 == null) {
			return obj2 == null;
		}
		return obj1.equals(obj2);
	}

	@Override
	public int hashCode() {
		int res = this.id == null ? 0 : this.id.hashCode();
		res += 7 * (this.codFuncionario == null ? 0 : this.codFuncionario.hashCode());
		res += 13 * (this.data == null ? 0 : this.data.hashCode());
		res += 17 * (this.hora == null ? 0 : this.hora.hashCode());
		return res;
	}

}
