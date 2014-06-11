
package br.udesc.ads.ponto.services.leitoraponto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de confirmarLeitura complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="confirmarLeitura">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idInicial" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="idFinal" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "confirmarLeitura", propOrder = {
    "idInicial",
    "idFinal"
})
public class ConfirmarLeitura {

    protected long idInicial;
    protected long idFinal;

    /**
     * Obtém o valor da propriedade idInicial.
     * 
     */
    public long getIdInicial() {
        return idInicial;
    }

    /**
     * Define o valor da propriedade idInicial.
     * 
     */
    public void setIdInicial(long value) {
        this.idInicial = value;
    }

    /**
     * Obtém o valor da propriedade idFinal.
     * 
     */
    public long getIdFinal() {
        return idFinal;
    }

    /**
     * Define o valor da propriedade idFinal.
     * 
     */
    public void setIdFinal(long value) {
        this.idFinal = value;
    }

}
