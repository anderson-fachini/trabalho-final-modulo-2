package br.udesc.ads.ponto.servicos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.udesc.ads.ponto.entidades.Usuario;
import br.udesc.ads.ponto.util.CriptografaSenha;
import br.udesc.ads.ponto.util.EntityManagerUtil;

public class UsuarioService {
	
	private static EntityManager em = EntityManagerUtil.getEntityManager();
	
	/**
	 * Método que verifica as credenciais do usuário na base de dados
	 * @param nomeUsuario Nome do usuário
	 * @param senha Senha do Usuário
	 * @return Objeto usuário, caso tenha encontradou ou então null
	 */
	public Usuario buscaUsuarioAutenticado(String nomeUsuario, String senha) {
		Query query = em.createNamedQuery("checkUserAutentication");
		query.setParameter("nomeUsuario", nomeUsuario);
		query.setParameter("senha", CriptografaSenha.criptografa(senha));
		List<Usuario> usuarios = (List<Usuario>) query.getResultList();
		
		if (usuarios != null && usuarios.size() > 0) {
			return usuarios.get(0);
		}
		
		return null;
	}
	
}
