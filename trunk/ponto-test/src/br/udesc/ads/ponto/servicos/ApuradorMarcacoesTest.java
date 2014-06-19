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
import br.udesc.ads.ponto.servicos.impl.ApuradorMarcacoes;

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

	// Cenários mapeados no arquivo "Marcações para testes automatizados":

	//TC1 - Cenário 1: Nenhuma ocorrência
	@Test
	public void testApuracao_1_NenhumaOcorrencia_1() {
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
	
	//TC1 - Cenário 2: Nenhuma ocorrência com tolerância de 1 minuto antes em todas as marcações
	@Test
	public void testApuracao_2_NenhumaOcorrencia_2() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(7, 59, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(11, 59, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 59, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 59, 0)));

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
	
	//TC1 - Cenário 3: Nenhuma ocorrência com tolerância de 1 minuto depois em todas as marcações
	@Test
	public void testApuracao_3_NenhumaOcorrencia_3() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 1, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 1, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 1, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 1, 0)));

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
	
	//TC1 - Cenário 4: Nenhuma ocorrência com tolerância de 1 minuto antes nas marcações da manhã e 1 minuto depois nas marcações da tarde
	@Test
	public void testApuracao_4_NenhumaOcorrencia_4() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(7, 59, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(11, 59, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 1, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 1, 0)));

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
	
	//TC1 - Cenário 5: Ocorrência de intervalo de almoço menor que 1h sem gerar extras ou faltas com tolerância de 1 minuto depois marcações da manhã e 1 minuto antes nas marcações da tarde
	@Test
	public void testApuracao_5_AlmocoIncompleto_1() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 1, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 1, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 59, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 59, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.INTERVALO_ALMOCO_INCOMPLETO}, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 6: Ocorrência de marcações fora da escala padrão com 6 minutos antes em todas as marcações
	@Test
	public void testApuracao_6_OcorrenciaMarcacoesForaPadrao_1() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(7, 54, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(11, 54, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 54, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 54, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA}, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 7: Ocorrência de marcações fora da escala padrão com 6 minutos depois em todas as marcações
	@Test
	public void testApuracao_7_OcorrenciaMarcacoesForaPadrao_2() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 6, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 6, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 6, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 6, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA}, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 8: Ocorrência de marcações fora da escala padrão com 10 minutos depois na primeira marcação e 10 minutos antes na última marcação
	@Test
	public void testApuracao_8_OcorrenciaMarcacoesForaPadrao_3() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 10, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 50, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA}, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 9: Ocorrência de marcações fora da escala padrão com 10 minutos antes na primeira marcação e 10 minutos depois na última marcação
	@Test
	public void testApuracao_9_OcorrenciaMarcacoesForaPadrao_4() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(7, 50, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 10, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA}, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 10: Ocorrência de marcações fora da escala padrão com 10 minutos antes na segunda marcação e 10 minutos depois na terceira marcação
	@Test
	public void testApuracao_10_OcorrenciaMarcacoesForaPadrao_5() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(11, 50, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 10, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA}, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 11: Ocorrência de almoço incompleto e marcações fora da escala padrão com 10 minutos depois na segunda marcação e 10 minutos antes na terceira marcação
	@Test
	public void testApuracao_11_AlmocoIncompleto_2() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 10, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 50, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.INTERVALO_ALMOCO_INCOMPLETO }, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 12: Ocorrência de marcações fora da escala padrão com 4 marcações padrão, 2 marcações de intervalo de 1h e 2 marcações de recuperação de hora; Ocorrência de marcações excedentes com 8 marcações
	@Test
	public void testApuracao_12_OcorrenciaMarcacoesForaPadrao_6() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(9, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(10, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(18, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(19, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.MARCACOES_EXCEDENTES }, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 13: Ocorrência de horas faltantes com 11 minutos depois na primeira marcação e 10 minutos antes na última marcação
	@Test
	public void testApuracao_13_OcorrenciaHorasFaltantes_1() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 11, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 50, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_FALTANTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(7, 39, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 21, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 14: Ocorrência de horas faltantes com 10 minutos depois na primeira marcação e 11 minutos antes na última marcação
	@Test
	public void testApuracao_14_OcorrenciaHorasFaltantes_2() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 10, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 49, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_FALTANTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(7, 39, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 21, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 15: Ocorrência de horas faltantes com 11 minutos depois na primeira marcação e 11 minutos antes na última marcação
	@Test
	public void testApuracao_15_OcorrenciaHorasFaltantes_3() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 11, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 49, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_FALTANTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(7, 38, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 22, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 16: Ocorrência de horas faltantes com 10 minutos antes na segunda marcação e 11 minutos depois na terceira marcação
	@Test
	public void testApuracao_16_OcorrenciaHorasFaltantes_4() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(11, 50, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 11, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_FALTANTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(7, 39, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 21, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 17: Ocorrência de horas faltantes com 11 minutos antes na segunda marcação e 10 minutos depois na terceira marcação
	@Test
	public void testApuracao_17_OcorrenciaHorasFaltantes_5() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(11, 49, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 10, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_FALTANTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(7, 39, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 21, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 18: Ocorrência de horas faltantes com 11 minutos antes na segunda marcação e 11 minutos depois na terceira marcação
	@Test
	public void testApuracao_18_OcorrenciaHorasFaltantes_6() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(11, 49, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 11, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_FALTANTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(7, 38, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 22, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
		
	//TC1 - Cenário 19: Ocorrência de horas faltantes com 6 minutos depois da primeira marcação, 6 minutos antes na segunda marcação, 6 minutos depois da terceira marcação e 6 minutos antes da ultima marcação
	@Test
	public void testApuracao_19_OcorrenciaHorasFaltantes_7() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 6, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(11, 54, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 6, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 54, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_FALTANTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(7, 36, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 24, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}	
	
	//TC1 - Cenário 20: Ocorrência de horas faltantes com 4 marcações padrão e duas de intervalo de 1h; Ocorrência de marcações excedentes com 6 marcações
	@Test
	public void testApuracao_20_OcorrenciaHorasFaltantes_8() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(15, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_FALTANTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.MARCACOES_EXCEDENTES }, apuracao);
		Assert.assertEquals(new LocalTime(7, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(1, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 21: Ocorrência de marcações excedentes com 5 marcações
	@Test
	public void testApuracao_21_MarcacoesExcedentes_1() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(15, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.MARCACOES_EXCEDENTES }, apuracao);
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}	
	
	//TC1 - Cenário 22: Ocorrência de marcações faltantes com 3 marcações
	@Test
	public void testApuracao_22_MarcacoesFaltantes_2() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FALTANTES }, apuracao);
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 23: Ocorrência de marcações faltantes com 2 marcações; Ocorrência de horas ininterruptas maior que 6h
	@Test
	public void testApuracao_23_MarcacoesFaltantes_3() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_EXCEDENTES, Ocorrencia.MARCACOES_FALTANTES, Ocorrencia.INTERVALO_TRABALHO_EXCEDIDO }, apuracao);
		Assert.assertEquals(new LocalTime(9, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(1, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 24: Ocorrência de marcações faltantes com 2 marcações; Ocorrência de horas faltantes
	@Test
	public void testApuracao_24_MarcacoesFaltantes_4() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_FALTANTES, Ocorrencia.MARCACOES_FALTANTES }, apuracao);
		Assert.assertEquals(new LocalTime(4, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(4, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}	
	
	//TC1 - Cenário 25: Ocorrência de marcações faltantes com 1 marcação
	@Test
	public void testApuracao_25_MarcacoesFaltantes_5() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FALTANTES }, apuracao);
		//by daniel
		//Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasTrabalhadas());
		//Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		//Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasFaltantes());
		//Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	//TC1 - Cenário 26: Ocorrência de horas excedentes com 11 minutos antes na primeira marcação e 10 minutos depois na última marcação
	@Test
	public void testApuracao_26_HorasExcedentes_1() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(7, 49, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 10, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_EXCEDENTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(8, 21, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 21, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 27: Ocorrência de horas excedentes com 10 minutos antes na primeira marcação e 11 minutos depois na última marcação
	@Test
	public void testApuracao_27_HorasExcedentes_2() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(7, 50, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 11, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_EXCEDENTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(8, 21, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 21, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 28: Ocorrência de horas excedentes com 11 minutos antes na primeira marcação e 11 minutos depois na última marcação
	@Test
	public void testApuracao_28_HorasExcedentes_3() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(7, 49, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 11, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_EXCEDENTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao);
		Assert.assertEquals(new LocalTime(8, 22, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 22, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 29: Ocorrência de horas excedentes com 10 minutos depois na segunda marcação e 11 minutos antes na terceira marcação; Ocorrência de intervalo de almoço menor que 1h
	@Test
	public void testApuracao_29_HorasExcedentes_4() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 10, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 49, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_EXCEDENTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.INTERVALO_ALMOCO_INCOMPLETO }, apuracao);
		Assert.assertEquals(new LocalTime(8, 21, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 21, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 30: Ocorrência de horas excedentes com 11 minutos depois na segunda marcação e 10 antes depois na terceira marcação; Ocorrência de intervalo de almoço menor que 1h
	@Test
	public void testApuracao_30_HorasExcedentes_5() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 11, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 50, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_EXCEDENTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.INTERVALO_ALMOCO_INCOMPLETO }, apuracao);
		Assert.assertEquals(new LocalTime(8, 21, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 21, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}

	//TC1 - Cenário 31: Ocorrência de horas excedentes com 11 minutos depois na segunda marcação e 11 minutos antes na terceira marcação; Ocorrência de intervalo de almoço menor que 1h
	@Test
	public void testApuracao_31_HorasExcedentes_6() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 11, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 49, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_EXCEDENTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.INTERVALO_ALMOCO_INCOMPLETO }, apuracao);
		Assert.assertEquals(new LocalTime(8, 22, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 22, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 32: Ocorrência de intervalo de almoço menor que 1h com 1 minuto depois na segunda marcação e 0 minuto antes na terceira marcação
	@Test
	public void testApuracao_32_AlmocoIncompleto_3() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 1, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.INTERVALO_ALMOCO_INCOMPLETO }, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 33: Ocorrência de intervalo de almoço menor que 1h com 0 minuto depois na segunda marcação e 1 minuto antes na terceira marcação
	@Test
	public void testApuracao_33_AlmocoIncompleto_4() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 59, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.INTERVALO_ALMOCO_INCOMPLETO }, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 34: Ocorrência de interjornada menor que 11h com início do trabalho às 8h do dia 1 e nova jornada às 3:59 do dia seguinte; Ocorrência de marcações fora da escala padrão
	/*@Test
	public void testApuracao_34_InterjornadaIncompleto_1() {

		//Marcações do PRIMEIRO dia		
		Apuracao apuracao1 = new Apuracao();
		apuracao1.setColaborador(colaborador);
		apuracao1.setData(new LocalDate(2014, 2, 6));
		apuracao1.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao1.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao1.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao1.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));
		
		apurador.processarApuracao(apuracao1);
		
		Assert.assertEquals(true, apuracao1.getApurada());
		assertOcorrencias(new Ocorrencia[] { }, apuracao1);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao1.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao1.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao1.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao1.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao1.getDataAprovacao());
		Assert.assertNull(apuracao1.getResponsavelAprovacao());
		
		//Marcações do SEGUNDO dia
		Apuracao apuracao2 = new Apuracao();
		apuracao2.setColaborador(colaborador);
		apuracao2.setData(new LocalDate(2014, 3, 6));
		apuracao2.addMarcacao(new Marcacao(new LocalTime(3, 59, 0)));
		apuracao2.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao2.addMarcacao(new Marcacao(new LocalTime(9, 0, 0)));
		apuracao2.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));

		apurador.processarApuracao(apuracao2);

		Assert.assertEquals(true, apuracao2.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.INTERVALO_INTERJORNADAS_INCOMPLETO }, apuracao2);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao2.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao2.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao2.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao2.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao2.getDataAprovacao());
		Assert.assertNull(apuracao2.getResponsavelAprovacao());
	}*/
	
	//TC1 - Cenário 35: Ocorrência de interjornada menor que 11h com início do trabalho às 12:01h do dia 1 e nova jornada às 8:00 do dia seguinte; Ocorrência de marcações fora da escala padrão
	/*@Test
	public void testApuracao_35_InterjornadaIncompleto_2() {

		//Marcações do PRIMEIRO dia		
		Apuracao apuracao1 = new Apuracao();
		apuracao1.setColaborador(colaborador);
		apuracao1.setData(new LocalDate(2014, 2, 6));
		apuracao1.addMarcacao(new Marcacao(new LocalTime(12, 1, 0)));
		apuracao1.addMarcacao(new Marcacao(new LocalTime(16, 0, 0)));
		apuracao1.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));
		apuracao1.addMarcacao(new Marcacao(new LocalTime(21, 0, 0)));
		
		apurador.processarApuracao(apuracao1);
		
		Assert.assertEquals(true, apuracao1.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA }, apuracao1);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao1.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao1.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao1.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao1.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao1.getDataAprovacao());
		Assert.assertNull(apuracao1.getResponsavelAprovacao());
		
		//Marcações do SEGUNDO dia
		Apuracao apuracao2 = new Apuracao();
		apuracao2.setColaborador(colaborador);
		apuracao2.setData(new LocalDate(2014, 3, 6));
		apuracao2.addMarcacao(new Marcacao(new LocalTime(7, 59, 0)));
		apuracao2.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao2.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao2.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao2);

		Assert.assertEquals(true, apuracao2.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.INTERVALO_INTERJORNADAS_INCOMPLETO }, apuracao2);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao2.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao2.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao2.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao2.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao2.getDataAprovacao());
		Assert.assertNull(apuracao2.getResponsavelAprovacao());
	}*/
	
	//TC1 - Cenário 36: Ocorrência de horas ininterruptas maior que 6h com intervalo no meio da manhã
	@Test
	public void testApuracao_36_IntervaloTrabalho_1() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(9, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(10, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.INTERVALO_TRABALHO_EXCEDIDO }, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 37: Ocorrência de horas ininterruptas maior que 6h com intervalo no meio da tarde
	@Test
	public void testApuracao_37_IntervaloTrabalho_2() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(15, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(16, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.INTERVALO_TRABALHO_EXCEDIDO }, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 38: Ocorrência de intervalo menor que 15min entre período de 6h após o almoço e 1h extra à noite
	@Test
	public void testApuracao_38_IntervaloIntrajornada_1() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 10, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(18, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_EXCEDENTES, Ocorrencia.MARCACOES_FORA_DA_ESCALA, Ocorrencia.INTERVALO_INTRAJORNADA_INCOMPLETO, Ocorrencia.MARCACOES_EXCEDENTES }, apuracao);
		Assert.assertEquals(new LocalTime(8, 50, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 50, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	//TC1 - Cenário 39: Ocorrência de horas excedentes por trabalho no Feriado
	@Test
	public void testApuracao_39_IntervaloIntrajornada_1() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 1, 1));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		assertOcorrencias(new Ocorrencia[] { Ocorrencia.HORAS_EXCEDENTES }, apuracao);
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasAbonadas());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
}
