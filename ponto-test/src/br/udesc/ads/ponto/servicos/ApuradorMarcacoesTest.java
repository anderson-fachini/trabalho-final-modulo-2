package br.udesc.ads.ponto.servicos;

import java.util.Arrays;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Marcacao;
import br.udesc.ads.ponto.entidades.Ocorrencia;
import br.udesc.ads.ponto.entidades.Situacao;

public class ApuradorMarcacoesTest {

	private ApuradorMarcacoes apurador;
	private Colaborador colaborador;

	@Before
	public void setup() {
		apurador = new ApuradorMarcacoes();
		colaborador = criarColaboradorZerado();
	}

	private Colaborador criarColaboradorZerado() {
		Colaborador result = new Colaborador();
		result.setCodigo(1000L);
		result.setCpf("86312523446");
		result.setNome("Amanda C. Moreira");
		result.setSaldoBH(0.0);
		result.setSituacao(Situacao.ATIVO);
		return result;
	}

	private void assertOcorrencias(Ocorrencia[] ocorrencias, Apuracao apuracao) {
		Ocorrencia[] actual = new Ocorrencia[apuracao.getOcorrenciasSize()];
		for (int i = 0; i < actual.length; ++i) {
			actual[i] = apuracao.getOcorrencia(i);
		}
		Assert.assertEquals(Arrays.toString(ocorrencias), Arrays.toString(actual));
	}

	// TODO Verificar se é possível mockear Feriados, Config e outras apurações
	// para não precisar consultar no banco

	// Cenários Samuel:

	@Test
	public void testApuracao_1_NenhumaOcorrencia() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] {}, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	// TODO Este cenário deveria ter outro nome
	@Test
	public void testApuracao_2_OcorrenciaMarcacoesForaPadrao_1() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 5, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 5, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 5, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 5, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] {}, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	// TODO Este cenário deveria ter outro nome
	@Test
	public void testApuracao_3_OcorrenciaMarcacoesForaPadrao_2() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(7, 55, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(11, 55, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 55, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 55, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] {}, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	@Test
	public void testApuracao_4_OcorrenciaMarcacoesForaPadrao_3() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(7, 50, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 10, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	@Test
	public void testApuracao_5_OcorrenciaMarcacoesForaPadrao_4() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 10, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 50, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	@Test
	public void testApuracao_6_OcorrenciaMarcacoesExcedentes() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(7, 59, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_EXCEDENTES }, apuracao);
		// Não deve-se calcular as horas quando existe número ímpar de
		// marcações:
		Assert.assertNull(apuracao.getHorasTrabalhadas());
		Assert.assertNull(apuracao.getHorasExcedentes());
		Assert.assertNull(apuracao.getHorasFaltantes());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getHorasAbonadas());
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	@Test
	public void testApuracao_7_OcorrenciaMarcacoesFaltantes() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 11, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 55, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 1, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.MARCACOES_FALTANTES }, apuracao);
		// Não deve-se calcular as horas quando existe número ímpar de
		// marcações:
		Assert.assertNull(apuracao.getHorasTrabalhadas());
		Assert.assertNull(apuracao.getHorasExcedentes());
		Assert.assertNull(apuracao.getHorasFaltantes());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getHorasAbonadas());
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	@Test
	public void testApuracao_8_OcorrenciaMarcacoesFaltantes() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 2, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 58, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		Ocorrencia[] ocorrencias = new Ocorrencia[] { //
		Ocorrencia.HORAS_EXCEDENTES, //
				Ocorrencia.MARCACOES_FALTANTES, //
				Ocorrencia.INTERVALO_TRABALHO_EXCEDIDO };
		assertOcorrencias(ocorrencias, apuracao);
		Assert.assertEquals(new LocalTime(8, 56, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 56, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	@Test
	public void testApuracao_9_OcorrenciaMarcacoesFaltantes() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 3, 25)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FALTANTES }, apuracao);
		// Não deve-se calcular as horas quando existe número ímpar de
		// marcações:
		Assert.assertNull(apuracao.getHorasTrabalhadas());
		Assert.assertNull(apuracao.getHorasExcedentes());
		Assert.assertNull(apuracao.getHorasFaltantes());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getHorasAbonadas());
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	// Cenários Daniel:

}
