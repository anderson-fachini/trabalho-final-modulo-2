package br.udesc.ads.ponto.jpaconverters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.udesc.ads.ponto.entidades.PerfilUsuario;

@Converter(autoApply = true)
public class PerfilUsuarioToStringConverter implements AttributeConverter<PerfilUsuario, String> {

	@Override
	public String convertToDatabaseColumn(PerfilUsuario perfil) {
		if (perfil == null) {
			return null;
		}
		return perfil.getId();
	}

	@Override
	public PerfilUsuario convertToEntityAttribute(String id) {
		if (id == null) {
			return null;
		}
		return PerfilUsuario.fromId(id);
	}

}
