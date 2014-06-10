package br.udesc.ads.ponto.entidades;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class TestFeriadoPersistence extends BasePersistenceTest {

	@Test
	public void testInsereRecuperaFeriado() {
		Feriado natal = new Feriado();
		natal.setData(new LocalDate(2014, 12, 25));
		natal.setNome("Natal");

		Assert.assertNull(natal.getId());
		entityManager.persist(natal);
		Assert.assertNotNull(natal.getId());

		Feriado actual = entityManager.find(Feriado.class, natal.getId());
		Assert.assertEquals("Natal", actual.getNome());
		Assert.assertEquals(new LocalDate(2014, 12, 25), actual.getData());
	}

}
