package br.udesc.ads.ponto.entidades;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestApuracaoPersistence extends BasePersistenceTest {

	private Colaborador colaborador;

	@Before
	@Override
	public void setup() {
		super.setup();

		Setor setor = insereSetor();
		this.colaborador = insereColaborador(setor);
	}

	private Setor insereSetor() {
		Setor setor = new Setor();
		setor.setNome("Almoxarifado");
		entityManager.persist(setor);

		Colaborador gerente = new Colaborador();
		gerente.setCpf("139.665.696-04");
		gerente.setNome("Jorge Steinback");
		gerente.setSaldoBH(1.0);
		gerente.setSetor(setor);
		entityManager.persist(gerente);

		setor.setGerente(gerente);
		entityManager.merge(setor);
		return setor;
	}

	private Colaborador insereColaborador(Setor setor) {
		Colaborador colaborador = new Colaborador();
		colaborador.setCodigo(1000L);
		colaborador.setCpf("789.256.836-01");
		colaborador.setNome("David Luiz");
		colaborador.setSaldoBH(1.0);
		colaborador.setSetor(setor);
		entityManager.persist(colaborador);
		return colaborador;
	}

	@Test
	public void testInsereRecuperaApuracao_Minima() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 6, 7));

		Assert.assertNull(apuracao.getId());
		entityManager.persist(apuracao);
		Assert.assertNotNull(apuracao.getId());

		Apuracao actual = entityManager.find(Apuracao.class, apuracao.getId());

		Assert.assertEquals(apuracao.getId(), actual.getId());
		Assert.assertEquals(new LocalDate(2014, 6, 7), actual.getData());
		Assert.assertTrue(colaborador == actual.getColaborador());
	}

	@Test
	public void testInsereRecuperaApuracao_ComHorasCalculadas() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 6, 7));
		apuracao.setHorasTrabalhadas(new LocalTime(8, 20));
		apuracao.setHorasExcedentes(new LocalTime(1, 10));
		apuracao.setHorasFaltantes(new LocalTime(2, 38));
		apuracao.setHorasAbonadas(new LocalTime(1, 14));

		Assert.assertNull(apuracao.getId());
		entityManager.persist(apuracao);
		Assert.assertNotNull(apuracao.getId());

		Apuracao actual = entityManager.find(Apuracao.class, apuracao.getId());

		Assert.assertEquals(apuracao.getId(), actual.getId());
		Assert.assertEquals(new LocalDate(2014, 6, 7), actual.getData());
		Assert.assertTrue(colaborador == actual.getColaborador());
		Assert.assertEquals(new LocalTime(8, 20), actual.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(1, 10), actual.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(2, 38), actual.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(1, 14), actual.getHorasAbonadas());
	}

	@Test
	public void testInsereRecuperaApuracao_ComMarcacoes() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 6, 7));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 4)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(11, 58)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 29)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 57)));
		apuracao.setApurada(true);

		Assert.assertNull(apuracao.getId());
		entityManager.persist(apuracao);
		Assert.assertNotNull(apuracao.getId());

		Apuracao actual = entityManager.find(Apuracao.class, apuracao.getId());

		Assert.assertEquals(apuracao.getId(), actual.getId());
		Assert.assertEquals(new LocalDate(2014, 6, 7), actual.getData());
		Assert.assertTrue(colaborador == actual.getColaborador());
		Assert.assertEquals(4, actual.getMarcacoesSize());
		Assert.assertEquals(new LocalTime(8, 4), actual.getMarcacao(0).getHora());
		Assert.assertEquals(new LocalTime(11, 58), actual.getMarcacao(1).getHora());
		Assert.assertEquals(new LocalTime(13, 29), actual.getMarcacao(2).getHora());
		Assert.assertEquals(new LocalTime(17, 57), actual.getMarcacao(3).getHora());
		Assert.assertTrue(actual == actual.getMarcacao(0).getApuracao());
		Assert.assertTrue(actual == actual.getMarcacao(1).getApuracao());
		Assert.assertTrue(actual == actual.getMarcacao(2).getApuracao());
		Assert.assertTrue(actual == actual.getMarcacao(3).getApuracao());
		Assert.assertEquals(true, actual.getApurada());
	}

	@Test
	public void testInsereRecuperaApuracao_ComOcorrencias() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 6, 7));
		apuracao.addOcorrencia(Ocorrencia.HORAS_EXCEDENTES);
		apuracao.addOcorrencia(Ocorrencia.INTERVALO_ALMOCO_INCOMPLETO);

		Assert.assertNull(apuracao.getId());
		entityManager.persist(apuracao);
		Assert.assertNotNull(apuracao.getId());

		Apuracao actual = entityManager.find(Apuracao.class, apuracao.getId());

		Assert.assertEquals(apuracao.getId(), actual.getId());
		Assert.assertEquals(new LocalDate(2014, 6, 7), actual.getData());
		Assert.assertTrue(colaborador == actual.getColaborador());
		Assert.assertEquals(2, actual.getOcorrenciasSize());
		Assert.assertEquals(Ocorrencia.HORAS_EXCEDENTES, actual.getOcorrencia(0));
		Assert.assertEquals(Ocorrencia.INTERVALO_ALMOCO_INCOMPLETO, actual.getOcorrencia(1));
	}

	@Test
	public void testInsereRecuperaApuracao_ComAbonos() {

		MotivoAbono motivoConsulta = new MotivoAbono();
		motivoConsulta.setDescricao("Consulta médica");
		entityManager.persist(motivoConsulta);

		MotivoAbono motivoCachaca = new MotivoAbono();
		motivoCachaca.setDescricao("Bebendo cachaça com os amigos");
		entityManager.persist(motivoCachaca);

		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 6, 7));
		apuracao.addAbono(new Abono(new LocalTime(8, 0), new LocalTime(12, 0), motivoConsulta));
		apuracao.addAbono(new Abono(new LocalTime(13, 30), new LocalTime(18, 0), motivoCachaca));

		Assert.assertNull(apuracao.getId());
		entityManager.persist(apuracao);
		Assert.assertNotNull(apuracao.getId());

		Apuracao actual = entityManager.find(Apuracao.class, apuracao.getId());

		Assert.assertEquals(apuracao.getId(), actual.getId());
		Assert.assertEquals(new LocalDate(2014, 6, 7), actual.getData());
		Assert.assertTrue(colaborador == actual.getColaborador());
		Assert.assertEquals(2, actual.getAbonosSize());
		Assert.assertTrue(motivoConsulta == actual.getAbono(0).getMotivo());
		Assert.assertTrue(motivoCachaca == actual.getAbono(1).getMotivo());
		Assert.assertTrue(actual == actual.getAbono(0).getApuracao());
		Assert.assertTrue(actual == actual.getAbono(1).getApuracao());
	}
}
