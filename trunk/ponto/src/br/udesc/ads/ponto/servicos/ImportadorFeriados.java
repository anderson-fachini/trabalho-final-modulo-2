package br.udesc.ads.ponto.servicos;

import java.io.File;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.joda.time.LocalDate;

import br.udesc.ads.ponto.entidades.Feriado;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.util.csvdataset.CsvSortableDataset;
import br.udesc.ads.ponto.util.csvdataset.FieldType;

public class ImportadorFeriados {

	private EntityManager entityManager;

	public ImportadorFeriados() {
		entityManager = Manager.get().getEntityManager();
	}

	public void importar(File arquivo) {
		CsvSortableDataset dataset = new CsvSortableDataset();
		try {
			dataset.loadFromFile(arquivo);
			dataset.sort("data", FieldType.LOCALDATE, true);
			
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			try {
				while (dataset.next()) {
					if (existeFeriado(dataset.getAsLocalDate("data"))) {
						continue;
					}
					Feriado feriado = new Feriado();
					feriado.setData(dataset.getAsLocalDate("data"));
					feriado.setNome(dataset.getAsString("nome"));
					entityManager.persist(feriado);
				}
				transaction.commit();
			} catch (Throwable ex) {
				transaction.rollback();
				throw ex;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao carregar arquivo de feriados:\n" + e.getMessage());
		}
	}

	private boolean existeFeriado(LocalDate data) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Feriado> query = cb.createQuery(Feriado.class);
		Root<Feriado> root = query.from(Feriado.class);
		query.select(root).where(cb.equal(root.get("data"), data));
		return !entityManager.createQuery(query).getResultList().isEmpty();
	}

}
