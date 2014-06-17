package br.udesc.ads.ponto.servicos;

import java.io.File;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
					if (FeriadoService.get().existeFeriado(dataset.getLocalDate("data"))) {
						continue;
					}
					Feriado feriado = new Feriado();
					feriado.setData(dataset.getLocalDate("data"));
					feriado.setNome(dataset.getString("nome"));
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

}
