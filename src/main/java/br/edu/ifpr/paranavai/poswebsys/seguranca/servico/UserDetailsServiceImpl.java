package br.edu.ifpr.paranavai.poswebsys.seguranca.servico;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpr.paranavai.poswebsys.adm.dominio.Usuario;
import br.edu.ifpr.paranavai.poswebsys.adm.dominio.UsuarioRepositorio;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepositorio usuarioRepo;
	
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepo.findByUsername(username);
		
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(usuario.getRole()));
		
		User userSpring = new User(usuario.getUsername(), usuario.getPassword(), authorities);
		
		return userSpring;
	}
}
