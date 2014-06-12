package br.udesc.ads.ponto.util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import br.udesc.ads.ponto.entidades.Usuario;

public class PhaseListenerLoginControl implements PhaseListener {

	private static final long serialVersionUID = -2895806001090580939L;

	@Override
	public void afterPhase(PhaseEvent arg0) {
		if (!JsfUtils.getContext().getViewRoot().getViewId().startsWith("/login")) {
			HttpSession session = (HttpSession) JsfUtils.getExternalContext().getSession(true);
			Usuario usuario = (Usuario) session.getAttribute(Constants.SESSION_USER_ATTR);
			
			if (usuario == null) {
				JsfUtils.redirectTo(Constants.PAGINA_LOGIN);
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
