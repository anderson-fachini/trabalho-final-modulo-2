package br.udesc.ads.ponto.servicos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.Apuracao;
import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Marcacao;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.services.leitoraponto.LeitoraPontoService;
import br.udesc.ads.ponto.services.leitoraponto.LeitoraPontoService_Service;
import br.udesc.ads.ponto.services.leitoraponto.RegistroMarcacao;

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
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
		
			List<RegistroMarcacao> registros = lerRegistrosDaLeitora();
			List<Apuracao> apuracoes = converterRegistrosEmApuracoes(registros);
			for (Apuracao apura : apuracoes) {
				entityManager.persist(apura);
			}
			confirmarRegistrosProcessados(registros);
			
			transaction.commit();
		} catch (Throwable ex) {
			transaction.rollback();
			throw ex;
		}
	}

	private List<RegistroMarcacao> lerRegistrosDaLeitora() {
		List<RegistroMarcacao> registros = new ArrayList<>();
		while (true) {
			List<RegistroMarcacao> bloco = leitora.lerMarcacoes(tamanhoBloco);
			registros.addAll(bloco);
			// TODO Para cada bloco lido, já precisa ser processado (ou pelo menos enviada a confirmação de recebimento).
			// TODO Tratar recebimento parcial de Apuracoes?
			if (bloco.size() < tamanhoBloco) {
				break;
			}
		}
		return registros;
	}

	private List<Apuracao> converterRegistrosEmApuracoes(List<RegistroMarcacao> registros) {

		Map<ApuracaoChave, Apuracao> map = new LinkedHashMap<>();

		for (RegistroMarcacao reg : registros) {
			Long codFunc = reg.getCodFuncionario();
			LocalDate data = getDateSection(reg.getMarcacao());
			LocalTime hora = getTimeSection(reg.getMarcacao());
			ApuracaoChave chave = new ApuracaoChave(codFunc, data);

			Apuracao apuracao = map.get(chave);
			if (apuracao == null) {
				apuracao = new Apuracao();
				apuracao.setData(data);
				apuracao.setColaborador(getColaboradorPorCodigo(codFunc));
				map.put(chave, apuracao);
			}
			apuracao.addMarcacao(new Marcacao(hora));
		}

		List<Apuracao> result = new ArrayList<>(map.values());
		// TODO Ordernar as marcações por hora dentro de cada apuração.
		return result;
	}

	private LocalDate getDateSection(XMLGregorianCalendar data) {
		return new LocalDate(data.getYear(), data.getMonth(), data.getDay());
	}

	private LocalTime getTimeSection(XMLGregorianCalendar hora) {
		return new LocalTime(hora.getHour(), hora.getMinute(), hora.getSecond());
	}

	private Colaborador getColaboradorPorCodigo(Long codFunc) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Colaborador> criteria = builder.createQuery(Colaborador.class);
		Root<Colaborador> root = criteria.from(Colaborador.class);
		criteria.select(root).where(builder.equal(root.get("codigo"), codFunc));
		try {
			return entityManager.createQuery(criteria).getSingleResult();
		} catch (NoResultException ex) {
			throw new RuntimeException(String.format("Nenhum colaborador encontrado com código '%d'.", codFunc));
		}
	}

	private void confirmarRegistrosProcessados(List<RegistroMarcacao> registros) {

		// Gera uma cópia da lista para não avacalhar com a original
		registros = new ArrayList<>(registros);

		// Ordena os registros por ID (para o caso de terem vindo fora de ordem)
		Collections.sort(registros, new Comparator<RegistroMarcacao>() {

			@Override
			public int compare(RegistroMarcacao o1, RegistroMarcacao o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});

		// Envia as confirmações de leitura, em intervalos contíguos
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
