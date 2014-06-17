package br.udesc.ads.ponto.servicos;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Marcacao;
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

	// TODO Verificar se é possível mockear Feriados, Config e outras apurações
	// para não precisar consultar no banco

	// Cenários Samuel:
	
	@Test
	public void testApuracao_Cenario1_NenhumaOcorrencia() {
		Apuracao apuracao = new Apuracao();
		apuracao.setColaborador(colaborador);
		apuracao.setData(new LocalDate(2014, 2, 6));
		apuracao.addMarcacao(new Marcacao(new LocalTime(8, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(12, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(13, 0, 0)));
		apuracao.addMarcacao(new Marcacao(new LocalTime(17, 0, 0)));

		apurador.processarApuracao(apuracao);

		Assert.assertEquals(true, apuracao.getApurada());
		Assert.assertEquals(0, apuracao.getOcorrenciasSize());
		Assert.assertEquals(new LocalTime(8, 0, 0), apuracao.getHorasTrabalhadas());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasExcedentes());
		Assert.assertEquals(new LocalTime(0, 0, 0), apuracao.getHorasFaltantes());
		// Não deve ter ocorrido nenhuma aprovação:
		Assert.assertNull(apuracao.getHorasAbonadas());
		Assert.assertNull(apuracao.getDataAprovacao());
		Assert.assertNull(apuracao.getResponsavelAprovacao());
	}
	
	// Cenários Daniel:

}
