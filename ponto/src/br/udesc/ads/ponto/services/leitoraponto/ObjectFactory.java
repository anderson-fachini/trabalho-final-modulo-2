
package br.udesc.ads.ponto.services.leitoraponto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.udesc.ads.ponto.services.leitoraponto package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ResetarConfirmacoes_QNAME = new QName("http://leitoraponto.services.ponto.ads.udesc.br/", "resetarConfirmacoes");
    private final static QName _LerMarcacoesResponse_QNAME = new QName("http://leitoraponto.services.ponto.ads.udesc.br/", "lerMarcacoesResponse");
    private final static QName _ConfirmarLeituraResponse_QNAME = new QName("http://leitoraponto.services.ponto.ads.udesc.br/", "confirmarLeituraResponse");
    private final static QName _LerMarcacoes_QNAME = new QName("http://leitoraponto.services.ponto.ads.udesc.br/", "lerMarcacoes");
    private final static QName _ConfirmarLeitura_QNAME = new QName("http://leitoraponto.services.ponto.ads.udesc.br/", "confirmarLeitura");
    private final static QName _ResetarConfirmacoesResponse_QNAME = new QName("http://leitoraponto.services.ponto.ads.udesc.br/", "resetarConfirmacoesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.udesc.ads.ponto.services.leitoraponto
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LerMarcacoes }
     * 
     */
    public LerMarcacoes createLerMarcacoes() {
        return new LerMarcacoes();
    }

    /**
     * Create an instance of {@link ConfirmarLeituraResponse }
     * 
     */
    public ConfirmarLeituraResponse createConfirmarLeituraResponse() {
        return new ConfirmarLeituraResponse();
    }

    /**
     * Create an instance of {@link ResetarConfirmacoesResponse }
     * 
     */
    public ResetarConfirmacoesResponse createResetarConfirmacoesResponse() {
        return new ResetarConfirmacoesResponse();
    }

    /**
     * Create an instance of {@link ConfirmarLeitura }
     * 
     */
    public ConfirmarLeitura createConfirmarLeitura() {
        return new ConfirmarLeitura();
    }

    /**
     * Create an instance of {@link ResetarConfirmacoes }
     * 
     */
    public ResetarConfirmacoes createResetarConfirmacoes() {
        return new ResetarConfirmacoes();
    }

    /**
     * Create an instance of {@link LerMarcacoesResponse }
     * 
     */
    public LerMarcacoesResponse createLerMarcacoesResponse() {
        return new LerMarcacoesResponse();
    }

    /**
     * Create an instance of {@link RegistroMarcacao }
     * 
     */
    public RegistroMarcacao createRegistroMarcacao() {
        return new RegistroMarcacao();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResetarConfirmacoes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leitoraponto.services.ponto.ads.udesc.br/", name = "resetarConfirmacoes")
    public JAXBElement<ResetarConfirmacoes> createResetarConfirmacoes(ResetarConfirmacoes value) {
        return new JAXBElement<ResetarConfirmacoes>(_ResetarConfirmacoes_QNAME, ResetarConfirmacoes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LerMarcacoesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leitoraponto.services.ponto.ads.udesc.br/", name = "lerMarcacoesResponse")
    public JAXBElement<LerMarcacoesResponse> createLerMarcacoesResponse(LerMarcacoesResponse value) {
        return new JAXBElement<LerMarcacoesResponse>(_LerMarcacoesResponse_QNAME, LerMarcacoesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfirmarLeituraResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leitoraponto.services.ponto.ads.udesc.br/", name = "confirmarLeituraResponse")
    public JAXBElement<ConfirmarLeituraResponse> createConfirmarLeituraResponse(ConfirmarLeituraResponse value) {
        return new JAXBElement<ConfirmarLeituraResponse>(_ConfirmarLeituraResponse_QNAME, ConfirmarLeituraResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LerMarcacoes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leitoraponto.services.ponto.ads.udesc.br/", name = "lerMarcacoes")
    public JAXBElement<LerMarcacoes> createLerMarcacoes(LerMarcacoes value) {
        return new JAXBElement<LerMarcacoes>(_LerMarcacoes_QNAME, LerMarcacoes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfirmarLeitura }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leitoraponto.services.ponto.ads.udesc.br/", name = "confirmarLeitura")
    public JAXBElement<ConfirmarLeitura> createConfirmarLeitura(ConfirmarLeitura value) {
        return new JAXBElement<ConfirmarLeitura>(_ConfirmarLeitura_QNAME, ConfirmarLeitura.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResetarConfirmacoesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://leitoraponto.services.ponto.ads.udesc.br/", name = "resetarConfirmacoesResponse")
    public JAXBElement<ResetarConfirmacoesResponse> createResetarConfirmacoesResponse(ResetarConfirmacoesResponse value) {
        return new JAXBElement<ResetarConfirmacoesResponse>(_ResetarConfirmacoesResponse_QNAME, ResetarConfirmacoesResponse.class, null, value);
    }

}
