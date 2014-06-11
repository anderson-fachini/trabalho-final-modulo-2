package br.udesc.ads.ponto.services.leitoraponto;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.11
 * 2014-06-10T19:55:09.397-03:00
 * Generated source version: 2.7.11
 * 
 */
@WebService(targetNamespace = "http://leitoraponto.services.ponto.ads.udesc.br/", name = "LeitoraPontoService")
@XmlSeeAlso({ObjectFactory.class})
public interface LeitoraPontoService {

    @RequestWrapper(localName = "confirmarLeitura", targetNamespace = "http://leitoraponto.services.ponto.ads.udesc.br/", className = "br.udesc.ads.ponto.services.leitoraponto.ConfirmarLeitura")
    @WebMethod(action = "confirmarLeitura")
    @ResponseWrapper(localName = "confirmarLeituraResponse", targetNamespace = "http://leitoraponto.services.ponto.ads.udesc.br/", className = "br.udesc.ads.ponto.services.leitoraponto.ConfirmarLeituraResponse")
    public void confirmarLeitura(
        @WebParam(name = "idInicial", targetNamespace = "")
        long idInicial,
        @WebParam(name = "idFinal", targetNamespace = "")
        long idFinal
    );

    @RequestWrapper(localName = "resetarConfirmacoes", targetNamespace = "http://leitoraponto.services.ponto.ads.udesc.br/", className = "br.udesc.ads.ponto.services.leitoraponto.ResetarConfirmacoes")
    @WebMethod(action = "resetarConfirmacoes")
    @ResponseWrapper(localName = "resetarConfirmacoesResponse", targetNamespace = "http://leitoraponto.services.ponto.ads.udesc.br/", className = "br.udesc.ads.ponto.services.leitoraponto.ResetarConfirmacoesResponse")
    public void resetarConfirmacoes();

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "lerMarcacoes", targetNamespace = "http://leitoraponto.services.ponto.ads.udesc.br/", className = "br.udesc.ads.ponto.services.leitoraponto.LerMarcacoes")
    @WebMethod(action = "lerMarcacoes")
    @ResponseWrapper(localName = "lerMarcacoesResponse", targetNamespace = "http://leitoraponto.services.ponto.ads.udesc.br/", className = "br.udesc.ads.ponto.services.leitoraponto.LerMarcacoesResponse")
    public java.util.List<br.udesc.ads.ponto.services.leitoraponto.RegistroMarcacao> lerMarcacoes(
        @WebParam(name = "quantidade", targetNamespace = "")
        int quantidade
    );
}
