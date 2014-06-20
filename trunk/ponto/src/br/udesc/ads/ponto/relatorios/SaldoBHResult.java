package br.udesc.ads.ponto.relatorios;

import java.util.ArrayList;
import java.util.List;

public class SaldoBHResult {

	private List<SaldoBHResultItem> itens = new ArrayList<>();

	public void addItem(SaldoBHResultItem item) {
		itens.add(item);
	}

	public List<SaldoBHResultItem> getItens() {
		return itens;
	}

}
