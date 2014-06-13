package br.udesc.ads.ponto.servicos;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.udesc.ads.ponto.entidades.Colaborador;
import br.udesc.ads.ponto.entidades.Setor;
import br.udesc.ads.ponto.manager.Manager;
import br.udesc.ads.ponto.util.FileUtils;

public class ImportadorColaboradores {

	private final EntityManager entityManager;

	public ImportadorColaboradores() {
		entityManager = Manager.get().getEntityManager();
	}

	public void importar(File arquivo) {
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
			boolean primeiraLinha = true;
			for (String linha : linhas) {
				String[] tokens = linha.split(";");
				if (primeiraLinha && !isLongInteger(tokens[0])) {
					// Pula o header
					primeiraLinha = false;
					continue;
				}
				primeiraLinha = false;
				processaColaborador(tokens);
			}
			transaction.commit();
		} catch (Throwable ex) {
			transaction.rollback();
			throw ex;
		}
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
		if (col != null) {
			// O cara já existe
			col.setNome(tokens[1]);
			col.setCpf(tokens[2]);
			col.setSetor(setor);
			entityManager.merge(col);
		} else {
			// O cara é novo
			col = new Colaborador();
			col.setCodigo(codigo);
			col.setNome(tokens[1]);
			col.setCpf(tokens[2]);
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
			setor.setGerente(gerente);
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

	// TODO Tratar demissões

	// TODO Tratar remoção de setores

}
