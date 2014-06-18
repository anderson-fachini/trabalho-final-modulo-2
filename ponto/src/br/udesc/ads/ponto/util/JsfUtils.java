package br.udesc.ads.ponto.util;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class JsfUtils {
	
	/**
	 * Adiciona uma mensagem de informação na tela
	 * @param msg Mensagem
	 */
	public static void addMensagemInfo(String msg) {
		addMensagemInfo(null, msg);
	}
	
	/**
	 * Adiciona uma mensagem de informação na tela
	 * @param idComponente Id do componente
	 * @param msg Mensagem
	 */
	public static void addMensagemInfo(String idComponente, String msg) {
		addMensagem(null, msg, FacesMessage.SEVERITY_INFO);
	}
	
	/**
	 * Adiciona uma mensagem de warning na tela
	 * @param msg Mensagem
	 */
	public static void addMensagemWarning(String msg) {
		addMensagemWarning(null, msg);
	}
	
	/**
	 * Adiciona uma mensagem de warning na tela
	 * @param idComponente Id do componente
	 * @param msg Mensagem
	 */
	public static void addMensagemWarning(String idComponente, String msg) {
		addMensagem(null, msg, FacesMessage.SEVERITY_WARN);
	}
	
	/**
	 * Adiciona uma mensagem de erro na tela
	 * @param msg Mensagem
	 */
	public static void addMensagemErro(String msg) {
		addMensagemErro(null, msg);
	}
	
	/**
	 * Adiciona uma mensagem de erro na tela
	 * @param idComponente Id do componente
	 * @param msg Mensagem
	 */
	public static void addMensagemErro(String idComponente, String msg) {
		addMensagem(null, msg, FacesMessage.SEVERITY_ERROR);
	}
	
	private static void addMensagem(String idComponente, String msg, Severity severidade) {
		FacesMessage facesMsg = new FacesMessage(severidade, msg, null);
		getContext().addMessage(idComponente, facesMsg);
	}

	/**
	 * Obtêm o contexto externo atual
	 * @return
	 */
	public static ExternalContext getExternalContext() {
		return getContext().getExternalContext();
	}

	/**
	 * Redireciona para uma páginda da aplicação
	 * @param pagina Página para redirecionar (já deve conter a barra "/" na frente do nome)
	 */
	public static void redirectTo(String pagina) {
		try {
			getExternalContext().redirect(Constants.APPLICATION_URL + pagina);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Obtêm a instância atual do FacesContext
	 * @return
	 */
	public static FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}
}
