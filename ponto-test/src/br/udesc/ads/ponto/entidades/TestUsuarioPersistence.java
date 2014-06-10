package br.udesc.ads.ponto.entidades;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestUsuarioPersistence extends BasePersistenceTest {

	private Colaborador colaborador;

	@Before
	@Override
	public void setup() {
		super.setup();
		this.colaborador = insereColaborador(null);
	}
	
	private Colaborador insereColaborador(Setor setor) {
		Colaborador colaborador = new Colaborador();
		colaborador.setCodigo(1000L);
		colaborador.setCpf("789.256.836-01");
		colaborador.setNome("David Luiz");
		colaborador.setSaldoBH(BigDecimal.ZERO);
		colaborador.setSetor(setor);
		entityManager.persist(colaborador);
		return colaborador;
	}
	
	@Test
	public void testInsereRecuperaUsuario() {
		Usuario usuario = new Usuario();
		usuario.setNomeUsuario("samuel");
		usuario.setPerfil(PerfilUsuario.DONO_EMPRESA);
		usuario.setSenha("12345678");
		usuario.setSituacao(Situacao.ATIVO);
		usuario.setColaborador(colaborador);
		
		Assert.assertNull(usuario.getId());
		entityManager.persist(usuario);
		Assert.assertNotNull(usuario.getId());
		
		Usuario actual = entityManager.find(Usuario.class, usuario.getId());
		
		Assert.assertEquals("samuel", actual.getNomeUsuario());
		Assert.assertEquals(PerfilUsuario.DONO_EMPRESA, actual.getPerfil());
		Assert.assertEquals("12345678", actual.getSenha());
		Assert.assertEquals(Situacao.ATIVO, actual.getSituacao());
		Assert.assertTrue(colaborador == actual.getColaborador());
	}

}
