package br.udesc.ads.ponto.util;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class JsfUtils {
	
	/**
	 * Adiciona uma mensagem de erro na tela
	 * @param idComponente Id do componente
	 * @param msg Mensagem
	 */
	public static void addMensagemErro(String idComponente, String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				msg, msg);
		getContext().addMessage(idComponente, facesMsg);
	}
	
	/**
	 * Adiciona uma mensagem de erro na tela
	 * @param msg Mensagem
	 */
	public static void addMensagemErro(String msg) {
		addMensagemErro(null, msg);
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
