package br.udesc.ads.ponto.entidades;

import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

public class TestEscalaPersistence extends PersistenceTest {

	@Test
	public void testInsereRecuperaEscala() {

		Escala escala = new Escala();
		escala.setNome("Escala padrão");
		escala.addMarcacao(new EscalaMarcacao(DiaSemana.DOMINGO, new LocalTime(8, 0)));
		escala.addMarcacao(new EscalaMarcacao(DiaSemana.SEGUNDA_FEIRA, new LocalTime(12, 0)));
		escala.addMarcacao(new EscalaMarcacao(DiaSemana.TERCA_FEIRA, new LocalTime(13, 30)));
		escala.addMarcacao(new EscalaMarcacao(DiaSemana.SABADO, new LocalTime(18, 0)));

		Assert.assertNull(escala.getId());
		entityManager.persist(escala);
		Assert.assertNotNull(escala.getId());

		Escala actual = entityManager.find(Escala.class, escala.getId());
		Assert.assertEquals("Escala padrão", actual.getNome());
		Assert.assertEquals(4, actual.getMarcacoesSize());
		
		Assert.assertEquals(DiaSemana.DOMINGO, actual.getMarcacao(0).getDiaSemana());
		Assert.assertEquals(DiaSemana.SEGUNDA_FEIRA, actual.getMarcacao(1).getDiaSemana());
		Assert.assertEquals(DiaSemana.TERCA_FEIRA, actual.getMarcacao(2).getDiaSemana());
		Assert.assertEquals(DiaSemana.SABADO, actual.getMarcacao(3).getDiaSemana());
		
		Assert.assertEquals(new LocalTime(8, 0), actual.getMarcacao(0).getHora());
		Assert.assertEquals(new LocalTime(12, 0), actual.getMarcacao(1).getHora());
		Assert.assertEquals(new LocalTime(13, 30), actual.getMarcacao(2).getHora());
		Assert.assertEquals(new LocalTime(18, 0), actual.getMarcacao(3).getHora());
	}

}
