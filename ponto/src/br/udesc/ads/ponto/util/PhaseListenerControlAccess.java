package br.udesc.ads.ponto.util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import br.udesc.ads.ponto.entidades.PerfilUsuario;
import br.udesc.ads.ponto.entidades.Usuario;

public class PhaseListenerControlAccess implements PhaseListener {

	private static final long serialVersionUID = -8205741260376659748L;
	
	public static final String PREFIXO_PAGINAS_IMPORTACAO = "/importar";
	public static final String PREFIXO_PAGINAS_CADASTRO = "/cadastro";	
	public static final String PREFIXO_PAGINA_AJUSTE_PONTO = "/ponto";
	public static final String PREFIXO_PAGINA_BH = "/banco_horas";
	public static final String PREFIXO_PAGINA_REL_APURACAO_PONTO = "/relatorios/resumo_apuracoes";
	public static final String PREFIXO_PAGINA_REL_HORAS_TRAB = "/relatorios/resumo_horas";
	
	@Override
	public void afterPhase(PhaseEvent arg0) {
		String viewId = JsfUtils.getContext().getViewRoot().getViewId();
		
		if (!viewId.startsWith("/login") && !viewId.startsWith("/index")) {
			HttpSession session = (HttpSession) JsfUtils.getExternalContext().getSession(true);
			Usuario usuario = (Usuario) session.getAttribute(Constants.SESSION_USER_ATTR);
			
			if (session != null && usuario != null && 
					(usuario.getPerfil() == PerfilUsuario.FUNCIONARIO_RH ||
					usuario.getPerfil() == PerfilUsuario.GERENTE)) {
				
				if (!isPerfilHavePermissionAccessPage(usuario.getPerfil(), viewId)) {
					JsfUtils.redirectTo(Constants.PAGINA_INDEX);
				}
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
	
	private boolean isPerfilHavePermissionAccessPage(PerfilUsuario perfil, String viewId) {
		if (perfil == PerfilUsuario.FUNCIONARIO_RH) {
			return isFuncionarioRhHavaPermissionAccessPage(viewId);
		}
		
		return isGerenteHavaPermissionAccessPage(viewId);
	}
	
	private boolean isFuncionarioRhHavaPermissionAccessPage(String viewId) {
		if (viewId.startsWith(PREFIXO_PAGINAS_IMPORTACAO) ||
				viewId.startsWith(PREFIXO_PAGINAS_CADASTRO) ||
				viewId.startsWith(PREFIXO_PAGINA_REL_APURACAO_PONTO)) {
			
			return true;
		}
		
		return false;
	}
	
	private boolean isGerenteHavaPermissionAccessPage(String viewId) {
		if (viewId.startsWith(PREFIXO_PAGINA_AJUSTE_PONTO) ||
				viewId.startsWith(PREFIXO_PAGINA_REL_APURACAO_PONTO) ||
				viewId.startsWith(PREFIXO_PAGINA_BH) ||
				viewId.startsWith(PREFIXO_PAGINA_REL_HORAS_TRAB)) {
			
			return true;
		}
		
		return false;
	}

}
