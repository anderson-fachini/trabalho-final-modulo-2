package br.udesc.ads.ponto.servicos.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Marcacao;
import br.udesc.ads.ponto.entidades.MarcacaoLida;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.services.leitoraponto.LeitoraPontoService;
import br.udesc.ads.ponto.services.leitoraponto.LeitoraPontoService_Service;
import br.udesc.ads.ponto.services.leitoraponto.RegistroMarcacao;
import br.udesc.ads.ponto.servicos.ColaboradorService;
import br.udesc.ads.ponto.util.TimeUtils;

public class ImportadorMarcacoes {

	private final EntityManager entityManager;
	private final LeitoraPontoService leitora;
	private final int tamanhoBloco;

	public ImportadorMarcacoes() {
		this.entityManager = Manager.get().getEntityManager();
		this.tamanhoBloco = Manager.get().getTamanhoBlocoLeituraMarcacoes();

		LeitoraPontoService_Service service = new LeitoraPontoService_Service();
		leitora = service.getLeitoraPontoServicePort();
	}

	public void importar() {
		// Importa da leitora para uma fila no banco, ignorando duplicidades:
		importarRegistrosDaLeitora();
		
		// Seleciona da fila e converte para Apuracoes:
		List<MarcacaoLida> lidas = selecionarMarcacoesLidas();
		List<Apuracao> apuracoes = converterRegistrosEmApuracoes(lidas);

		// Persiste as Apuracoes e remove da fila:
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			for (Apuracao apura : apuracoes) {
				entityManager.persist(apura);
			}
			for (MarcacaoLida lida : lidas) {
				entityManager.remove(lida);
			}
			transaction.commit();
		} catch (Throwable ex) {
			transaction.rollback();
			throw ex;
		}
	}

	private void importarRegistrosDaLeitora() {
		while (true) {
			List<RegistroMarcacao> registros = leitora.lerMarcacoes(tamanhoBloco);

			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			try {
				for (RegistroMarcacao reg : registros) {
					MarcacaoLida lida = new MarcacaoLida();
					lida.setId(reg.getId());
					lida.setCodFuncionario(reg.getCodFuncionario());
					lida.setData(getDateSection(reg.getMarcacao()));
					lida.setHora(getTimeSection(reg.getMarcacao()));

					// Caso a marca��o j� esteja na base, ignora.
					// (Isto pode acontecer se ficou uma opera��o incompleta)
					MarcacaoLida existente = entityManager.find(MarcacaoLida.class, reg.getId());
					if (!lida.equals(existente)) {
						entityManager.persist(lida);
					}
				}
				transaction.commit();
			} catch (Throwable ex) {
				transaction.rollback();
				throw ex;
			}
			confirmarRegistrosProcessados(registros);

			if (registros.size() < tamanhoBloco) {
				break;
			}
		}
	}

	private List<MarcacaoLida> selecionarMarcacoesLidas() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<MarcacaoLida> criteria = cb.createQuery(MarcacaoLida.class);
		Root<MarcacaoLida> root = criteria.from(MarcacaoLida.class);
		criteria.select(root).orderBy(cb.asc(root.get("data")), cb.asc(root.get("hora")));
		return entityManager.createQuery(criteria).getResultList();
	}

	private List<Apuracao> converterRegistrosEmApuracoes(List<MarcacaoLida> lidas) {

		Map<ApuracaoChave, Apuracao> map = new LinkedHashMap<>();

		for (MarcacaoLida lida : lidas) {
			Long codFunc = lida.getCodFuncionario();
			LocalDate data = lida.getData();
			LocalTime hora = lida.getHora();
			hora = TimeUtils.ajustarPrecisao(hora);
			ApuracaoChave chave = new ApuracaoChave(codFunc, data);

			Apuracao apuracao = map.get(chave);
			if (apuracao == null) {
				apuracao = new Apuracao();
				apuracao.setData(data);
				Colaborador col = ColaboradorService.get().getColaboradorPorCodigo(codFunc, false);
				apuracao.setColaborador(col);
				map.put(chave, apuracao);
			}
			apuracao.addMarcacao(new Marcacao(hora));
		}

		List<Apuracao> result = new ArrayList<>(map.values());
		return result;
	}

	private LocalDate getDateSection(XMLGregorianCalendar data) {
		return new LocalDate(data.getYear(), data.getMonth(), data.getDay());
	}

	private LocalTime getTimeSection(XMLGregorianCalendar hora) {
		return new LocalTime(hora.getHour(), hora.getMinute(), hora.getSecond());
	}

	private void confirmarRegistrosProcessados(List<RegistroMarcacao> registros) {

		// Gera uma c�pia da lista para n�o avacalhar com a original
		registros = new ArrayList<>(registros);

		// Ordena os registros por ID (para o caso de terem vindo fora de ordem)
		Collections.sort(registros, new Comparator<RegistroMarcacao>() {

			@Override
			public int compare(RegistroMarcacao o1, RegistroMarcacao o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});

		// Envia as confirma��es de leitura, em intervalos cont�guos
		if (registros.isEmpty()) {
			return;
		}
		long idInicial = registros.get(0).getId();
		long idFinal = idInicial;
		for (int i = 1; i < registros.size(); ++i) {
			long id = registros.get(i).getId();
			if (id != idFinal + 1) {
				leitora.confirmarLeitura(idInicial, idFinal);
				idInicial = id;
			}
			idFinal = id;
		}
		leitora.confirmarLeitura(idInicial, idFinal);
	}

	static class ApuracaoChave {

		public final Long codFunc;
		public final LocalDate data;

		public ApuracaoChave(Long codFunc, LocalDate data) {
			super();
			if (codFunc == null || data == null) {
				throw new NullPointerException(); // Safe programming
			}
			this.codFunc = codFunc;
			this.data = data;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof ApuracaoChave)) {
				return false;
			}
			ApuracaoChave other = (ApuracaoChave) obj;
			return codFunc.equals(other.codFunc) && data.equals(other.data);
		}

		@Override
		public int hashCode() {
			return codFunc.hashCode() * 7 + data.hashCode();
		}
	}

}
