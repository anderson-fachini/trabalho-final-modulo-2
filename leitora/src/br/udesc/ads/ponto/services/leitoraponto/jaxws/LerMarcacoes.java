
package br.udesc.ads.ponto.services.leitoraponto.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.7.11
 * Tue Jun 10 19:46:05 BRT 2014
 * Generated source version: 2.7.11
 */

@XmlRootElement(name = "lerMarcacoes", namespace = "http://leitoraponto.services.ponto.ads.udesc.br/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lerMarcacoes", namespace = "http://leitoraponto.services.ponto.ads.udesc.br/")

public class LerMarcacoes {

    @XmlElement(name = "quantidade")
    private int quantidade;

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(int newQuantidade)  {
        this.quantidade = newQuantidade;
    }

}
