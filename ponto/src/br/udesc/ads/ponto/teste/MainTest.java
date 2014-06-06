package br.udesc.ads.ponto.teste;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import br.udesc.ads.ponto.entidades.DiaSemana;
import br.udesc.ads.ponto.entidades.Escala;
import br.udesc.ads.ponto.entidades.EscalaMarcacao;
import br.udesc.ads.ponto.entidades.Feriado;

public class MainTest {

	public static void main(String[] args) {
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("ponto");
		EntityManager entityManager = factory.createEntityManager();
		
		
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();

		insereUmasEscalas(entityManager);
//		insereUnsFeriados(entityManager);
		
		transaction.commit();
	
		entityManager.close();
		
		factory.close();
		
		
	}

	private static void insereUmasEscalas(EntityManager entityManager) {
		
//		Escala escala = new Escala();
//		escala.setNome("Escala padrão");
//		List<EscalaMarcacao> marcacoes = escala.getMarcacoes();
//		
//		marcacoes.add(new EscalaMarcacao(DiaSemana.SEGUNDA_FEIRA, new LocalTime(8, 0)));
//		marcacoes.add(new EscalaMarcacao(DiaSemana.SEGUNDA_FEIRA, new LocalTime(12, 0)));
//		marcacoes.add(new EscalaMarcacao(DiaSemana.SEGUNDA_FEIRA, new LocalTime(13, 30)));
//		marcacoes.add(new EscalaMarcacao(DiaSemana.SEGUNDA_FEIRA, new LocalTime(18, 0)));
		
		entityManager.persist(new EscalaMarcacao(DiaSemana.DOMINGO, new LocalTime(8, 0)));
		entityManager.persist(new EscalaMarcacao(DiaSemana.SEGUNDA_FEIRA, new LocalTime(12, 00)));
		entityManager.persist(new EscalaMarcacao(DiaSemana.TERCA_FEIRA, new LocalTime(13, 30)));
		entityManager.persist(new EscalaMarcacao(DiaSemana.SABADO, new LocalTime(18, 01)));
		
		EscalaMarcacao marca = entityManager.find(EscalaMarcacao.class, 703L);
		System.out.println(marca);
	}

	private static void insereUnsFeriados(EntityManager entityManager) {
		Feriado feriado = new Feriado();
		feriado.setData(new LocalDate(2014, 12, 25));
		feriado.setNome("Natal");
		entityManager.persist(feriado);
		
		Feriado pascoa = new Feriado();
		pascoa.setData(new LocalDate(2014, 11, 15));
		pascoa.setNome("Proclamação da República");
		entityManager.persist(pascoa);
	}
	
}
