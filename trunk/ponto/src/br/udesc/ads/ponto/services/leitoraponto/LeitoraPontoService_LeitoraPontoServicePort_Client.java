
package br.udesc.ads.ponto.services.leitoraponto;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.11
 * 2014-06-10T19:55:09.325-03:00
 * Generated source version: 2.7.11
 * 
 */
public final class LeitoraPontoService_LeitoraPontoServicePort_Client {

    private static final QName SERVICE_NAME = new QName("http://leitoraponto.services.ponto.ads.udesc.br/", "LeitoraPontoService");

    private LeitoraPontoService_LeitoraPontoServicePort_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = LeitoraPontoService_Service.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        LeitoraPontoService_Service ss = new LeitoraPontoService_Service(wsdlURL, SERVICE_NAME);
        LeitoraPontoService port = ss.getLeitoraPontoServicePort();  
        
        {
        System.out.println("Invoking confirmarLeitura...");
        long _confirmarLeitura_idInicial = -4110023410575201609l;
        long _confirmarLeitura_idFinal = 8750611852956772825l;
        port.confirmarLeitura(_confirmarLeitura_idInicial, _confirmarLeitura_idFinal);


        }
        {
        System.out.println("Invoking resetarConfirmacoes...");
        port.resetarConfirmacoes();


        }
        {
        System.out.println("Invoking lerMarcacoes...");
        int _lerMarcacoes_quantidade = -512333600;
        java.util.List<br.udesc.ads.ponto.services.leitoraponto.RegistroMarcacao> _lerMarcacoes__return = port.lerMarcacoes(_lerMarcacoes_quantidade);
        System.out.println("lerMarcacoes.result=" + _lerMarcacoes__return);


        }

        System.exit(0);
    }

}