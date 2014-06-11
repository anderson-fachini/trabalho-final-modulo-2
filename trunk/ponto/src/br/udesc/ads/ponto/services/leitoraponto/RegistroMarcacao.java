
package br.udesc.ads.ponto.services.leitoraponto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de registroMarcacao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="registroMarcacao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codFuncionario" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="marcacao" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registroMarcacao", propOrder = {
    "codFuncionario",
    "id",
    "marcacao"
})
public class RegistroMarcacao {

    protected Long codFuncionario;
    protected Long id;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar marcacao;

    /**
     * Obtém o valor da propriedade codFuncionario.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodFuncionario() {
        return codFuncionario;
    }

    /**
     * Define o valor da propriedade codFuncionario.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodFuncionario(Long value) {
        this.codFuncionario = value;
    }

    /**
     * Obtém o valor da propriedade id.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o valor da propriedade id.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Obtém o valor da propriedade marcacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMarcacao() {
        return marcacao;
    }

    /**
     * Define o valor da propriedade marcacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMarcacao(XMLGregorianCalendar value) {
        this.marcacao = value;
    }

}
