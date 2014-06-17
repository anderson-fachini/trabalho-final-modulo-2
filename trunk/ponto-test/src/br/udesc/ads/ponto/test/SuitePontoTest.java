package br.udesc.ads.ponto.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.udesc.ads.ponto.entidades.SuitePersistenceTest;
import br.udesc.ads.ponto.servicos.SuiteServicesTest;

@RunWith(Suite.class)
@SuiteClasses({//
SuitePersistenceTest.class, //
		SuiteServicesTest.class, //
})
public class SuitePontoTest {

}
