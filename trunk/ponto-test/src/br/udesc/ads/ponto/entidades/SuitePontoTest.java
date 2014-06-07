package br.udesc.ads.ponto.entidades;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ //
TestFeriadoPersistence.class, //
		TestEscalaPersistence.class, //
		TestApuracaoPersistence.class //
})
public class SuitePontoTest {

}
