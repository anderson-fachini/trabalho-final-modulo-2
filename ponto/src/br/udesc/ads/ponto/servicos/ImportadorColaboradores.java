package br.udesc.ads.ponto.servicos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.entidades.Situacao;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.util.FileUtils;

public class ImportadorColaboradores {

	private final EntityManager entityManager;
	private final Set<Colaborador> colaboradoresProcessados = new HashSet<>();
	private final Set<Setor> setoresProcessados = new HashSet<>();

	public ImportadorColaboradores() {
		entityManager = Manager.get().getEntityManager();
	}

	public void importar(File arquivo) {
		colaboradoresProcessados.clear();
		setoresProcessados.clear();
		
		List<String> linhas;
		try {
			linhas = FileUtils.fileToString(arquivo);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			// Importa os colaboradores/setores:
			boolean primeiraLinha = true;
			for (String linha : linhas) {
				String[] tokens = linha.split(";");
				// Pula o header
				if (!primeiraLinha || isLongInteger(tokens[0])) {
					processaColaborador(tokens);
				}
				primeiraLinha = false;
			}
			
			// Marca os ausentes como Inativos:
			inativarColaboradoresAusentes();
			inativarSetoresAusentes();
			
			transaction.commit();
		} catch (Throwable ex) {
			transaction.rollback();
			throw ex;
		}
	}

	private void inativarColaboradoresAusentes() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<Colaborador> update = cb.createCriteriaUpdate(Colaborador.class);
		Root<Colaborador> root = update.from(Colaborador.class);
		update.set(root.get("situacao"), Situacao.INATIVO);
		Predicate in = root.get("id").in(getIdsColaboradores(colaboradoresProcessados));
		update.where(cb.not(in));
		entityManager.createQuery(update).executeUpdate();
	}

	private void inativarSetoresAusentes() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<Setor> update = cb.createCriteriaUpdate(Setor.class);
		Root<Setor> root = update.from(Setor.class);
		update.set(root.get("situacao"), Situacao.INATIVO);
		Predicate in = root.get("id").in(getIdsSetores(setoresProcessados));
		update.where(cb.not(in));
		entityManager.createQuery(update).executeUpdate();
	}
	
	private List<Long> getIdsSetores(Set<Setor> setores) {
		List<Long> result = new ArrayList<>();
		for (Setor s : setores) {
			result.add(s.getId());
		}
		return result;
	}
	
	private List<Long> getIdsColaboradores(Set<Colaborador> colaboradores) {
		List<Long> result = new ArrayList<>();
		for (Colaborador c : colaboradores) {
			result.add(c.getId());
		}
		return result;
	}

	private boolean isLongInteger(String string) {
		try {
			Long.parseLong(string);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	private void processaColaborador(String[] tokens) {
		long codigo = Long.parseLong(tokens[0]);
		boolean isGerente = tokens.length == 4;
		String codGerente = isGerente ? String.valueOf(codigo) : tokens[4];
		Setor setor = getSetor(tokens[3], codGerente);
		Colaborador col = ColaboradorService.get().getColaboradorPorCodigo(codigo, true);
		String cpf = tokens[2];
		cpf = cpf.replaceAll("\\D", "");
		if (col != null) {
			// O cara já existe
			col.setNome(tokens[1]);
			col.setCpf(cpf);
			col.setSetor(setor);
			col.setSituacao(Situacao.ATIVO);
			entityManager.merge(col);
		} else {
			// O cara é novo
			col = new Colaborador();
			col.setCodigo(codigo);
			col.setNome(tokens[1]);
			col.setCpf(cpf);
			col.setSetor(setor);
			col.setSaldoBH(0.00);
			entityManager.persist(col);
		}
		// Seta o cara atual como gerente e atualiza o setor:
		// Precisa ser feito posteriormente porque são tabelas mutuamente dependentes.
		if (isGerente && setor.getGerente() != col) {
			setor.setGerente(col);
			entityManager.merge(setor);
		}
		colaboradoresProcessados.add(col);
		setoresProcessados.add(col.getSetor());
	}

	private Setor getSetor(String nome, String codGerente) {
		long longCodGerente = Long.parseLong(codGerente);
		Colaborador gerente = ColaboradorService.get().getColaboradorPorCodigo(longCodGerente, true);
		Setor setor = getSetorPorNome(nome);
		if (setor == null) {
			// O setor é novo
			setor = new Setor();
			setor.setNome(nome);
			setor.setGerente(gerente);
			entityManager.persist(setor);
		} else {
			// O setor já existe
			if (setoresProcessados.contains(setor)) {
				if (!setor.getGerente().getCodigo().equals(longCodGerente)) {
					String fmt = "Gerentes diferentes informados para o setor '%s'. Valores: '%d', '%d'.";
					throw new RuntimeException(String.format(fmt, nome, setor.getGerente().getCodigo(), longCodGerente));
				}
			}
			setor.setGerente(gerente);
			setor.setSituacao(Situacao.ATIVO);
			entityManager.merge(setor);
		}
		return setor;
	}

	private Setor getSetorPorNome(String nome) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Setor> query = cb.createQuery(Setor.class);
		Root<Setor> root = query.from(Setor.class);
		query.select(root).where(cb.equal(root.get("nome"), nome));
		List<Setor> results = entityManager.createQuery(query).getResultList();
		if (results.isEmpty()) {
			return null;
		}
		if (results.size() > 1) {
			throw new AssertionError("Não deveria haver mais de um setor com mesmo nome.");
		}
		return results.get(0);
	}

	public static void main(String[] args) {

		new ImportadorColaboradores().importar(new File("C:\\colaboradores.csv"));

	}

	// TODO Ordenar os registros [gerentes primeiro] antes de importar (depende de um "CsvDataset").

}
