package br.udesc.ads.ponto.entidades;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestColaboradorPersistence extends BasePersistenceTest {

	private Setor setor;

	@Before
	@Override
	public void setup() {
		super.setup();
		this.setor = insereSetor();
	}

	private Setor insereSetor() {
		Setor setor = new Setor();
		setor.setNome("Desenvolvimento Ponto Web");
		entityManager.persist(setor);

		Colaborador gerente = new Colaborador();
		gerente.setCpf("139.665.696-04");
		gerente.setNome("Daniel Rosa");
		gerente.setSaldoBH(34.56);
		gerente.setSetor(setor);
		entityManager.persist(gerente);

		setor.setGerente(gerente);
		entityManager.merge(setor);
		return setor;
	}

	@Test
	public void testInsereRecuperaColaborador() {
		Colaborador colaborador = new Colaborador();
		colaborador.setCodigo(1234L);
		colaborador.setCpf("789.256.836-01");
		colaborador.setNome("Samuel Yuri Deschamps");
		colaborador.setSaldoBH(15.45);
		colaborador.setSetor(setor);

		Assert.assertNull(colaborador.getId());
		entityManager.persist(colaborador);
		Assert.assertNotNull(colaborador.getId());

		Colaborador actual = entityManager.find(Colaborador.class, colaborador.getId());

		Assert.assertEquals(colaborador.getId(), actual.getId());
		Assert.assertEquals(new Long(1234L), actual.getCodigo());
		Assert.assertEquals("789.256.836-01", actual.getCpf());
		Assert.assertEquals("Samuel Yuri Deschamps", actual.getNome());
		Assert.assertEquals(15.45, actual.getSaldoBH(), 0.00001);
		Assert.assertTrue(setor == actual.getSetor());
	}

}
