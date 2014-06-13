package br.udesc.ads.ponto.teste;

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
		
		Escala escala = new Escala();
		escala.setNome("Escala padrão");
		escala.addMarcacao(new EscalaMarcacao(DiaSemana.SEGUNDA_FEIRA, new LocalTime(8, 0)));
		escala.addMarcacao(new EscalaMarcacao(DiaSemana.SEGUNDA_FEIRA, new LocalTime(12, 0)));
		escala.addMarcacao(new EscalaMarcacao(DiaSemana.SEGUNDA_FEIRA, new LocalTime(13, 30)));
		escala.addMarcacao(new EscalaMarcacao(DiaSemana.SEGUNDA_FEIRA, new LocalTime(18, 0)));
		entityManager.persist(escala);
		Long id = escala.getId();
		
		Escala recuperada = entityManager.find(Escala.class, id);
		System.out.println(recuperada);
	}

	private static void insereUnsFeriados(EntityManager entityManager) {
		Feriado natal = new Feriado();
		natal.setData(new LocalDate(2014, 12, 25));
		natal.setNome("Natal");
		entityManager.persist(natal);
		
		Feriado proclamacao = new Feriado();
		proclamacao.setData(new LocalDate(2014, 11, 15));
		proclamacao.setNome("Proclamação da República");
		entityManager.persist(proclamacao);
	}
	
}
